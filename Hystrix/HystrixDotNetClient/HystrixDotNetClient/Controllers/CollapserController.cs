using System.Collections.Generic;
using System.Linq;
using HystrixDotNetClient.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Steeltoe.CircuitBreaker.Hystrix;

namespace HystrixDotNetClient.Controllers
{
    public class CollapserController : Controller
    {
        private readonly ILoggerFactory _loggerFactory;

        public CollapserController(ILoggerFactory loggerFactory)
        {
            _loggerFactory = loggerFactory;
        }

        [Route("/getById/{id}")]
        public User getById(int id)
        {
            var collapserOptions = new HystrixCollapserOptions(HystrixCollapserKeyDefault.AsKey("UserCollapser"), RequestCollapserScope.GLOBAL)
            {
                TimerDelayInMilliseconds = 5000,
                MaxRequestsInBatch = 10,
                RequestCacheEnabled = true
            };
            var collapser = new UserServiceCollapser(collapserOptions, _loggerFactory);
            collapser.UserId = id;
            return collapser.Execute();
        }

    }

    public class UserServiceCollapser : HystrixCollapser<List<User>, User, int>
    {
        ILogger<UserServiceCollapser> _logger;
        ILoggerFactory _loggerFactory;

        public static Dictionary<int, User> UserStore { get; set; } = new Dictionary<int, User>()
        {
            {1, new User() {Id = 1, Name = "Andrew"}},
            {2, new User() {Id = 2, Name = "Alex"}},
            {3, new User() {Id = 2, Name = "John"}},
            {4, new User() {Id = 3, Name = "Bob"}},
            {5, new User() {Id = 4, Name = "Seth"}},
        };

        

        public UserServiceCollapser(IHystrixCollapserOptions options, ILoggerFactory logFactory) : base(options)
        {
            _logger = logFactory.CreateLogger<UserServiceCollapser>();
            _loggerFactory = logFactory;
            
        }

        public virtual int UserId { get; set; }

        public override int RequestArgument => UserId;

        protected override HystrixCommand<List<User>> CreateCommand(ICollection<ICollapsedRequest<User, int>> requests)
        {
            _logger.LogInformation("Creating batch command to handle {0} number of requests", requests.Count);
            return new HystrixCommand<List<User>>(
                group: HystrixCommandGroupKeyDefault.AsKey("UserCollapser"), 
                run: () =>  requests.Select(x => UserStore.GetValueOrDefault(x.Argument)).ToList(), 
                fallback: () => new User[requests.Count].ToList() );
        }

        protected override void MapResponseToRequests(List<User> batchResponse, ICollection<ICollapsedRequest<User, int>> requests)
        {
            
            foreach (var f in batchResponse)
            {
                foreach (var r in requests)
                {
                    if (r.Argument == f.Id)
                    {
                        r.Response = f;
                    }
                }
            }
        }
    }
}

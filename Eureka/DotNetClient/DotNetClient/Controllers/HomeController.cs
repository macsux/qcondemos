using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json.Linq;
using Steeltoe.Common.Discovery;

namespace DotNetClient.Controllers
{
    public class HomeController : Controller
    {
        private readonly IDiscoveryClient _discoveryClient;

        public HomeController(IDiscoveryClient discoveryClient)
        {
            _discoveryClient = discoveryClient;
        }
        [Route("/")]
        public IActionResult Index()
        {
            var services = _discoveryClient.Services.Select(service =>
                new
                {
                    ServiceName = service,
                    Instances = _discoveryClient.GetInstances(service)
                }
            );

            return Json(services);
        }
        [Route("/hello")]
        public string Hello()
        {
            return "Hello Spring World from .NET world";
        }

        [Route("/ask")]
        public async Task<string> Ask()
        {
            var httpClient = new HttpClient(new DiscoveryHttpClientHandler(_discoveryClient));
            try
            {
                return await httpClient.GetStringAsync("http://EurekaSpringClient/hello");
            }
            catch (Exception e)
            {
                return e.Message;
            }
        }
    }
}

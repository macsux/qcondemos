using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using HystrixDotNetClient.Controllers;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Steeltoe.CircuitBreaker.Hystrix;

namespace HystrixDotNetClient
{
    public class Startup
    {
        private readonly IConfiguration _configuration;

        public Startup(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        public void ConfigureServices(IServiceCollection services)
        {
            services.AddMvc();
            services.AddHystrixCollapser<UserServiceCollapser>("UserCollapser", RequestCollapserScope.GLOBAL, _configuration);
            services.AddHystrixMetricsStream(_configuration);
            
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IHostingEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }
            app.UseHystrixRequestContext(); // ensures that HttpContext is accessible inside Hystrix Commands
            app.UseMvc();

            app.UseHystrixMetricsStream(); // app will now publish hystrix statistics at /hystrix/hystrix.stream endpoint (http://localhost:51550/hystrix/hystrix.stream)

        }
    }
}

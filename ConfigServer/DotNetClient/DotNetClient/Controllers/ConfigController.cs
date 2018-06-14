using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace DotNetClient.Controllers
{
    [Produces("application/json")]
    public class ConfigController : Controller
    {
        [HttpPost]
        [Route("/refresh")]
        public void Refresh()
        {
            
        }
    }
}
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using DotNetClient.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;

namespace DotNetClient.Controllers
{
    public class HomeController : Controller
    {
        public IActionResult Index([FromServices]IOptionsSnapshot<MyOptions> options)
        {
            return View(options.Value);
        }
        
    }
}

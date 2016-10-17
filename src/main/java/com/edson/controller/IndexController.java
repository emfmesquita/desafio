
package com.edson.controller;


import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.edson.core.SKUService;


@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private SKUService skuService;
    
    @RequestMapping(method = { GET })
    public String index(ModelMap model) {    	
    	model.addAttribute("skus", skuService.list());
        return "index";
    }
    
    @RequestMapping(value = "/create", method = { GET, POST })
    public String create(ModelMap model) {
    	skuService.createSKU();
    	model.addAttribute("skus", skuService.list());
        return "index";
    }
}

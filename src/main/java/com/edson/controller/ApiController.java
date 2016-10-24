package com.edson.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.edson.core.SKUService;

@Controller
@RequestMapping("/api")
public class ApiController {

	@Autowired
	private SKUService skuService;

	@RequestMapping(value = "/create", method = { POST })
	public String create(ModelMap model, @RequestBody String skuJson) {
		String id = skuService.createFromJson(skuJson);
		model.addAttribute("id", id);
		return "create";
	}

	@RequestMapping(value = "/update", method = { POST })
	public String update(@RequestBody String skuJson) {
		skuService.updateFromJson(skuJson);
		return "update";
	}

	@RequestMapping(value = "/delete/{id}", method = { POST })
	public String delete(@PathVariable String id) {
		skuService.deleteSku(id);
		return "delete";
	}

	@RequestMapping(value = "/find/{id}", method = { GET })
	public String find(ModelMap model, @PathVariable String id) {
		String skuJson = skuService.find(id);
		model.addAttribute("content", skuJson);
		return "content";
	}

	@RequestMapping(value = "/list", method = { GET })
	public String list(ModelMap model) {
		String skusJson = skuService.list();
		model.addAttribute("content", skusJson);
		return "content";
	}
    
    @RequestMapping(value = "/available", method = { GET })
    public String available(ModelMap model) {
        String skusJson = skuService.available();
        model.addAttribute("content", skusJson);
        return "content";
    }
}

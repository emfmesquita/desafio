package com.edson.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.edson.core.SKUService;

@Controller
@RequestMapping("/notification")
public class NotificationController {

	@Autowired
	private SKUService skuService;

	@RequestMapping(method = { POST })
	public void notificate(ModelMap model, @RequestBody String notifications) throws InterruptedException {
		skuService.createFromNotifications(notifications);
	}
}

//Xử lý toàn bộ mapping liên quan đến người dùng
package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.project.model.User;
import com.project.service.UserService;
import com.project.util.EncrytedPasswordUtils;
import com.project.util.WebUtils;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

@Controller
public class UserController {
 
    @Autowired
    private UserService service;
     
    // handler methods...
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView viewLoginPage(@RequestParam(value = "error", required = false) String error) {
        ModelAndView  modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        
        return modelAndView;
         
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute("User") User user) {
    
    	String registerName = user.getUsername();
    	
    	if (service.findByUsername(registerName) == null)
    	{
    		user.setRole("ROLE_USER");
        	user.setPassword(EncrytedPasswordUtils.encrytePassword(user.getPassword()));
            service.save(user);
    	} else {
    		System.out.println("Account already exists in database!");
    	}
    	return "login";
       }
   
}

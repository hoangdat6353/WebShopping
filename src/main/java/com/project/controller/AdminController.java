//Xử lý toàn bộ mapping liên quan đến Admin
package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.project.model.User;
import com.project.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class AdminController {
 
    @Autowired
    private UserService service;
     
}

//Xử lý toàn bộ mapping liên quan đến Admin
package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.project.model.Categories;
//import com.project.model.Cards;
import com.project.service.CategoriesService;
//import com.project.service.CardsService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class AdminController {
 
    @Autowired
    private CategoriesService service;
    //@Autowired
    //private CardsService service1;
    
     
    @RequestMapping("/categories")
    public String viewCategories(Model model) {
    	 List<Categories> listCategories = service.listAll();
         model.addAttribute("listCategories", listCategories);
          
         return "categories";
    }
//    
//    
    @RequestMapping(value = "/addcategories", method = RequestMethod.POST)
    public String saveCategories(@ModelAttribute("Categories") Categories categories) {
        service.save(categories);
         
        return "redirect:/categories";
    }
    
    
    @RequestMapping("/editcategories/{id}")
    public String showEditCategoriesPage(@PathVariable(name = "id") int id, Model model) {
    	Categories categories = service.get(id);
    	model.addAttribute("categories", categories);
         
    	return "edit_categories";
    }
    
    @RequestMapping("/deletecategories/{id}")
    public String deleteProduct(@PathVariable(name = "id") int id) {
        service.delete(id);
        return "redirect:/categories";       
    }
    
//   
//    
//    
    
// // handler methods...
//    @RequestMapping("/cards")
//    public String viewCards(Model model) {
//    	 List<Cards> listCards = service1.listAll();
//         model.addAttribute("listCards", listCards);
//          
//         return "cards";
//    }
//    

    
//    @RequestMapping(value = "/newcards", method = RequestMethod.POST)
//    public String saveCards(@ModelAttribute("Cards") Cards cards) {
//    	
//        service1.save(cards);
//         
//        return "redirect:/cards";
//    }
    
    
//    @RequestMapping("/editcards/{id}")
//    public String showEditCategoriesPage(@PathVariable(name = "id") int id, Model model) {
//    	Cards cards = service1.get(id);
//    	model.addAttribute("cards", cards);
//         
//    	return "edit_categories";
//    }
    
//    @RequestMapping("/deletecards/{id}")
//    public String deleteCards(@PathVariable(name = "id") int id) {
//        service1.delete(id);
//        return "redirect:/cards";       
//    }
}
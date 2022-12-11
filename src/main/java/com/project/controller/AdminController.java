//Xử lý toàn bộ mapping liên quan đến Admin
package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.model.App;
import com.project.model.Cards;
import com.project.model.Categories;
import com.project.model.Developer;
import com.project.model.PendingApp;
import com.project.model.User;
import com.project.service.AppService;
import com.project.service.CardsService;
//import com.project.model.Cards;
import com.project.service.CategoriesService;
import com.project.service.PendingAppService;
//import com.project.service.CardsService;
import com.project.service.UserService;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class AdminController {
 
    @Autowired
    private CategoriesService categoryService;
    @Autowired
    private UserService service;
    @Autowired
    private AppService appService;
    @Autowired
    private PendingAppService pendingService;
    @Autowired
    private CardsService cardService;
    //@Autowired
    //private CardsService service1;
    @RequestMapping(value = "/index-admin", method = RequestMethod.GET)
    public String viewIndexDev(Model model,Principal principal) {
        String userName = principal.getName();
        User loginedUser = service.findByUsername(userName);
        
        List<User> listUsers = service.listAll();
        List<App> listApps = appService.listAll();
        List<Categories> listCategories = categoryService.listAll();
        List<App> listFreeApps = appService.listAllAppsByStatusAndPayment("Publish", "Free");
        List<App> listPaidApps = appService.listAllAppsByStatusAndPayment("Publish", "Paid");
        
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("listFreeApps", listFreeApps);
        model.addAttribute("listPaidApps", listPaidApps);
        model.addAttribute("totalUsers", listUsers.size());
        model.addAttribute("totalApps", listApps.size());
        model.addAttribute("User", loginedUser);      
        
        return "index-admin";
    }
    
    @RequestMapping(value = "/verify-apps", method = RequestMethod.GET)
    public String viewVerifyPage(Model model,Principal principal) {
        String userName = principal.getName();
        User loginedUser = service.findByUsername(userName);
        
        List<PendingApp> listPendingApps = pendingService.listAllPendingAppsByStatus("Chờ duyệt");
        
        model.addAttribute("listPendingApps", listPendingApps);
        model.addAttribute("User", loginedUser);      
        
        return "verify-apps";
    }
    
    @RequestMapping(value = "/verify-app", method = RequestMethod.POST)
    public String handleVerifyApp(@RequestParam("id") int id,
    		@RequestParam("approval") String approval,
    		RedirectAttributes redirectAttr) {
		PendingApp pendingApp = pendingService.get(id);

    	if (approval.equals("accepted"))
    	{
    		
    		App newApp = new App();
    		newApp.setAppname(pendingApp.getAppname());
    		newApp.setCategory(pendingApp.getCategory());
    		newApp.setDetaildescription(pendingApp.getDetaildescription());
    		newApp.setDeveloper(pendingApp.getDeveloper());
    		newApp.setFilesize(pendingApp.getFilesize());
    		newApp.setIconimage(pendingApp.getIconimage());
    		newApp.setInstallfile(pendingApp.getInstallfile());
    		newApp.setPayment(pendingApp.getPayment());
    		newApp.setPrice(pendingApp.getPrice());
    		newApp.setScreenshot1(pendingApp.getScreenshot1());
    		newApp.setScreenshot2(pendingApp.getScreenshot2());
    		newApp.setScreenshot3(pendingApp.getScreenshot3());
    		newApp.setScreenshot4(pendingApp.getScreenshot4());
    		newApp.setShortdescription(pendingApp.getShortdescription());
    		newApp.setUploaddate(pendingApp.getUploaddate());
    		newApp.setStatus("Publish");
    		newApp.setDownloads(0);
    		
    		appService.save(newApp);
    		
    		pendingApp.setStatus("Publish");
    		pendingService.save(pendingApp);
    		redirectAttr.addFlashAttribute("message", "ỨNG DỤNG ĐÃ ĐƯỢC XÉT DUYỆT THÀNH CÔNG !");
    	} else if (approval.equals("denied"))
    	{
    		pendingApp.setStatus("Từ chối");
    		pendingService.save(pendingApp);
    		redirectAttr.addFlashAttribute("message", "ĐÃ TỪ CHỐI YÊU CẦU XÉT DUYỆT CỦA ỨNG DỤNG !");

    	}
        return "redirect:/verify-apps";
    }
    
    @RequestMapping("/accounts")
    public String viewUser(Model model) {
    	 List<User> listUser = service.listAll();
         model.addAttribute("listUsers", listUser);
          
         return "account";
    }
      
    @RequestMapping("/edituser/{id}")
    public String showEditUserPage(@PathVariable(name = "id") int id, Model model) {
    	User users = service.get(id);
    	model.addAttribute("users", users);
        
    	return "edit_user";
    }
    
    @RequestMapping("/deleteuser/{id}")
    public String deleteUser(@PathVariable(name = "id") int id) {
        service.delete(id);
        return "redirect:/account";       
    }
    
    @RequestMapping("/cards")
    public String viewCards(Model model) {
    	 List<Cards> listCards = cardService.listAll();
         model.addAttribute("listCards", listCards);
         model.addAttribute("cards", new Cards());
              
         return "cards";
    }
    

    
    @RequestMapping(value = "/addcards", method = RequestMethod.POST)
    public String saveCards(@ModelAttribute("Cards") Cards cards) {
        cardService.save(cards);
        return "redirect:/cards";
    }
    
    
    @RequestMapping("/editcards/{id}")
    public String showEditCardsPage(@PathVariable(name = "id") int id, Model model) {
    	Cards cards = cardService.get(id);
    	model.addAttribute("cards", cards);
         
    	return "edit_cards";
    }
    
    @RequestMapping("/deletecards/{id}")
    public String deleteCards(@PathVariable(name = "id") int id) {
        cardService.delete(id);
        return "redirect:/cards";       
    }
     
    @RequestMapping("/categories")
    public String viewCategories(Model model) {
    	 List<Categories> listCategories = categoryService.listAll();
         model.addAttribute("listCategories", listCategories);
          
         return "categories";
    }
//    
//    
    @RequestMapping(value = "/addcategories", method = RequestMethod.POST)
    public String saveCategories(@ModelAttribute("Categories") Categories categories) {
        categoryService.save(categories);
         
        return "redirect:/categories";
    }
    
    
    @RequestMapping("/editcategories/{id}")
    public String showEditCategoriesPage(@PathVariable(name = "id") int id, Model model) {
    	Categories categories = categoryService.get(id);
    	model.addAttribute("categories", categories);
         
    	return "edit_categories";
    }
    
    @RequestMapping("/deletecategories/{id}")
    public String deleteProduct(@PathVariable(name = "id") int id) {
        categoryService.delete(id);
        return "redirect:/categories";       
    }
   
    
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
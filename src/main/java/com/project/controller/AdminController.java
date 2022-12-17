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
import com.project.model.EmailDetails;
import com.project.model.PendingApp;
import com.project.model.User;
import com.project.service.AppService;
import com.project.service.CardsService;
//import com.project.model.Cards;
import com.project.service.CategoriesService;
import com.project.service.DeveloperService;
import com.project.service.EmailService;
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
    @Autowired 
    private EmailService emailService;
    @Autowired
	 private DeveloperService developerService;
    
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

        EmailDetails emailDetails = new EmailDetails();
        Developer currDev = developerService.findByDevname(pendingApp.getDeveloper());
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
    		
    		if (currDev != null)
    		{
    			String content = "Ứng dụng yêu cầu được xét duyệt của bạn trên hệ thống hiện tại đã được xét duyệt thành công và đã xuất hiện trên trang web của chúng tôi\n"
    					+ "Vui lòng kiểm tra ứng dụng tại trang quản trị dành cho nhà phát triển\n"
    					+"Nếu có thêm thắc mắc, vui lòng liên hệ chúng tôi để được giải đáp sớm nhất. Xin cảm ơn";
                emailDetails.setRecipient(currDev.getDevemail());
                emailDetails.setSubject("Thông Báo Ứng Dụng Của Bạn Đã Được Duyệt Thành Công - AppShopping");
                emailDetails.setMsgBody(content);
                emailService.sendSimpleMail(emailDetails);
    		}
    	
    		redirectAttr.addFlashAttribute("message", "ỨNG DỤNG ĐÃ ĐƯỢC XÉT DUYỆT THÀNH CÔNG !");
    	} else if (approval.equals("denied"))
    	{
    		pendingApp.setStatus("Từ chối");
    		pendingService.save(pendingApp);
    		
    		if (currDev != null)
    		{
    			String content = "Ứng dụng yêu cầu được xét duyệt của bạn trên hệ thống hiện tại đã bị từ chối. Vui lòng cập nhật các thông tin của ứng dụng\n"
    			+ "đúng với tiêu chuẩn của hệ thống chúng tôi và gửi yêu cầu xét duyệt lần nữa nếu có nhu cầu!"
    			+"Nếu có thêm thắc mắc, vui lòng liên hệ chúng tôi để được giải đáp sớm nhất. Xin cảm ơn";
                emailDetails.setRecipient(currDev.getDevemail());
                emailDetails.setSubject("Thông Báo Ứng Dụng Của Bạn Không Được Phê Duyệt Tại Hệ Thống - AppShopping");
                emailDetails.setMsgBody(content);
                emailService.sendSimpleMail(emailDetails);
    		}
    		redirectAttr.addFlashAttribute("message", "ĐÃ TỪ CHỐI YÊU CẦU XÉT DUYỆT CỦA ỨNG DỤNG !");

    	}
        return "redirect:/verify-apps";
    }
    
    @RequestMapping("/accounts")
    public String viewUser(Model model) {
    	List<User> listUser = service.listAll();
        model.addAttribute("listUser", listUser);
        model.addAttribute("users", new User());
          
         return "account";
    }
    
    @RequestMapping(value = "/adduser", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("user") User users, RedirectAttributes redirectAttr) {
    	String username = users.getUsername();
    	String email = users.getEmail();
    	String role = users.getRole();
    	
    	//Lấy currentUser ra
    	User currUser = service.get(users.getId());
    	
    	if(username == "" || email == "" ||  role == "") {
    		if(users.getId() != null) {
				redirectAttr.addFlashAttribute("message", "Cập nhật user thất bại! Vui lòng điền đầy đủ thông tin");
			} else {
				redirectAttr.addFlashAttribute("message", "Thêm user thất bại! Vui lòng điền đầy đủ thông tin");
	    	}
		}
    	else {
    		//Nếu không trùng username và email thì update lại thông tin của currUser
    		if (service.findByUsername(username) == null && service.findByEmail(email) == null) {
    			currUser.setUsername(username);
    			currUser.setEmail(email);
    			currUser.setRole(role);
    			service.save(currUser);
        	}
        	else {
        		if(users.getId() != null) {
        			redirectAttr.addFlashAttribute("message", "Cập nhật user thất bại! user đã có trong hệ thống");
        		}
        		else{
        			redirectAttr.addFlashAttribute("message", "Thêm user thất bại! user đã có trong hệ thống");
        		}
        	}
    	} 
    	
        
        return "redirect:/accounts";
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
        return "redirect:/accounts";       
    }
    
    @RequestMapping("/cards")
    public String viewCards(Model model) {
    	 List<Cards> listCards = cardService.listAll();
         model.addAttribute("listCards", listCards);
         model.addAttribute("cards", new Cards());
              
         return "cards";
    }
   
   
    @RequestMapping(value = "/addcards", method = RequestMethod.POST)
    public String saveCards(RedirectAttributes redirectAttr,@ModelAttribute("Cards") Cards cards) {
    	
    	int soseri = cards.getSoseri();
    	int mathecao = cards.getMathecao();
    	
    	if((soseri == 0 || mathecao == 0)) {
    		if(cards.getId() != null) {
				redirectAttr.addFlashAttribute("message", "Cập nhật thẻ thất bại! Vui lòng điền đầy đủ thông tin");
			} else {
				redirectAttr.addFlashAttribute("message", "Thêm thẻ thất bại! Vui lòng điền đầy đủ thông tin");
	    	}
		}
    	else {
    			if (cardService.findCardsByCode(mathecao) == null && cardService.findCardsBySeri(soseri) == null) {
    				
//        			int mathecao1 = Integer.parseInt(mathecao);
//        			int soseri1 = Integer.parseInt(soseri);
    				cardService.save(cards);
            	}
            	else {
            		if(cards.getId() != null) {
            			redirectAttr.addFlashAttribute("message", "Cập nhật thẻ thất bại! Thẻ đã có trong hệ thống");
            		}
            		else{
            			redirectAttr.addFlashAttribute("message", "Thêm thẻ thất bại! Thẻ đã có trong hệ thống");
            		}
            	}
    		} 
    	
        return "redirect:/cards";
    }
    
    
    @RequestMapping("/editcards/{id}")
    public String showEditCategoriesPage(@PathVariable(name = "id") int id, Model model, @ModelAttribute("Cards") Cards cards) {
    	
    	Integer soseri = cards.getSoseri();
    	Integer mathecao = cards.getMathecao();
    	cards = cardService.get(id);
    	
    	if (cardService.findCardsByCode(mathecao) == null && cardService.findCardsBySeri(soseri) == null) {
    		model.addAttribute("cards", cards);
    	}
    	
         
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

    
    @RequestMapping(value = "/addcategories", method = RequestMethod.POST)
    public String saveCategories(RedirectAttributes redirectAttr, @ModelAttribute("Categories") Categories categories) {
    	
    	String tentheloai = categories.getTentheloai();
    	
    	if(tentheloai == "") {
    		if(categories.getId() != null) {
				redirectAttr.addFlashAttribute("message", "Cập nhật thể loại thất bại! Vui lòng điền đầy đủ thông tin");
			} else {
				redirectAttr.addFlashAttribute("message", "Thêm thể loại thất bại! Vui lòng điền đầy đủ thông tin");
	    	}
		}
    	else {
    		if (categoryService.findByTentheloai(tentheloai) == null) {
    			categoryService.save(categories);
    		}
    		else {
    			if(categories.getId() != null) {
    				redirectAttr.addFlashAttribute("message", "Cập nhật thể loại thất bại! Thể loại đã có trong hệ thống");
    			}
    			else{
    				redirectAttr.addFlashAttribute("message", "Thêm thể loại thất bại! Thể loại đã có trong hệ thống");
    			}
    		}
    	} 
    		 
        return "redirect:/categories";
    }
    
    
    @RequestMapping("/editcategories/{id}")
    public String showEditCategoriesPage(RedirectAttributes redirectAttr, @PathVariable(name = "id") int id, Model model,@ModelAttribute("Categories") Categories categories) {
    	
    	String tentheloai = categories.getTentheloai();
    	categories = categoryService.get(id);
    	if(tentheloai == "") {
    		redirectAttr.addFlashAttribute("message", "Thêm thể loại thất bại! Vui lòng điền đầy đủ thông tin");
    	}else {
    		if (categoryService.findByTentheloai(tentheloai) == null) {
        		model.addAttribute("categories", categories);
        	}
    	}
    	
    	
    	return "edit_categories";
    }
    
    @RequestMapping("/deletecategories/{id}")
    public String deleteProduct(@PathVariable(name = "id") int id) {
        categoryService.delete(id);
        return "redirect:/categories";       
    }
   
}
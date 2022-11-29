//Xử lý toàn bộ mapping liên quan đến người dùng
package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.project.model.Cards;
import com.project.model.ChangePass;
import com.project.model.Developer;
import com.project.model.Product;
import com.project.model.TopUp;
import com.project.model.UpdateProfile;
import com.project.model.User;
import com.project.service.CardsService;
import com.project.service.DeveloperService;
import com.project.service.UserService;
import com.project.util.EncrytedPasswordUtils;
import com.project.util.FileUploadUtil;
import com.project.util.WebUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

@Controller
public class UserController {
 
    @Autowired
    private UserService service;
    @Autowired
    private CardsService cardsService;
    @Autowired
    private DeveloperService developerService;
     
    // handler methods...
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView viewLoginPage(@RequestParam(value = "error", required = false) String error) {
        ModelAndView  modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        
        return modelAndView;
         
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(Model model,@ModelAttribute("User") User user) {
    
    	String registerName = user.getUsername();
    	
    	if (service.findByUsername(registerName) == null)
    	{
    		user.setRole("ROLE_USER");
        	user.setPassword(EncrytedPasswordUtils.encrytePassword(user.getPassword()));
            service.save(user);
    		model.addAttribute("message", "Tạo tài khoản mới thành công!");
    	} else {
    		model.addAttribute("message", "Tạo tài khoản thất bại! Tài khoản đã tồn tại trong hệ thống");
    	}
    	return "login";
       }
   
    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public String logoutSuccessfulPage(Model model) {
        return "logout";
    }
    
    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public String changePassword(Model model) {
    	model.addAttribute("changePass", new ChangePass());
        return "change-pass";
    }
    
    @RequestMapping(value = "/changePassHandler", method = RequestMethod.POST)
    public String changePasswordHandler(Model model,@ModelAttribute ChangePass changePass) {
    	    	
    	if (changePass != null)
    	{
    		User currentUser = service.findByUsername(changePass.getUsername());
    	  	if (currentUser != null )
    	  	{
        		String currentPassword = currentUser.getPassword();
    	  		if (EncrytedPasswordUtils.verifyPassword(changePass.getPassword(), currentPassword) == true)
    	  		{
    	  			currentUser.setPassword(EncrytedPasswordUtils.encrytePassword(changePass.getNewPassword()));
    	  			service.save(currentUser);
            		model.addAttribute("message", "Cập nhật mật khẩu mới thành công!");
    	  		} else {
            		model.addAttribute("message", "Có lỗi xảy ra ! Mật khẩu không đúng !");
    	  		}
    	  	} else {
        		model.addAttribute("message", "Có lỗi xảy ra ! Tài khoản không tồn tại trong hệ thống");
    	  	}
    	} else {
    		model.addAttribute("message", "Có lỗi xảy ra ! Vui lòng nhập đầy đủ thông tin");
    	}

        return "change-pass";
    }
    
    @RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
    public String forgotPassword(Model model) {
    	//model.addAttribute("changePass", new ChangePass());
        return "forgot-password";
    }
    
    @RequestMapping(value = "/accountProfile", method = RequestMethod.GET)
    public String viewProfile(Model model,Principal principal) {
    	 // Sau khi user login thanh cong se co principal
        String userName = principal.getName();
        User loginedUser = service.findByUsername(userName);
        
        List<Cards> listCards= cardsService.listAllCardsByUsername(userName);
        
        model.addAttribute("listCards", listCards); 
        model.addAttribute("User", loginedUser);
        model.addAttribute("updateProfile", new UpdateProfile());
        model.addAttribute("topupInfo", new TopUp());
        //User loginedUser = (User) ((Authentication) principal).getPrincipal();
        //String userInfo = WebUtils.toString(loginedUser);
        
        return "account-profile";
    }
    
    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public String updateProfile(Model model,@ModelAttribute UpdateProfile updateInfo, Principal principal) {
    	String userName = principal.getName();
        User loginedUser = service.findByUsername(userName);
        model.addAttribute("User", loginedUser);
        
    	if (updateInfo != null)
    	{
    		User currentUser = service.get(updateInfo.getId());
    		currentUser.setFullname(updateInfo.getFullname());
    		currentUser.setPhone(updateInfo.getPhone());
    		currentUser.setAddress(updateInfo.getAddress());
    		currentUser.setDateofbirth(updateInfo.getDateofbirth());
    		
    		service.save(currentUser);
    	} else {
    		model.addAttribute("message", "Có lỗi xảy ra ! Vui lòng nhập đầy đủ thông tin");
    	}
    	
        return "account-profile";
    }
    
    @RequestMapping(value = "/uploadAvatar", method = RequestMethod.POST)
    public String uploadAvatar(Model model, Principal principal,@ModelAttribute User user, @RequestParam("image") MultipartFile multipartFile) throws IOException {
    	String userName = principal.getName();
        User loginedUser = service.findByUsername(userName);

        if (multipartFile.isEmpty())
            return "redirect:/accountProfile";

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        loginedUser.setAvatar(fileName);
        service.save(loginedUser);
        
        String uploadDir = System.getProperty("user.dir") + "/uploads";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        
        
        return "redirect:/accountProfile";
    }
    
    @RequestMapping(value = "/topup", method = RequestMethod.POST)
    public String userTopUp(Model model,@ModelAttribute TopUp topupInfo, Principal principal) {
    	String userName = principal.getName();
        User loginedUser = service.findByUsername(userName);
        model.addAttribute("User", loginedUser);

    	if (topupInfo != null)
    	{
    		Cards cards = cardsService.findCardsByCodeAndSeri(topupInfo.getMathe(), topupInfo.getSoseri());
    		if (cards != null)
    		{
    			if (cards.getTrangthai().equals("Ready"))
    			{
    				//Update số dư người dùng
    				loginedUser.setBalance(loginedUser.getBalance() + cards.getMenhgia());
    				service.save(loginedUser);
    				
    				//Cập nhật lại trạng thái cho thẻ
    				String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    				cards.setNguoinap(userName);
    				cards.setTrangthai("Used");
    				cards.setNgaynap(date);
    				cardsService.save(cards);
    			} 
    			else 
    			{
    				System.out.println("Thẻ đã được sử dụng");
    			}
    		}
    		else {
    			System.out.println("Thẻ đã tồn tại");
    		}
    	} else {
    		model.addAttribute("message", "Có lỗi xảy ra ! Vui lòng nhập đầy đủ thông tin");
    	}
    	
        return "redirect:/accountProfile";
    }
    
    @RequestMapping(value = "/upgrade", method = RequestMethod.GET)
    public String userUpgrade(Model model, Principal principal) {
    	String userName = principal.getName();
        User loginedUser = service.findByUsername(userName);
        model.addAttribute("User", loginedUser);
        model.addAttribute("Developer", new Developer());
        
        return "upgrade";
    }
     
    @RequestMapping(value = "/uploadDevAvatar", method = RequestMethod.POST)
    public String uploadDevAvatar(Model model, Principal principal,@ModelAttribute User user, @RequestParam("image") MultipartFile multipartFile) throws IOException {
    	String userName = principal.getName();
        User loginedUser = service.findByUsername(userName);

        if (multipartFile.isEmpty())
            return "redirect:/upgrade";

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        loginedUser.setDevavatar(fileName);
        service.save(loginedUser);
        
        String uploadDir = System.getProperty("user.dir") + "/uploads";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        
        
        return "redirect:/upgrade";
    }
    
    @RequestMapping(value = "/upgradeDeveloper", method = RequestMethod.POST)
    public String upgradeDeveloper(Model model,@ModelAttribute Developer developer, Principal principal, @RequestParam("credentialFront") MultipartFile credentialfront, @RequestParam("credentialBack") MultipartFile credentialback) throws IOException {
    	String userName = principal.getName();
        User loginedUser = service.findByUsername(userName);
      
        if (credentialfront.isEmpty() || credentialback.isEmpty())
            return "redirect:/upgrade";

        String fileName1 = StringUtils.cleanPath(credentialfront.getOriginalFilename());
        String fileName2 = StringUtils.cleanPath(credentialback.getOriginalFilename());
        developer.setCredentialfront(fileName1);
        developer.setCredentialback(fileName2);
        developer.setUsername(userName);
        
        int currBalance = loginedUser.getBalance();
        if (currBalance >= 500000)
        {
        	String uploadDir = System.getProperty("user.dir") + "/uploads";
            FileUploadUtil.saveFile(uploadDir, fileName1, credentialfront);
            FileUploadUtil.saveFile(uploadDir, fileName2, credentialback);
        	developerService.save(developer);
        	
        	loginedUser.setBalance(currBalance - 500000);
        	loginedUser.setRole("ROLE_DEV");
        	service.save(loginedUser);
        } else {
            return "redirect:/upgrade";
        }

    	
        return "redirect:/accountProfile";
    }
    
    @RequestMapping(value = "/devProfile", method = RequestMethod.GET)
    public String viewDevProfile(Model model,Principal principal) {
    	 // Sau khi user login thanh cong se co principal
        String userName = principal.getName();
        User loginedUser = service.findByUsername(userName);
        model.addAttribute("User", loginedUser);
        
        Developer currDev = developerService.findByUsername(userName);
        model.addAttribute("Developer", currDev);
//        List<Cards> listCards= cardsService.listAllCardsByUsername(userName);
//        
//        model.addAttribute("listCards", listCards); 
//        model.addAttribute("updateProfile", new UpdateProfile());
//        model.addAttribute("topupInfo", new TopUp());
//        //User loginedUser = (User) ((Authentication) principal).getPrincipal();
//        //String userInfo = WebUtils.toString(loginedUser);
        
        return "dev-profile";
    }
    
    @RequestMapping(value = "/updateDeveloperProfile", method = RequestMethod.POST)
    public String upgradeDeveloper(Model model,@ModelAttribute Developer developer, Principal principal) throws IOException {
    	String userName = principal.getName();
        User loginedUser = service.findByUsername(userName);
        Developer currDev = developerService.findByUsername(userName);
        
        currDev.setDevname(developer.getDevname());
        currDev.setDevphone(developer.getDevphone());
        currDev.setDevaddress(developer.getDevaddress());
        currDev.setDevemail(developer.getDevemail());
        
        developerService.save(currDev);
            	
        return "redirect:/devProfile";
    }
    
    
}

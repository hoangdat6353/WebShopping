//Xử lý toàn bộ mapping liên quan đến Nhà phát triển
package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.model.App;
import com.project.model.Categories;
import com.project.model.Developer;
import com.project.model.PendingApp;
import com.project.model.User;
import com.project.service.AppService;
import com.project.service.CategoriesService;
import com.project.service.DeveloperService;
import com.project.service.PendingAppService;
import com.project.service.UserService;
import com.project.util.FileUploadUtil;

import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class DeveloperController {  
	 @Autowired
	 private UserService service;
	 @Autowired
	 private CategoriesService categoryService;
	 @Autowired
	 private DeveloperService developerService;
	 @Autowired
	 private PendingAppService pendingAppService;
	 @Autowired
	 private AppService appService;
	 
	@RequestMapping(value = "/index-dev", method = RequestMethod.GET)
	    public String viewIndexDev(Model model,Principal principal) {
	        String userName = principal.getName();
	        User loginedUser = service.findByUsername(userName);
	        Developer currDev = developerService.findByUsername(userName);
	        
	        List<App> listAppsSold = appService.listAllAppsByDeveloperAndStatus(currDev.getDevname(), "Publish");
	        int Revenue = 0;
	        for (int i = 0; i < listAppsSold.size(); i++)
	        {
	        	Revenue += listAppsSold.get(i).getPrice() * listAppsSold.get(i).getDownloads();
	        }
	        
	        model.addAttribute("listAppsSold", listAppsSold);
	        model.addAttribute("Developer", currDev);
	        model.addAttribute("User", loginedUser);      
	        model.addAttribute("Revenue", Revenue);
	        
	        return "index-dev";
	    }
	
	@RequestMapping(value = "/approval", method = RequestMethod.GET)
    public String viewUploadAppPage(Model model,Principal principal) {
        String userName = principal.getName();
        User loginedUser = service.findByUsername(userName);
        Developer currDev = developerService.findByUsername(userName);
        
        List<Categories> listCategories = categoryService.listAll();

        model.addAttribute("PendingApp", new PendingApp());
        model.addAttribute("Developer", currDev);
        model.addAttribute("User", loginedUser);
        model.addAttribute("listCategories", listCategories);
                
        return "approval";
    }
	
	@RequestMapping(value = "/dev-apps", method = RequestMethod.GET)
    public String viewDevApps(Model model,Principal principal) {
        String userName = principal.getName();
        User loginedUser = service.findByUsername(userName);
        Developer currDev = developerService.findByUsername(userName);  
        List<PendingApp> listWaitForAdminToApprove = pendingAppService.listAllPendingAppsByStatusAndDevName("Chờ duyệt", currDev.getDevname());
        List<PendingApp> listRejectedByAdmin = pendingAppService.listAllPendingAppsByStatusAndDevName("Từ chối", currDev.getDevname());
        
        List<App> listPublishApps = appService.listAllAppsByDeveloperAndStatus(currDev.getDevname(), "Publish");
        List<App> listPrivateApps = appService.listAllAppsByDeveloperAndStatus(currDev.getDevname(), "Private");

        model.addAttribute("listPrivateApps", listPrivateApps);
        model.addAttribute("listPublishApps", listPublishApps);

        model.addAttribute("listUnapprovedApps", listWaitForAdminToApprove);
        model.addAttribute("listRejectedApps", listRejectedByAdmin);
        model.addAttribute("Developer", currDev);
        model.addAttribute("User", loginedUser);      
        
        return "dev-apps";
    }
	
	@RequestMapping(value = "/uploadApp", method = RequestMethod.POST)
    public String upgradeDeveloper(Model model,@ModelAttribute PendingApp pendingApp, Principal principal,RedirectAttributes redirectAttr,
    		@RequestParam("installFile") MultipartFile installFile,
    		@RequestParam("iconImage") MultipartFile iconImage,
    		@RequestParam("screenShot1") MultipartFile screenshot1,
    		@RequestParam("screenShot2") MultipartFile screenshot2,
    		@RequestParam("screenShot3") MultipartFile screenshot3,
    		@RequestParam("screenShot4") MultipartFile screenshot4) throws IOException {
    	String userName = principal.getName();
        //User loginedUser = service.findByUsername(userName);
        Developer currDev = developerService.findByUsername(userName);

        if (installFile.isEmpty() || iconImage.isEmpty() || screenshot1.isEmpty() || screenshot2.isEmpty() || screenshot3.isEmpty() || screenshot4.isEmpty())
        {
        	redirectAttr.addFlashAttribute("message", "Vui lòng chọn đầy đủ các file !");
        	return "redirect:/approval";
        }
        long installFileSize = installFile.getSize();
		if (installFileSize > 5242880)
		{
        	redirectAttr.addFlashAttribute("message", "File cài đặt cần có dung lượng nhỏ hơn 5Mb!");
			return "redirect:/approval";
		}
		
        //Get file name
        String installFileName = StringUtils.cleanPath(installFile.getOriginalFilename());
        String iconImageFileName = StringUtils.cleanPath(iconImage.getOriginalFilename());
        String screenshot1FileName = StringUtils.cleanPath(screenshot1.getOriginalFilename());
        String screenshot2FileName = StringUtils.cleanPath(screenshot2.getOriginalFilename());
        String screenshot3FileName = StringUtils.cleanPath(screenshot3.getOriginalFilename());
        String screenshot4FileName = StringUtils.cleanPath(screenshot4.getOriginalFilename());

		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		
        //Setting up attribute
		String fileIconDir = System.getProperty("user.dir") + "/src/main/resources/static/img/featured";
		String fileInstallDir = System.getProperty("user.dir") + "/src/main/resources/static/img/downloads";
		String fileScreenshotDir = System.getProperty("user.dir") + "/src/main/resources/static/img/details";

		//Save files
        FileUploadUtil.saveFile(fileIconDir, iconImageFileName, iconImage);
        FileUploadUtil.saveFile(fileInstallDir, installFileName, installFile);
        FileUploadUtil.saveFile(fileScreenshotDir, screenshot1FileName, screenshot1);
        FileUploadUtil.saveFile(fileScreenshotDir, screenshot2FileName, screenshot2);
        FileUploadUtil.saveFile(fileScreenshotDir, screenshot3FileName, screenshot3);
        FileUploadUtil.saveFile(fileScreenshotDir, screenshot4FileName, screenshot4);
        
        //Save object
        pendingApp.setDeveloper(currDev.getDevname());
        pendingApp.setInstallfile(installFileName);
        pendingApp.setIconimage(iconImageFileName);
        pendingApp.setScreenshot1(screenshot1FileName);
        pendingApp.setScreenshot2(screenshot2FileName);
        pendingApp.setScreenshot3(screenshot3FileName);
        pendingApp.setScreenshot4(screenshot4FileName);
        pendingApp.setUploaddate(date);
        pendingApp.setStatus("Chờ duyệt");
        pendingApp.setFilesize(FileUploadUtil.getStringSizeLengthFile(installFileSize));
        
        pendingAppService.save(pendingApp);
    	redirectAttr.addFlashAttribute("message", "Ứng dụng đã được đăng lên để chờ duyệt thành công !");
    	
        return "redirect:/approval";
    }
	
	@RequestMapping(value = "/update-app-status", method = RequestMethod.POST)
    public String handleUpdateAppStatus(
    		@RequestParam("filter") String status,
    		@RequestParam("id") int id,
    		RedirectAttributes redirectAttr) {
        
		if (status.equals("delete"))
		{
			pendingAppService.delete(id);
	    	redirectAttr.addFlashAttribute("message", "Ứng dụng đã được xóa vĩnh viễn khỏi hệ thộng !");
		} else if (status.equals("deleteStore"))
		{
			appService.delete(id);
			pendingAppService.delete(id);
	    	redirectAttr.addFlashAttribute("message", "Ứng dụng đã được xóa vĩnh viễn khỏi hệ thộng !");
		}
		else if(status.equals("publish"))
		{
			App currApp = appService.get(id);
			currApp.setStatus("Publish");
			appService.save(currApp);
	    	redirectAttr.addFlashAttribute("message", "Ứng dụng đã được chuyển trạng thái thành công khai và đã xuất hiện trên trang chủ !");
		} else if (status.equals("private"))
		{
			App currApp = appService.get(id);
			currApp.setStatus("Private");
			appService.save(currApp);
	    	redirectAttr.addFlashAttribute("message", "Ứng dụng đã được chuyển trạng thái thành riêng tư và đã được ẩn khỏi trang chủ !");
		}
		                
        return "redirect:/dev-apps";
    }
     
}
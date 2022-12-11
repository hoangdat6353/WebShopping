//Xử lý toàn bộ mapping liên quan đến việc mua bán, tìm kiếm ứng dụng
//Demo các tác vụ liên quan đến product như thêm sửa xóa
package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.model.App;
import com.project.model.AppDownloaded;
import com.project.model.AppIndex;
import com.project.model.Categories;
import com.project.model.Product;
import com.project.model.User;
import com.project.service.AppDownloadedService;
import com.project.service.AppIndexService;
import com.project.service.AppService;
import com.project.service.CategoriesService;
import com.project.service.ProductService;
import com.project.service.UserService;

import java.awt.print.Pageable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@Controller
public class AppController {
 
    @Autowired
    private ProductService service;
    @Autowired
    private UserService userService;
    @Autowired
	private CategoriesService categoryService;
    @Autowired
    private AppIndexService appIndexService;
    @Autowired
    private AppService appService;
    @Autowired
    private AppDownloadedService appDownloadedService;
    
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String viewHomePage(Model model,Principal principal) {
    	if (principal == null)
    	{
			model.addAttribute("Role","Guest");
    	}else 
    	{
    		String userName = principal.getName();
            User loginedUser = userService.findByUsername(userName);
    		if (loginedUser.getRole().equals("ROLE_DEV"))
    		{
    			model.addAttribute("Role","Developer");
    			model.addAttribute("User", loginedUser);
    		} 
    		if (loginedUser.getRole().equals("ROLE_USER")){
                model.addAttribute("Role", "User");
    			model.addAttribute("User", loginedUser);
    		}
    		if (loginedUser.getRole().equals("ROLE_ADMIN"))
    			return "redirect:/index-admin";
    	}
   
        List<Categories> listCategories = categoryService.listAll();
        List<AppIndex> listAppsIndex = appIndexService.listAll();
        List<App> listApps = appService.listAllAppsByStatus("Publish");

        model.addAttribute("listCategories", listCategories);
        model.addAttribute("listAppsIndex", listAppsIndex);
        model.addAttribute("listApps", listApps);


        return "index";
    }
    
 
    @RequestMapping(value = "/shop-details/{appname}", method = RequestMethod.GET)
    public String showShopDetailsPage(@PathVariable(name = "appname") String appName, Model model, Principal principal) {
    	if (principal == null)
    	{
			model.addAttribute("Role","Guest");
    	}else 
    	{
    		String userName = principal.getName();
            User loginedUser = userService.findByUsername(userName);
            AppDownloaded appDownloaded = appDownloadedService.findAppByUserNameAndAppName(appName, appName);
            model.addAttribute("appDownloaded", appDownloaded);		
    		if (loginedUser.getRole().equals("ROLE_DEV"))
    		{
    			model.addAttribute("Role","Developer");
    			model.addAttribute("User", loginedUser);
    		} 
    		if (loginedUser.getRole().equals("ROLE_USER")){
                model.addAttribute("Role", "User");
    			model.addAttribute("User", loginedUser);
    		}
    		if (loginedUser.getRole().equals("ROLE_ADMIN"))
    			return "redirect:/index-admin";
    	}
   
        List<Categories> listCategories = categoryService.listAll();
        model.addAttribute("listCategories", listCategories);

        App displayedApp = appService.findAppByNameAndStatus(appName, "Publish");
        model.addAttribute("App", displayedApp);
        
        List<App> listAppsSameDeveloper = appService.listAllAppsByDeveloperAndStatus(displayedApp.getDeveloper(), "Publish");
        model.addAttribute("listAppsSameDeveloper",listAppsSameDeveloper);
        
        List<App> listAppsSameCategory = appService.listAllAppsByCategoryAndStatus(displayedApp.getCategory(), "Publish");
        model.addAttribute("listAppsSameCategory",listAppsSameCategory);
        
        return "shop-details";
    };
    
    @RequestMapping(value = "/download-app", method = RequestMethod.POST)
    public String downloadApp(Model model, Principal principal, @RequestParam("appName") String appName,
    		@RequestParam("price") int price,
    		HttpServletResponse response,
    		RedirectAttributes redirectAttr) {
    	App selectedApp = appService.findAppByNameAndStatus(appName, "Publish");
    	String installFileName = selectedApp.getInstallfile();
    	String filePath = System.getProperty("user.dir") + "/src/main/resources/static/img/downloads/";
		String userName = principal.getName();
        User loginedUser = userService.findByUsername(userName);

    	if (price == 0)
    	{
    		selectedApp.setDownloads(selectedApp.getDownloads() + 1);
    		appService.save(selectedApp);
    		
    		AppDownloaded appDownloaded = appDownloadedService.findAppByUserNameAndAppName(userName, appName);
    		if (appDownloaded == null)
    		{
    			AppDownloaded newAppDownloaded = new AppDownloaded();
    			newAppDownloaded.setAppname(selectedApp.getAppname());
        		newAppDownloaded.setCategory(selectedApp.getCategory());
        		newAppDownloaded.setIconimage(selectedApp.getIconimage());
        		newAppDownloaded.setPayment(selectedApp.getPayment());
        		newAppDownloaded.setUsername(userName);
        		appDownloadedService.save(newAppDownloaded);
    		} 
    		try {
                File fileToDownload = new File(filePath+ installFileName);
                InputStream inputStream = new FileInputStream(fileToDownload);
                response.setContentType("application/force-download");
                response.setHeader("Content-Disposition", "attachment; filename="+installFileName); 
                IOUtils.copy(inputStream, response.getOutputStream());
                response.flushBuffer();
                inputStream.close();
            } catch (Exception exception){
                System.out.println(exception.getMessage());
            } 
    	} else {
    		if (loginedUser.getBalance() >= price)
    		{
    			loginedUser.setBalance(loginedUser.getBalance() - selectedApp.getPrice());
    			userService.save(loginedUser);
    			selectedApp.setDownloads(selectedApp.getDownloads() + 1);
        		appService.save(selectedApp);
        		
        		AppDownloaded appDownloaded = appDownloadedService.findAppByUserNameAndAppName(userName, appName);
        		if (appDownloaded == null)
        		{
        			AppDownloaded newAppDownloaded = new AppDownloaded();
        			newAppDownloaded.setAppname(selectedApp.getAppname());
            		newAppDownloaded.setCategory(selectedApp.getCategory());
            		newAppDownloaded.setIconimage(selectedApp.getIconimage());
            		newAppDownloaded.setPayment(selectedApp.getPayment());
            		newAppDownloaded.setUsername(userName);
            		appDownloadedService.save(newAppDownloaded);
        		} 
        		
        		try {
                    File fileToDownload = new File(filePath+ installFileName);
                    InputStream inputStream = new FileInputStream(fileToDownload);
                    response.setContentType("application/force-download");
                    response.setHeader("Content-Disposition", "attachment; filename="+installFileName); 
                    IOUtils.copy(inputStream, response.getOutputStream());
                    response.flushBuffer();
                    inputStream.close();
                } catch (Exception exception){
                    System.out.println(exception.getMessage());
                } 
    		} else {
    	    	redirectAttr.addFlashAttribute("message", "Tài khoản của bạn không có đủ số dư để mua ứng dụng !");
    			return "redirect:/shop-details/" + appName;
    		}
    	}
    	
        return "redirect:/accountProfile";
    }
    
    @RequestMapping(value = "/shop-grid", method = RequestMethod.GET)
    public String viewRankingPage(Model model,Principal principal) {
    	if (principal == null)
    	{
			model.addAttribute("Role","Guest");
    	}else 
    	{
    		String userName = principal.getName();
            User loginedUser = userService.findByUsername(userName);
    		if (loginedUser.getRole().equals("ROLE_DEV"))
    		{
    			model.addAttribute("Role","Developer");
    			model.addAttribute("User", loginedUser);
    		} 
    		if (loginedUser.getRole().equals("ROLE_USER")){
                model.addAttribute("Role", "User");
    			model.addAttribute("User", loginedUser);
    		}
    		if (loginedUser.getRole().equals("ROLE_ADMIN"))
    			return "redirect:/index-admin";
    	}
   
        List<Categories> listCategories = categoryService.listAll();         
        model.addAttribute("listCategories", listCategories);
       
        List<App> listAppsFree = appService.listAllAppsByStatusAndPayment("Publish", "Free");
        System.out.println("List apps Free" + listAppsFree);
        model.addAttribute("listAppsFree",listAppsFree);
        List<App> listAppsPaid = appService.listAllAppsByStatusAndPayment("Publish", "Paid");
        model.addAttribute("listAppsPaid",listAppsPaid);
        
        return "shop-grid";
    }
    
    @RequestMapping(value = "/shop-type/{filter}", method = RequestMethod.GET)
    public String showShopTypePage(@PathVariable(name = "filter") String filter, Model model, Principal principal) {
    	if (principal == null)
    	{
			model.addAttribute("Role","Guest");
    	}else 
    	{
    		String userName = principal.getName();
            User loginedUser = userService.findByUsername(userName);
    		if (loginedUser.getRole().equals("ROLE_DEV"))
    		{
    			model.addAttribute("Role","Developer");
    			model.addAttribute("User", loginedUser);
    		} 
    		if (loginedUser.getRole().equals("ROLE_USER")){
                model.addAttribute("Role", "User");
    			model.addAttribute("User", loginedUser);
    		}
    		if (loginedUser.getRole().equals("ROLE_ADMIN"))
    			return "redirect:/index-admin";
    	}
    	List<App> listResult;
    	String filterName = "";
    	if (filter.equals("Free") || filter.equals("Paid"))
    	{
    		listResult = appService.listAllAppsByStatusAndPayment("Publish", filter);
    		if (filter.equals("Free"))
    			filterName = "Miễn phí";
    		else if (filter.equals("Paid"))
    			filterName = "Trả phí";
    		else 
    			filterName = filter;
    	} else {
    		listResult = appService.listAllAppsByCategoryAndStatus(filter, "Publish");
    		filterName = filter;
    	}
    	
    	
    	model.addAttribute("listResult", listResult);
    	model.addAttribute("filter", filterName);
        List<Categories> listCategories = categoryService.listAll();
        model.addAttribute("listCategories", listCategories);
          
        return "shop-type";
    };
    
    @RequestMapping(value = "/shop-search", method = RequestMethod.GET)
    public String showShopSearchPage(Model model, Principal principal) {
    	if (principal == null)
    	{
			model.addAttribute("Role","Guest");
    	}else 
    	{
    		String userName = principal.getName();
            User loginedUser = userService.findByUsername(userName);
    		if (loginedUser.getRole().equals("ROLE_DEV"))
    		{
    			model.addAttribute("Role","Developer");
    			model.addAttribute("User", loginedUser);
    		} 
    		if (loginedUser.getRole().equals("ROLE_USER")){
                model.addAttribute("Role", "User");
    			model.addAttribute("User", loginedUser);
    		}
    		if (loginedUser.getRole().equals("ROLE_ADMIN"))
    			return "redirect:/index-admin";
    	}
    	
        List<Categories> listCategories = categoryService.listAll();
        model.addAttribute("listCategories", listCategories);
          
        return "shop-search";
    };
    
    @RequestMapping(value = "/search-apps", method = RequestMethod.POST)
    public String handleSearch(Principal principal, Model model,@RequestParam("search") String search,RedirectAttributes redirectAttr) {
    	System.out.println("Search:" + search);
    	
    	List<App> listSearchResult = appService.findByAppnameContainingIgnoreCase(search);
    	if (listSearchResult  != null)
    		redirectAttr.addFlashAttribute("message", "ỨNG DỤNG TƯƠNG TỰ VỚI NỘI DUNG BẠN TÌM KIẾM");
    	else
    		redirectAttr.addFlashAttribute("message", "KHÔNG TÌM THẤY ỨNG DỤNG TƯƠNG TỰ NỘI DUNG BẠN TÌM KIẾM");

    	System.out.println("Search result:" + listSearchResult);
    	redirectAttr.addFlashAttribute("listSearchResult", listSearchResult);
    	
        return "redirect:/shop-search";
    }
    
    @RequestMapping("/new")
    public String showNewProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
         
        return "new_product";
    }
    
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveProduct(@ModelAttribute("product") Product product) {
        service.save(product);
         
        return "redirect:/";
    }
    
    @RequestMapping("/edit/{id}")
    public ModelAndView showEditProductPage(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("edit_product");
        Product product = service.get(id);
        mav.addObject("product", product);
         
        return mav;
    }
    
    @RequestMapping("/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") int id) {
        service.delete(id);
        return "redirect:/";       
    }
    
}

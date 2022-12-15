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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.project.model.AppDownloaded;
import com.project.model.Cards;
import com.project.model.ChangePass;
import com.project.model.Developer;
import com.project.model.EmailDetails;
import com.project.model.TopUp;
import com.project.model.UpdateProfile;
import com.project.model.User;
import com.project.service.AppDownloadedService;
import com.project.service.CardsService;
import com.project.service.DeveloperService;
import com.project.service.EmailService;
import com.project.service.UserService;
import com.project.util.EncrytedPasswordUtils;
import com.project.util.FileUploadUtil;

import net.bytebuddy.utility.RandomString;

import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class UserController {
 
    @Autowired
    private UserService service;
    @Autowired
    private CardsService cardsService;
    @Autowired
    private DeveloperService developerService;
    @Autowired
    private AppDownloadedService appDownloadedService;
    @Autowired 
    private EmailService emailService;

    
    // handler methods...
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String viewLoginPage(@RequestParam(value = "error", required = false) String error,Principal principal, Model model) {
    	if (principal != null)
    		return "redirect:/index";
        if (error != null)
        {
        	model.addAttribute("message", "Có lỗi xảy ra, đăng nhập thất bại ! Vui lòng kiểm tra lại tài khoản và mật khẩu");
        }
    	
        return "login";
         
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute("User") User user, RedirectAttributes redirectAttr) {
    	String registerName = user.getUsername();
    	
    	if (service.findByUsername(registerName) == null && service.findByEmail(user.getEmail()) == null ) //Nếu email và tên tài khoản chưa tồn tại trong hệ thống
    	{
    		//Kiểm tra mật khẩu có đủ mạnh (tối thiểu 8 ký tự, tối đa 12 ký tự, có chứa ít nhất 1 số, 1 ký tự hoa, 1 ký tự thường và 1 ký tự đặc biệt 
    		String password = user.getPassword();
    		int upChars=0, lowChars=0, special=0, digits = 0;
    		
    		for(int i=0; i< user.getPassword().length(); i++)
            {
               char ch = password.charAt(i);
               if(Character.isUpperCase(ch))
                  upChars = 1;
               else if(Character.isLowerCase(ch))
                  lowChars = 1;
               else if(Character.isDigit(ch))
                  digits = 1;
               else
                  special = 1;
            }
    	    if(upChars==1 && lowChars==1 && digits==1 && special==1) //Nếu thỏa toàn bộ điều kiện thì tạo tài khoản
    	    {
    	    	user.setRole("ROLE_USER");
            	user.setPassword(EncrytedPasswordUtils.encrytePassword(user.getPassword()));
                service.save(user);
        		redirectAttr.addFlashAttribute("message", "Tạo tài khoản mới thành công! ");
    	    } else //Nếu không thì thông báo cho người dùng mật khẩu chưa đủ mạnh
        		redirectAttr.addFlashAttribute("message", "Mật khẩu không đủ mạnh! Vui lòng tạo mật khẩu có tối thiểu 8 ký tự, có chứa ít nhất 1 số, 1 ký tự hoa, 1 ký tự thường và 1 ký tự đặc biệt ");

    	} else if (service.findByUsername(registerName) != null)  //Nếu tài khoản đã tồn tại trong hệ thống
    	{
    		redirectAttr.addFlashAttribute("message", "Tạo tài khoản thất bại ! Tên tài khoản đã tồn tại trong hệ thống");
    	} else //Nếu email đã tồn tại trong hệ thống
    	{
    		redirectAttr.addFlashAttribute("message", "Tạo tài khoản thất bại ! Địa chỉ email đã tồn tại trong hệ thống");
    	}
    	
    	return "redirect:/login";
    }
   
    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public String logoutSuccessfulPage(Model model) {
        return "logout";
    }
    
    //Hiển thị giao diện đổi mật khẩu
    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public String changePassword(Model model) {
    	model.addAttribute("changePass", new ChangePass());
        return "change-pass";
    }
    
    //Xử lý đổi mật khẩu
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
    
    
    //Hiển thị trang thông tin cá nhân của người dùng
    @RequestMapping(value = "/accountProfile", method = RequestMethod.GET)
    public String viewProfile(Model model,Principal principal) {
    	 // Sau khi user login thanh cong se co principal
    	//Lấy ra username của người dùng bằng Spring Security
        String userName = principal.getName();
        //Kiếm thông tin người dùng trong database
        User loginedUser = service.findByUsername(userName);
        
        //Hiển thị toàn bộ thẻ nạp mà người dùng đã nạp
        List<Cards> listCards= cardsService.listAllCardsByUsername(userName);
        
        //Hiển thị toàn bộ ứng dụng mà người dùng đã tải
        List<AppDownloaded> listFreeApps = appDownloadedService.findAppByUserNameAndPayment(userName, "Free");
        List<AppDownloaded> listPaidApps = appDownloadedService.findAppByUserNameAndPayment(userName, "Paid");

        model.addAttribute("listFreeApps", listFreeApps);
        model.addAttribute("listPaidApps", listPaidApps);
        model.addAttribute("listCards", listCards); 
        model.addAttribute("User", loginedUser);
        model.addAttribute("updateProfile", new UpdateProfile());
        model.addAttribute("topupInfo", new TopUp());
       
        return "account-profile";
    }
    
    //Xử lý người dùng cập nhật thông tin cá nhân
    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public String updateProfile(Model model,@ModelAttribute UpdateProfile updateInfo, Principal principal,RedirectAttributes redirectAttr) {
    	if (updateInfo != null)
    	{
    		//Lấy ra user nào đang thực hiện thay đổi thông tin
    		User currentUser = service.get(updateInfo.getId());
    		
    		//Update lại dữ liệu cho người dùng
    		currentUser.setFullname(updateInfo.getFullname());
    		currentUser.setPhone(updateInfo.getPhone());
    		currentUser.setAddress(updateInfo.getAddress());
    		currentUser.setDateofbirth(updateInfo.getDateofbirth());
    		
    		//Lưu vào database
    		service.save(currentUser);
    	} else {
    		redirectAttr.addFlashAttribute("messageProfile", "Có lỗi xảy ra ! Cập nhật thông tin cá nhân thất bại");
    	}
    	
        return "redirect:/accountProfile";
    }
    
    //Xử lý người dùng upload avatar cá nhân
    @RequestMapping(value = "/uploadAvatar", method = RequestMethod.POST)
    public String uploadAvatar(Model model, Principal principal,@ModelAttribute User user, @RequestParam("image") MultipartFile multipartFile) throws IOException {
    	 if (multipartFile.isEmpty())
             return "redirect:/accountProfile";
    	String userName = principal.getName();
        User loginedUser = service.findByUsername(userName);  

        //Lấy ra cái tên của file ảnh
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        
        //Cập nhật cột avatar = tên file hình
        loginedUser.setAvatar(fileName);    
        //Lưu vào database
        service.save(loginedUser);
        
        //Lấy ra thư mục sẽ chứa file
        String uploadDir = System.getProperty("user.dir") + "/uploads";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        
        return "redirect:/accountProfile";
    }
    
    //Xử lý người dùng nạp thẻ
    @RequestMapping(value = "/topup", method = RequestMethod.POST)
    public String userTopUp(Model model,@ModelAttribute TopUp topupInfo, Principal principal, RedirectAttributes redirectAttr) {	
    	String userName = principal.getName();
        User loginedUser = service.findByUsername(userName);

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
    				redirectAttr.addFlashAttribute("message", "Nạp thẻ thành công");
    			} 
    			else 
    			{
    				redirectAttr.addFlashAttribute("message", "Thẻ đã được sử dụng");
    			}
    		}
    		else {
				redirectAttr.addFlashAttribute("message", "Thẻ không tồn tại ! Vui lòng liên hệ Admin để được nhận thẻ cào mới");
    		}
    	} else {
			redirectAttr.addFlashAttribute("Có lỗi xảy ra !", "Vui lòng nhập đầy đủ thông tin thẻ");
    	}
    	
        return "redirect:/accountProfile";
    }
    
    //Hiển thị trang nâng cấp thành nhà phát triển
    @RequestMapping(value = "/upgrade", method = RequestMethod.GET)
    public String userUpgrade(Model model, Principal principal) {
    	//Hiển thị thông tin người dùng
    	String userName = principal.getName();
        User loginedUser = service.findByUsername(userName);
        model.addAttribute("User", loginedUser);
       
        model.addAttribute("Developer", new Developer());
        
        return "upgrade";
    }
     
    //Xử lý người dùng cập nhật logo nhà phát triển
    @RequestMapping(value = "/uploadDevAvatar", method = RequestMethod.POST)
    public String uploadDevAvatar(Model model, Principal principal,@ModelAttribute User user, @RequestParam("image") MultipartFile multipartFile) throws IOException {
    	String userName = principal.getName();
        User loginedUser = service.findByUsername(userName);

        if (multipartFile.isEmpty())
            return "redirect:/devProfile";

        //Lấy tên file hình, lưu vào database cho người dùng ở cột devavatar
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        loginedUser.setDevavatar(fileName);
        service.save(loginedUser);
        
        //Tên folder upload hình
        String uploadDir = System.getProperty("user.dir") + "/uploads";
        //Lưu hình của người dùng vào folder 
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        
        System.out.println("REACHED HERE");
        return "redirect:/devProfile";
    }
    
    @RequestMapping(value = "/upgradeDeveloper", method = RequestMethod.POST)
    public String upgradeDeveloper(Model model,@ModelAttribute Developer developer, Principal principal, @RequestParam("credentialFront") MultipartFile credentialfront, @RequestParam("credentialBack") MultipartFile credentialback) throws IOException {
    	String userName = principal.getName();
        User loginedUser = service.findByUsername(userName);
      
        if (credentialfront.isEmpty() || credentialback.isEmpty())
            return "redirect:/upgrade";

        //Lấy ra tên của 2 file hình
        String fileName1 = StringUtils.cleanPath(credentialfront.getOriginalFilename());
        String fileName2 = StringUtils.cleanPath(credentialback.getOriginalFilename());
       //Lưu tên file hình vào cột dữ liệu cho người dùng
        developer.setCredentialfront(fileName1);
        developer.setCredentialback(fileName2);
        developer.setUsername(userName);
        
        //Lấy ra số dư người dùng hiện tại
        int currBalance = loginedUser.getBalance();
        if (currBalance >= 500000)
        {
        	//Upload hình CMND nhà phát triển vào folder uploads
        	String uploadDir = System.getProperty("user.dir") + "/uploads";
            FileUploadUtil.saveFile(uploadDir, fileName1, credentialfront);
            FileUploadUtil.saveFile(uploadDir, fileName2, credentialback);
        	developerService.save(developer);
        	
        	//Cập nhật lại số dư và quyền người dùng
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
        String userName = principal.getName();
        User loginedUser = service.findByUsername(userName);
        model.addAttribute("User", loginedUser);
        
        Developer currDev = developerService.findByUsername(userName);
        model.addAttribute("Developer", currDev);
        
        return "dev-profile";
    }
    
    @RequestMapping(value = "/updateDeveloperProfile", method = RequestMethod.POST)
    public String upgradeDeveloper(Model model,@ModelAttribute Developer developer, Principal principal) throws IOException {
    	String userName = principal.getName();
        Developer currDev = developerService.findByUsername(userName);
        
        currDev.setDevname(developer.getDevname());
        currDev.setDevphone(developer.getDevphone());
        currDev.setDevaddress(developer.getDevaddress());
        currDev.setDevemail(developer.getDevemail());
        
        developerService.save(currDev);
            	
        return "redirect:/devProfile";
    }
    
    //Hiển thị trang quên mật khẩu
    @RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
    public String forgotPassword(Model model) {
        return "forgot-password";
    }
    
    //Xử lý thay đổi mật khẩu
    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    public String handleForgotPassword(Model model, Principal principal,@RequestParam("email") String email,RedirectAttributes redirectAttr) {
        //Tạo token dùng để xác thực yêu cầu quên mật khẩu của người dùng
    	String token = RandomString.make(30);
        
    	//Tạo mới đối tượng EmailDetails - để lưu thông tin email gửi đến người dùng
        EmailDetails emailDetails = new EmailDetails();
        try {
        	User currUser = service.findByEmail(email);
        	if (currUser == null)
        	{
        		redirectAttr.addFlashAttribute("error", "Không tìm thấy địa chỉ email người dùng trong hệ thống !");
        	}
        	else {   		
                String resetPasswordLink = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/reset-password/" + token;
                String content = 
                "Rất tiếc khi bạn gặp vấn đề về mật khẩu, chúng tôi có thể giúp bạn tạo một mật khẩu mới.\n"             
        		+ "Vui lòng bấm vào link dưới đây để tới trang cập nhật lại mật khẩu mới: \n"
        		 + resetPasswordLink
        		 + "\nNếu đây không phải là yêu cầu của bạn, vui lòng kiểm tra và đổi mật khẩu của tài khoản vì lí do bảo mật.";
                emailDetails.setRecipient(email);
                emailDetails.setSubject("Reset Mật Khẩu Hệ Thống App Shopping");
                emailDetails.setMsgBody(content);
                
                String status = emailService.sendSimpleMail(emailDetails);
                if (status.equals("Mail đã được gửi thành công ! Vui lòng kiểm tra Email để nhận đường link đổi mật khẩu"))
                {
                	//Lưu reset token vào bảng user cho người dùng
                	currUser.setResetPasswordToken(token);
            		service.save(currUser);
                }
        		redirectAttr.addFlashAttribute("message", status);
        	}             
        } catch (Exception ex) {
        	redirectAttr.addFlashAttribute("error", "Có lỗi xảy ra ! Quá trình gửi Mail thất bại vui lòng liên hệ Admin !");
        }         	
        
        return "redirect:/forgotPassword";
    }
    
    //Trang cập nhật lại mật khẩu mới
    @RequestMapping(value = "/reset-password/{token}", method = RequestMethod.GET)
    public String showResetPasswordPage(@PathVariable(name = "token") String token, Model model, Principal principal) {
    	User currUser = service.getByResetPasswordToken(token);
    	if (currUser == null)
    		model.addAttribute("message", "Yêu cầu không hợp lệ ! Vui lòng truy cập vào trang thay đổi mật khẩu để tạo mới yêu cầu.");
    	else 
    	{
    		model.addAttribute("Token", token);
    		model.addAttribute("message", "Thông tin đã được xác thực, vui lòng nhập mật khẩu mới để chúng tôi có thể cập nhật lại cho bạn ");
    	}
    	     
        return "reset-password";
    };
    
    //Xử lý cập nhật mật khẩu mới
    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public String handleResetPassword(@RequestParam("newPassword") String newPassword,
    		@RequestParam("Token") String token,
    		RedirectAttributes redirectAttr) {
    	User currUser = service.getByResetPasswordToken(token);
    	if (currUser == null)
    	{
    		redirectAttr.addFlashAttribute("message", "Không tìm thấy tài khoản người dùng trong hệ thống !");
    		return "redirect:/reset-password/" + token;
    	}
    	
    	service.updatePassword(currUser, newPassword);
		redirectAttr.addFlashAttribute("message", "Mật khẩu người dùng đã được cập nhật thành công ! Vui lòng đăng nhập bằng mật khẩu mới");

        return "redirect:/login";
    };
    
    
}

package com.onlinemarket.OnlinemarketProjectBackend.user.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.onlinemarket.OnlinemarketProjectBackend.FileUploadUtil;
import com.onlinemarket.OnlinemarketProjectBackend.security.OnlinemarketUserDetails;
import com.onlinemarket.OnlinemarketProjectBackend.user.UserService;
import com.onlinemarket.OnlinemarketProjectCommon.entity.User;

@Controller
public class AccountController {

    @Autowired
    private UserService service;

    @GetMapping("/account")
    public String viewDetails(@AuthenticationPrincipal OnlinemarketUserDetails loggedUser, Model model) {
        String email = loggedUser.getUsername();
        User user = service.getByEmail(email);
        
        model.addAttribute("user", user);
        return "users/account_form";
    } 

    @PostMapping("/account/update")
	public String saveDetails(User user, RedirectAttributes RedirectAttributes,
	@AuthenticationPrincipal OnlinemarketUserDetails loggedUser,
    @RequestParam("image") MultipartFile multipartFile) throws IOException {
		
		if(!multipartFile.isEmpty()){
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = service.updateAccount(user);
			String uploadDir = "user-photos/"+savedUser.getId();
			FileUploadUtil.deleteFiles(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else{
			if(user.getPhotos().isEmpty()) user.setPhotos(null);
			service.updateAccount(user);
		}
		
        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setLastName(user.getLastName());

		RedirectAttributes.addFlashAttribute("message", "Your account details have been updated successfully!");
		
		return "redirect:/account";
	}
}

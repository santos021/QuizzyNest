package in.quizzynest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.quizzynest.entity.UserDtls;
import in.quizzynest.service.UserService;

@Controller
public class HomeController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public String home() {
		return "index";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	@GetMapping("/signin")
	public String login() {
		return "login";
	}
	
	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute UserDtls user,RedirectAttributes rd) {
		
		UserDtls registerUser = userService.saveUser(user);
		if (registerUser != null) {
			rd.addFlashAttribute("succMsg", "User Registered Successfully");
		} else {
			rd.addFlashAttribute("errorMsg", "Something went wrong.....");
		}
		//System.out.println(user);
		
		return "redirect:/register";
	}
}

package in.quizzynest.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import in.quizzynest.entity.UserDtls;
import in.quizzynest.repository.CategoryRepository;
import in.quizzynest.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@ModelAttribute
	public void addUserToModel(Model model, Principal principal) {
		if (principal != null) {
			String email = principal.getName();
			UserDtls user = userRepository.findByEmail(email);
			model.addAttribute("user", user);
		}
	}

	@GetMapping("/")
	public String home(Model m) {
		m.addAttribute("categories",categoryRepository.findAll());
		return "user/dashboard";
	}

	@GetMapping("/results")
	public String userResults() {
		return "user/results";
	}

	@GetMapping("/quiz-attempt")
	public String userQuizAttempt() {
		return "user/attempt_quiz";
	}

	@GetMapping("/profile")
	public String userProfile(Model model, Principal principal) {
		String email = principal.getName();
		UserDtls user = userRepository.findByEmail(email);
		model.addAttribute("user", user);
		return "user/profile";
	}

}

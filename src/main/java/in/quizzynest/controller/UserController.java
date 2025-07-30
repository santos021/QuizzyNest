package in.quizzynest.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.quizzynest.entity.Category;
import in.quizzynest.entity.Question;
import in.quizzynest.entity.QuizResult;
import in.quizzynest.entity.UserDtls;
import in.quizzynest.repository.CategoryRepository;
import in.quizzynest.repository.QuizResultRepository;
import in.quizzynest.repository.UserRepository;
import in.quizzynest.service.ActivityService;
import in.quizzynest.service.CategoryService;
import in.quizzynest.service.QuestionService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private QuizResultRepository quizResultRepository;
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

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
		m.addAttribute("categories", categoryRepository.findAll());
		return "user/dashboard";
	}

	@GetMapping("/quiz-attempt/{categoryId}")
	public String userQuizAttemptByCategory(@PathVariable int categoryId, Model model,Principal principal) {
		List<Question> questions = questionService.getQuestionsByCategoryId(categoryId);

		List<Map<String, Object>> questionDTOs = new ArrayList<>();

		for (Question q : questions) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", q.getId());
			map.put("questionText", q.getQuestionText());
			map.put("options", List.of(q.getOptionA(), q.getOptionB(), q.getOptionC(), q.getOptionD()));
			questionDTOs.add(map);
		}

		Category category = categoryService.getCategoryById(categoryId);
		String categoryTitle = category.getTitle();
		
		String email = principal.getName();

		model.addAttribute("questions", questionDTOs);
		model.addAttribute("categoryTitle", categoryTitle);
		model.addAttribute("categoryId", categoryId);
		activityService.logActivity("User '" + email + "' started the '" + categoryTitle + "' quiz.");
		return "user/attempt_quiz";
	}

	@PostMapping("/submit-quiz")
	public String submitQuiz(@RequestParam Map<String, String> params, Model model, Principal principal) {
	    int score = 0;
	    int total = 0;
	    int categoryId = Integer.parseInt(params.get("categoryId"));

	    for (String key : params.keySet()) {
	        if (key.startsWith("answer_")) {
	            total++;
	            int qId = Integer.parseInt(key.substring(7));
	            String selected = params.get(key);
	            Question q = questionService.getQuestionById(qId).orElse(null);
	            if (q != null && selected.trim().equalsIgnoreCase(q.getCorrectAnswer().trim())) {
	                score++;
	            }
	        }
	    }

	    // Save result
	    UserDtls user = userRepository.findByEmail(principal.getName());
	    Category category = categoryService.getCategoryById(categoryId);

	    QuizResult result = new QuizResult();
	    result.setUser(user);
	    result.setCategory(category);
	    result.setScore(score);
	    result.setTotal(total);
	    result.setAttemptDate(LocalDateTime.now());
	    quizResultRepository.save(result);
	    
	    String username = principal.getName();
	    String quizTitle = category.getTitle();
	    activityService.logActivity("User '" + username + "' completed the '" + quizTitle + "' quiz with a score of " + score + "/" + total + ".");


	    model.addAttribute("score", score);
	    model.addAttribute("total", total);
	    return "user/quiz_result";
	}
	
	@GetMapping("/results")
	public String userResults(Model model, Principal principal) {
		 UserDtls user = userRepository.findByEmail(principal.getName());
		    List<QuizResult> results = quizResultRepository.findByUser(user);
		    model.addAttribute("results", results);
		return "user/my_results";
	}

	@GetMapping("/profile")
	public String userProfile(Model model, Principal principal) {
		String email = principal.getName();
		UserDtls user = userRepository.findByEmail(email);
		model.addAttribute("user", user);
		return "user/profile";
	}
	
	@PostMapping("/update-password")
	public String updateUserPassword(
	        @RequestParam("currentPassword") String currentPassword,
	        @RequestParam("newPassword") String newPassword,
	        @RequestParam("confirmPassword") String confirmPassword,
	        Principal principal,
	        RedirectAttributes redirect) {

	    String email = principal.getName();
	    UserDtls user = userRepository.findByEmail(email);

	    if (user != null && passwordEncoder.matches(currentPassword, user.getPassword())) {
	        if (newPassword.equals(confirmPassword)) {
	            user.setPassword(passwordEncoder.encode(newPassword));
	            userRepository.save(user);
	            activityService.logActivity("User '" + user.getEmail() + "' changed the password.");
	            redirect.addFlashAttribute("success", "Password updated successfully.");
	        } else {
	            redirect.addFlashAttribute("error", "New and confirm passwords do not match.");
	        }
	    } else {
	        redirect.addFlashAttribute("error", "Invalid current password.");
	    }

	    return "redirect:/user/profile";
	}

}

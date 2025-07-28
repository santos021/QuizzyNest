package in.quizzynest.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
import in.quizzynest.entity.UserDtls;
import in.quizzynest.repository.UserRepository;
import in.quizzynest.service.CategoryService;
import in.quizzynest.service.QuestionService;
import in.quizzynest.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

//	@GetMapping("/")
//	public String home() {
//		return "admin/base";
//	}

	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		model.addAttribute("categoryCount", categoryService.getAllCategory().size());
		model.addAttribute("questionCount", questionService.getAllQuestions().size());
		model.addAttribute("userCount", userService.getUsers("ROLE_USER").size());
		return "admin/base";
	}

	// Show category management page
	@GetMapping("/categories")
	public String showCategoryPage(Model model) {
		model.addAttribute("category", new Category());
		model.addAttribute("categories", categoryService.getAllCategory());
		model.addAttribute("isEditCategory", false);
		return "admin/category";
	}

	// Add new category
	@PostMapping("/categories/add")
	public String addCategory(@ModelAttribute("category") Category category) {
		categoryService.saveCategory(category);
		return "redirect:/admin/categories";
	}

	// Edit category
	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable("id") int id, Model model) {
		Category existingCategory = categoryService.getCategoryById(id);
		if (existingCategory == null) {
			return "redirect:/admin/categories";
		}
		model.addAttribute("category", existingCategory);
		model.addAttribute("categories", categoryService.getAllCategory());
		model.addAttribute("isEditCategory", true);
		return "admin/category";
	}

	// Update category
	@PostMapping("/categories/update")
	public String updateCategory(@ModelAttribute("category") Category category) {
		categoryService.saveCategory(category);
		return "redirect:/admin/categories";
	}

	// Delete category
	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable("id") int id) {
		categoryService.deleteCategory(id);
		return "redirect:/admin/categories";
	}

	// ------------------- Question Management-----------------
	@GetMapping("/questions")
	public String showQuestionsPage(
			@RequestParam(name = "categoryId", required = false, defaultValue = "0") int categoryId, Model model) {
		model.addAttribute("question", new Question());
		model.addAttribute("categories", categoryService.getAllCategory());

		if (categoryId == 0) {
			model.addAttribute("questions", questionService.getAllQuestions());
		} else {
			model.addAttribute("questions", questionService.getQuestionsByCategoryId(categoryId));
		}

		model.addAttribute("selectedCategoryId", categoryId); // Needed for "selected" in dropdown
		model.addAttribute("isEdit", false);
		return "admin/questions";
	}

	@PostMapping("/questions/add")
	public String addQuestion(@RequestParam int categoryId, @RequestParam String questionText,
			@RequestParam String optionA, @RequestParam String optionB, @RequestParam String optionC,
			@RequestParam String optionD, @RequestParam String correctAnswer) {
		Category category = categoryService.getCategoryById(categoryId);
		Question question = new Question();
		question.setQuestionText(questionText);
		question.setOptionA(optionA);
		question.setOptionB(optionB);
		question.setOptionC(optionC);
		question.setOptionD(optionD);
		question.setCorrectAnswer(correctAnswer);
		question.setCategory(category);
		questionService.saveQuestion(question);
		return "redirect:/admin/questions";
	}

	@GetMapping("/questions/edit/{id}")
	public String editQuestion(@PathVariable("id") int id, Model model) {
		Optional<Question> existingQuestion = questionService.getQuestionById(id);

		if (existingQuestion.isEmpty()) {
			return "redirect:/admin/questions";
		}

		model.addAttribute("question", existingQuestion.get()); // Pass actual Question
		model.addAttribute("questions", questionService.getAllQuestions());
		model.addAttribute("categories", categoryService.getAllCategory());
		model.addAttribute("isEdit", true);
		return "admin/questions";
	}

	@PostMapping("/questions/update")
	public String updateQuestion(@RequestParam int categoryId, @ModelAttribute("question") Question question) {
		Category category = categoryService.getCategoryById(categoryId);
		question.setCategory(category);
		questionService.saveQuestion(question);
		return "redirect:/admin/questions";
	}

	@GetMapping("/questions/delete/{id}")
	public String deleteQuestion(@PathVariable("id") int id) {
		questionService.deleteQuestion(id);
		return "redirect:/admin/questions";
	}

	@GetMapping("/users")
	public String allUsers(Model model, @RequestParam(defaultValue = "1") Integer type) {
		List<UserDtls> users = null;
		if (type == 1) {
			users = userService.getUsers("ROLE_USER");
		}
		model.addAttribute("userType", type);
		model.addAttribute("users", users);
		return "admin/users";
	}

	@GetMapping("/profile")
	public String adminProfile(Model m, Principal p) {
		String email = p.getName();
		UserDtls user = userRepository.findByEmail(email);
		m.addAttribute("admin", user);
		return "admin/profile";
	}

	@PostMapping("/update-profile")
	public String updateAdminProfile(@ModelAttribute("admin") UserDtls updatedAdmin, RedirectAttributes redirect) {
		UserDtls existingAdmin = userRepository.findById(updatedAdmin.getId()).orElse(null);

		if (existingAdmin != null) {
			existingAdmin.setName(updatedAdmin.getName());
			// Do not allow email/role updates from frontend for security
			userRepository.save(existingAdmin);
			redirect.addFlashAttribute("success", "Profile updated successfully.");
		} else {
			redirect.addFlashAttribute("error", "Admin not found.");
		}

		return "redirect:/admin/profile";
	}

	@PostMapping("/update-password")
	public String updateAdminPassword(@RequestParam("id") Integer id,
			@RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confirmPassword, RedirectAttributes redirect) {

		UserDtls user = userRepository.findById(id).orElse(null);

		if (user != null && passwordEncoder.matches(currentPassword, user.getPassword())) {
			if (newPassword.equals(confirmPassword)) {
				user.setPassword(passwordEncoder.encode(newPassword));
				userRepository.save(user);
				redirect.addFlashAttribute("success", "Password updated successfully.");
			} else {
				redirect.addFlashAttribute("error", "New and confirm passwords do not match.");
			}
		} else {
			redirect.addFlashAttribute("error", "Invalid current password.");
		}

		return "redirect:/admin/profile";
	}

}

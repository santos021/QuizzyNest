package in.quizzynest.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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


import in.quizzynest.entity.Activity;
import in.quizzynest.entity.Category;
import in.quizzynest.entity.Question;
import in.quizzynest.entity.UserDtls;
import in.quizzynest.repository.UserRepository;
import in.quizzynest.service.ActivityService;
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

	@Autowired
	private ActivityService activityService;


	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		List<Activity> activities = activityService.getRecentActivities();
		model.addAttribute("activities", activities);
		model.addAttribute("categoryCount", categoryService.getAllCategory().size());
		model.addAttribute("questionCount", questionService.getAllQuestions().size());
		model.addAttribute("userCount", userService.getUsers("ROLE_USER").size());
		return "admin/base";
	}

	// Show category management page
	@GetMapping("/categories")
	public String showCategoryPage(Model m,
	       @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
	       @RequestParam(name = "pageSize", defaultValue = "4") Integer pageSize,
	       @RequestParam(name = "id", required = false) Integer id) {

	    m.addAttribute("category", id != null ? categoryService.getCategoryById(id) : new Category());
	    m.addAttribute("isEditCategory", id != null);

	    Page<Category> page = categoryService.getAllCategoryPagination(pageNo, pageSize);
	    List<Category> categories = page.getContent();

	    m.addAttribute("categories", categories);
	    m.addAttribute("pageNo", page.getNumber());
	    m.addAttribute("pageSize", pageSize);
	    m.addAttribute("totalElements", page.getTotalElements());
	    m.addAttribute("totalPages", page.getTotalPages());
	    m.addAttribute("isFirst", page.isFirst());
	    m.addAttribute("isLast", page.isLast());

	    return "admin/category";
	}

	// Add new category
	@PostMapping("/categories/add")
	public String addCategory(@ModelAttribute("category") Category category) {
		categoryService.saveCategory(category);

		activityService.logActivity("New category '" + category.getTitle() + "' added.");
		return "redirect:/admin/categories";
	}

	// Edit category
	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable("id") int id, Model model) {
		Category existingCategory = categoryService.getCategoryById(id);
		String title = existingCategory.getTitle();

		if (existingCategory == null) {
			return "redirect:/admin/categories";
		}
		model.addAttribute("category", existingCategory);
		model.addAttribute("categories", categoryService.getAllCategory());
		model.addAttribute("isEditCategory", true);

		activityService.logActivity("'" + title + "' category edited.");

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
		Category category = categoryService.getCategoryById(id); // Fetch from DB
		String title = category.getTitle();

		categoryService.deleteCategory(id);

		activityService.logActivity("'" + title + "' category deleted.");
		return "redirect:/admin/categories";
	}

	// ------------------- Question Management-----------------
	@GetMapping("/questions")
	public String showQuestionsPage(
			@RequestParam(name = "categoryId", required = false, defaultValue = "0") int categoryId, Model model, @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
		model.addAttribute("question", new Question());
		model.addAttribute("categories", categoryService.getAllCategory());

		Page<Question> page;
	    if (categoryId == 0) {
	        page = questionService.getAllQuestionPagination(pageNo, pageSize);
	    } else {
	        page = questionService.getQuestionsByCategoryIdPagination(categoryId, pageNo, pageSize);
	    }

		model.addAttribute("selectedCategoryId", categoryId);
		model.addAttribute("question", new Question());
		model.addAttribute("isEdit", false);
		
		model.addAttribute("questions", page.getContent());
	    model.addAttribute("pageNo", page.getNumber());
	    model.addAttribute("pageSize", pageSize);
	    model.addAttribute("totalElements", page.getTotalElements());
	    model.addAttribute("totalPages", page.getTotalPages());
	    model.addAttribute("isFirst", page.isFirst());
	    model.addAttribute("isLast", page.isLast());
	    model.addAttribute("isEdit", false);
		
		
		return "admin/questions";
	}

	@PostMapping("/questions/add")
	public String addQuestion(@RequestParam int categoryId, @RequestParam String questionText,
			@RequestParam String optionA, @RequestParam String optionB, @RequestParam String optionC,
			@RequestParam String optionD, @RequestParam String correctAnswer) {
		Category category = categoryService.getCategoryById(categoryId);
		String title = category.getTitle();
		Question question = new Question();
		question.setQuestionText(questionText);
		question.setOptionA(optionA);
		question.setOptionB(optionB);
		question.setOptionC(optionC);
		question.setOptionD(optionD);
		question.setCorrectAnswer(correctAnswer);
		question.setCategory(category);
		questionService.saveQuestion(question);

		activityService.logActivity("New '" + title + "' question added.");
		return "redirect:/admin/questions";
	}

	@GetMapping("/questions/edit/{id}")
	public String editQuestion(@PathVariable("id") int id,
	                           @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
	                           @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize,
	                           @RequestParam(name = "categoryId", defaultValue = "0") int categoryId,
	                           Model model) {

	    Optional<Question> existingQuestion = questionService.getQuestionById(id);
	    if (existingQuestion.isEmpty()) {
	        return "redirect:/admin/questions";
	    }

	    Page<Question> page;
	    if (categoryId == 0) {
	        page = questionService.getAllQuestionPagination(pageNo, pageSize);
	    } else {
	        page = questionService.getQuestionsByCategoryIdPagination(categoryId, pageNo, pageSize);
	    }

	    model.addAttribute("question", existingQuestion.get()); // Pass the question to edit
	    model.addAttribute("categories", categoryService.getAllCategory());
	    model.addAttribute("questions", page.getContent());

	    // Add pagination data
	    model.addAttribute("pageNo", page.getNumber());
	    model.addAttribute("pageSize", pageSize);
	    model.addAttribute("totalElements", page.getTotalElements());
	    model.addAttribute("totalPages", page.getTotalPages());
	    model.addAttribute("isFirst", page.isFirst());
	    model.addAttribute("isLast", page.isLast());
	    model.addAttribute("selectedCategoryId", categoryId);
	    model.addAttribute("isEdit", true);

	    return "admin/questions";
	}

	@PostMapping("/questions/update")
	public String updateQuestion(@RequestParam int categoryId, @ModelAttribute("question") Question question) {
		Category category = categoryService.getCategoryById(categoryId);
		String title = category.getTitle();
		question.setCategory(category);
		questionService.saveQuestion(question);

		activityService.logActivity("'" + title + "' question updated.");
		return "redirect:/admin/questions";
	}

	@GetMapping("/questions/delete/{id}")
	public String deleteQuestion(@PathVariable("id") int id) {
		Optional<Question> optionalQuestion = questionService.getQuestionById(id);

		if (optionalQuestion.isPresent()) {
			Question question = optionalQuestion.get();
			String categoryTitle = question.getCategory().getTitle(); // Get category before deletion
			questionService.deleteQuestion(id); // Delete the question
			activityService.logActivity("Admin deleted a question from category '" + categoryTitle + "'.");
		}
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

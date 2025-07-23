package in.quizzynest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import in.quizzynest.entity.Category;
import in.quizzynest.service.CategoryService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
    private CategoryService categoryService;

//	@GetMapping("/")
//	public String home() {
//		return "admin/base";
//	}
	
	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		model.addAttribute("categoryCount", categoryService.getAllCategory().size());
        //model.addAttribute("questionCount", questionService.getAllQuestions().size());
        //model.addAttribute("userCount", userService.getAllUsers().size());
		return "admin/base";
	}

	
	 // Show category management page
    @GetMapping("/categories")
    public String showCategoryPage(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("categories", categoryService.getAllCategory());
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
    @GetMapping("/questions")
	public String questions() {
		return "admin/questions";
	}
    @GetMapping("/users")
	public String allUsers() {
		return "admin/users";
	}
    
    @GetMapping("/profile")
	public String adminProfile() {
		return "admin/profile";
	}

}

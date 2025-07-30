package in.quizzynest.service;

import java.util.List;

import org.springframework.data.domain.Page;

import in.quizzynest.entity.Category;

public interface CategoryService {

	public Category saveCategory(Category category);

	//public Boolean existCategory(String name);

	public List<Category> getAllCategory();

	public Boolean deleteCategory(int id);

	public Category getCategoryById(int id);
	
	public Page<Category> getAllCategoryPagination(Integer pageNo,Integer pageSize);
}

package in.quizzynest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import in.quizzynest.entity.Category;
import in.quizzynest.entity.Question;

public interface QuestionService {
	
	public Question saveQuestion(Question question);
	
	public List<Question> getQuestionsByCategoryId(Integer categoryId);
	
	public Optional<Question> getQuestionById(int id);
	
	public boolean deleteQuestion(int id);
	
	public List<Question> getAllQuestions();
	
	public Page<Question> getAllQuestionPagination(Integer pageNo, Integer pageSize);
	public Page<Question> getQuestionsByCategoryIdPagination(int categoryId, int pageNo, int pageSize);
}

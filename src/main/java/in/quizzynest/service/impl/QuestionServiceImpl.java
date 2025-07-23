package in.quizzynest.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.quizzynest.entity.Category;
import in.quizzynest.entity.Question;
import in.quizzynest.repository.QuestionRepository;
import in.quizzynest.service.QuestionService;

@Service
public class QuestionServiceImpl implements QuestionService {
	
	@Autowired
	private QuestionRepository questionRepository;

	@Override
	public Question saveQuestion(Question question) {
		Question saveQuestions = questionRepository.save(question);
		return saveQuestions;
	}

	@Override
	public List<Question> getQuestionsByCategoryId(Integer categoryId) {
		
		return questionRepository.findByCategoryId(categoryId);
	}

	@Override
	public Optional<Question> getQuestionById(int id) {
		
		return questionRepository.findById(id);
	}

	@Override
	public boolean deleteQuestion(int id) {
		Question deleteQuestion = questionRepository.findById(id).orElse(null);
		if (deleteQuestion != null) {
			questionRepository.delete(deleteQuestion);
			return true;
		}
		return false;
	}

	@Override
	public List<Question> getAllQuestions() {
		return questionRepository.findAll();
		
	}

}

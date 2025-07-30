package in.quizzynest.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import in.quizzynest.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
	List<Question> findByCategoryId(int categoryId);
	Page<Question> findByCategoryId(int categoryId, Pageable pageable);
}

package in.quizzynest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.quizzynest.entity.QuizResult;
import in.quizzynest.entity.UserDtls;

public interface QuizResultRepository extends JpaRepository<QuizResult, Integer> {
	List<QuizResult> findByUser(UserDtls user);
}

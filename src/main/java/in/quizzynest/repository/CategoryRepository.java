package in.quizzynest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.quizzynest.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}

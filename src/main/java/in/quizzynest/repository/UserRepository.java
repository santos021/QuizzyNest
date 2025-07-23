package in.quizzynest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.quizzynest.entity.UserDtls;

public interface UserRepository extends JpaRepository<UserDtls, Integer> {
	public UserDtls findByEmail(String email);
}

package in.quizzynest.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class QuizResult {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	private UserDtls user;
	
	@ManyToOne
	private Category category;
	
	private int score;
	
	private int total;
	
	private LocalDateTime attemptDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserDtls getUser() {
		return user;
	}

	public void setUser(UserDtls user) {
		this.user = user;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public LocalDateTime getAttemptDate() {
		return attemptDate;
	}

	public void setAttemptDate(LocalDateTime attemptDate) {
		this.attemptDate = attemptDate;
	}

	@Override
	public String toString() {
		return "QuizResult [id=" + id + ", user=" + user + ", category=" + category + ", score=" + score + ", total="
				+ total + ", attemptDate=" + attemptDate + "]";
	}
	
	
}

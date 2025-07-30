package in.quizzynest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.quizzynest.entity.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findTop3ByOrderByTimestampDesc();
}
package in.quizzynest.service;

import java.util.List;

import in.quizzynest.entity.Activity;

public interface ActivityService {
	void logActivity(String description);
    List<Activity> getRecentActivities();
}

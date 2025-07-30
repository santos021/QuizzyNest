package in.quizzynest.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.quizzynest.entity.Activity;
import in.quizzynest.repository.ActivityRepository;
import in.quizzynest.service.ActivityService;

@Service
public class ActivityServiceImpl implements ActivityService {
	
	@Autowired
	private ActivityRepository activityRepository;

	@Override
	public void logActivity(String description) {
		Activity activity = new Activity(description,LocalDateTime.now());
		activityRepository.save(activity);
		
	}

	@Override
	public List<Activity> getRecentActivities() {
		
		return activityRepository.findTop3ByOrderByTimestampDesc();
	}
	
}

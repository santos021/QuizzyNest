package in.quizzynest.service;

import java.util.List;

import in.quizzynest.entity.UserDtls;

public interface UserService {
	
	public UserDtls saveUser(UserDtls user);
	
	public List<UserDtls> getUsers(String role);
//	public UserDtls saveUser(UserDtls user, String url);

//	public void removeSessionMessage();
//
//	public void sendEmail(UserDtls user, String path);
//
//	public boolean verifyAccount(String verificationCode);
//
//	public void increaseFailedAttempt(UserDtls user);
//
//	public void resetAttempt(String email);
//
//	public void lock(UserDtls user);
//
//	public boolean unlockAccountTimeExpired(UserDtls user);
	
}

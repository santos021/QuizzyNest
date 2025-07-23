package in.quizzynest.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.quizzynest.entity.UserDtls;
import in.quizzynest.repository.UserRepository;
import in.quizzynest.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDtls saveUser(UserDtls user) {
		
		user.setRole("ROLE_USER");
		String encodePassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);
		
		user.setRegDate(LocalDateTime.now());
		
		UserDtls saveUser = userRepository.save(user);
		return saveUser;
	}

	@Override
	public List<UserDtls> getUsers(String role) {
		
		return userRepository.findByRole(role);
	}

}

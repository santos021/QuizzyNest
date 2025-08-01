package in.quizzynest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.quizzynest.entity.UserDtls;
import in.quizzynest.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserDtls user = userRepository.findByEmail(username);
		
		if (user == null) {
			throw new UsernameNotFoundException("user not found");
		}
		
		return new CustomUser(user);
	}

}

package in.quizzynest.config;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import in.quizzynest.service.ActivityService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthSuccessHandlerImpl implements AuthenticationSuccessHandler {
	
	@Autowired
	private ActivityService activityService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		String email = authentication.getName(); // get the username (usually email)
        activityService.logActivity("User '" + email + "' logged in."); // log the login activity
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		
		Set<String> roles = AuthorityUtils.authorityListToSet(authorities);
		
		if (roles.contains("ROLE_ADMIN")) {
			response.sendRedirect("/admin/dashboard");
		} else {
			response.sendRedirect("/user/");
		}
	}

}

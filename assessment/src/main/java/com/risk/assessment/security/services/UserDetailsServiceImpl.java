package com.risk.assessment.security.services;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.risk.assessment.models.User;
import com.risk.assessment.repository.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

		return convert(user);
	}
	
	 private UserDetails convert(User user) {
	    	UserDetailsImpl userDetails = new UserDetailsImpl();

	        if (user != null) {
	            userDetails.setUsername(user.getUsername());
	            userDetails.setPassword(user.getPassword());
	            userDetails.setEnabled(true);
	            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
	            if(user.getRole() != null) {
	            	String roleName = user.getRole().getName();
	            	authorities.add(new SimpleGrantedAuthority(roleName));
	            }
	            userDetails.setAuthorities(authorities);
	            OffsetDateTime date = user.getLastPasswordReset();
	            if(date != null) {
	            	userDetails.setLastPasswordReset(new Date(date.toInstant().toEpochMilli()));
	            }
	        }

	        return userDetails;
	    }

}

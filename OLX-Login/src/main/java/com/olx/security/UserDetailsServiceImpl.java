package com.olx.security;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.olx.entity.UserEntity;
import com.olx.repo.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<UserEntity> userEntityList = userRepository.findByUserName(username);
		if(userEntityList==null || userEntityList.size()==0)
			throw new UsernameNotFoundException("Not found " + username);
		//return new UserDetailsImpl(opUser.get());
		UserEntity userEntity = userEntityList.get(0);
		String roles = userEntity.getRoles();
		List<SimpleGrantedAuthority> authorities = Arrays.stream(roles.split(",")).map((role)->new SimpleGrantedAuthority(role)).collect(Collectors.toList());
		/*
		return new User(userEntity.getUserName(), 
				PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(userEntity.getPassword()),
				userEntity.isActive(), true, true, true, authorities);
		*/
		return new User(userEntity.getUserName(), userEntity.getPassword(), authorities);
	}

}

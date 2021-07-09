package com.olx.service;

import java.util.List;

import com.olx.dto.RegisterRequest;
import com.olx.dto.UserDTO;

public interface UserService {

	public UserDTO registerUser(RegisterRequest registerRequest);
	List<UserDTO> findByUsernames(String usernames);
	UserDTO findByUsername(String username);
}

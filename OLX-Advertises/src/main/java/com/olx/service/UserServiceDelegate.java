package com.olx.service;

import java.util.List;
import java.util.Map;

public interface UserServiceDelegate {
	public List<Map> findByUsernames(String usernames);
	public Map findByUsername(String username);
	public boolean isLoggedInUser(String authToken);
}

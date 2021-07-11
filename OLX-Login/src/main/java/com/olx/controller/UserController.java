package com.olx.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.olx.dto.AdvertiseDTO;
import com.olx.dto.AuthenticationRequest;
import com.olx.dto.RegisterRequest;
import com.olx.dto.UserDTO;
import com.olx.service.AdvertiseServiceDelegate;
import com.olx.service.UserService;
import com.olx.utils.JwtUtils;

@RestController
//@CrossOrigin("*")
public class UserController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	private AdvertiseServiceDelegate advertiseServiceDelegate;
	
	@GetMapping(value="/token/validate")
	public ResponseEntity<Boolean> isTokenValid(@RequestHeader("Authorization") String jwtToken) {
		
		//1) Extract the jwtToken by removing the word "Bearer " from jwtToken string. Use substring()
		jwtToken = jwtToken.substring(7, jwtToken.length());
		
		//2) Extract username from the jwtToken using jwtUtils.extractUsername(jwtToken)
		String username = jwtUtils.extractUsername(jwtToken);
		
		//3) Extract UserDetails using UserDetailsService.loadUserByUsername(username)
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		
		//4) Validate the token using jwtUtils.validateToken(jwtToken, userDetails);
		boolean isTokenValid = jwtUtils.validateToken(jwtToken, userDetails);
		if(isTokenValid)
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
	}
	
	
	@PostMapping(value="/authenticate", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDTO> generateToken(@RequestBody AuthenticationRequest authRequest) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		}
		catch(BadCredentialsException e) {
			return new ResponseEntity<UserDTO>(HttpStatus.BAD_REQUEST);
		}
		UserDTO userDto = userService.findByUsername(authRequest.getUsername());
		userDto.setJwtToken(jwtUtils.generateToken(authRequest.getUsername()));
		return new ResponseEntity<UserDTO>(userDto, HttpStatus.OK);
	}
	
	@PostMapping(value="/register", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDTO> register(@RequestBody RegisterRequest registerRequest) throws Exception {
		UserDTO userDto = userService.registerUser(registerRequest);
		userDto.setJwtToken(jwtUtils.generateToken(userDto.getUsername()));
		return new ResponseEntity<UserDTO>(userDto, HttpStatus.OK);
	}
	
	@GetMapping(value="/user/list/{ids}", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<UserDTO> findByUsernames(@PathVariable("ids") String usernames) {
		return userService.findByUsernames(usernames);
	}
	
	@GetMapping(value="/user/{username}", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDTO> findByUsername(@PathVariable("username") String username) {
		return new ResponseEntity<UserDTO>(userService.findByUsername(username), HttpStatus.OK);
	}
	
	@GetMapping(value="/user/advertise", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<AdvertiseDTO> getAllAdvertises() {
		return advertiseServiceDelegate.getAllAdvertises();
	}
	
	@GetMapping(value="/user/advertise/{username}", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<AdvertiseDTO> getAdvertisesById(@PathVariable("username") String username) {
		return advertiseServiceDelegate.getAllAdvertisesByUsername(username);
	}

}

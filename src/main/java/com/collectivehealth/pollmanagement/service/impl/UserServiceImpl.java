package com.collectivehealth.pollmanagement.service.impl;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collectivehealth.pollmanagement.dto.UserDTO;
import com.collectivehealth.pollmanagement.form.UserForm;
import com.collectivehealth.pollmanagement.message.MessageConstants;
import com.collectivehealth.pollmanagement.message.MessageManager;
import com.collectivehealth.pollmanagement.model.User;
import com.collectivehealth.pollmanagement.repository.UserRepository;
import com.collectivehealth.pollmanagement.service.UserService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10, new SecureRandom());
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public List<UserDTO> findAll() {
	    List<User> result = userRepository.findAll();
	    return result.stream().map(UserDTO::convert).collect(Collectors.toList());
	}
	
	public Optional<UserDTO> findById(Integer id) {
        log.info("Getting user by ID {}", id);
        Optional<User> opt = userRepository.findById(id);
	    if (opt.isPresent()) {
	        return Optional.of(UserDTO.convert(opt.get()));
	    }
	    return Optional.ofNullable(null);
	}
	
	public UserDTO authenticate(@Valid UserForm user) {
	    
		log.info("Verifying password for user {}", user.getUsername());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Optional<User> opt = userRepository.findByUsernamePassword(user.getUsername(), user.getPassword());
		if (opt.isPresent()) {
	        log.info("Credentials are valid {}", user.getUsername());
		    return UserDTO.convert(opt.get());
		}
        log.info("Invalid credentials", user.getUsername());
		return UserDTO.convert(HttpStatus.NOT_FOUND.value(), 
		        MessageManager.getMessage(MessageConstants.INVALID_CREDENTIALS));
	}
    
    @Transactional
    public UserDTO create(@Valid UserForm user) {

        log.info("Check if username exists");
        Optional<User> opt = userRepository.findByUsername(user.getUsername());
        if (opt.isPresent()) {
            log.info("Username already exists: {}", user.getUsername());
            return UserDTO.convert(HttpStatus.BAD_REQUEST.value(), 
                    MessageManager.getMessage(MessageConstants.USERNAME_ALREADY_EXISTS, user.getUsername()));
        }

        log.info("Adding user {}", user.getUsername());
        User entity = User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .build();

        User res = userRepository.save(entity);
        return UserDTO.convert(res);
    }
}

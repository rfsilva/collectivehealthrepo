package com.collectivehealth.pollmanagement.service;

import java.util.List;
import java.util.Optional;

import com.collectivehealth.pollmanagement.dto.UserDTO;
import com.collectivehealth.pollmanagement.form.UserForm;

public interface UserService {

    public List<UserDTO> findAll();
    public Optional<UserDTO> findById(Integer id);
    public UserDTO create(UserForm user);
    public UserDTO authenticate(UserForm user);
}

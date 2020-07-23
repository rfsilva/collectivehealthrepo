package com.collectivehealth.pollmanagement.api;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.collectivehealth.pollmanagement.dto.ErrorDTO;
import com.collectivehealth.pollmanagement.dto.UserDTO;
import com.collectivehealth.pollmanagement.form.UserForm;
import com.collectivehealth.pollmanagement.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("user")
@Api("user")
@Log4j2
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @ApiOperation(value = "Get User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User found.", response = UserDTO.class),
            @ApiResponse(code = 400, message = "User not found.", response = ErrorDTO.class),
            @ApiResponse(code = 500, message = "Internal failure.", response = ErrorDTO.class) })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getOne(@PathVariable Integer id) {
        log.info("Request specific user {}", id);
        Optional<UserDTO> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @ApiOperation(value = "List Users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User list.", response = UserDTO.class),
            @ApiResponse(code = 500, message = "Internal failure.", response = ErrorDTO.class) })
    @GetMapping
    public List<UserDTO> getList() {
        log.info("Request list of users");
        return userService.findAll();
    }

    @ApiOperation(value = "Create User")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created.", response = UserDTO.class),
            @ApiResponse(code = 400, message = "Username already exists.", response = ErrorDTO.class),
            @ApiResponse(code = 500, message = "Internal failure.", response = ErrorDTO.class) })
    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody @Valid UserForm userForm, UriComponentsBuilder uriBuilder) {
        log.info("Request create user: {}", userForm);
        UserDTO userDTO = userService.create(userForm);
        if (userDTO.hasErrors()) {
            return ResponseEntity.badRequest().body(userDTO);
        }
        
        URI uri = uriBuilder.path("/user/{id}").buildAndExpand(userDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(userDTO);
    }

    @ApiOperation(value = "Authenticate User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User authenticated.", response = UserDTO.class),
            @ApiResponse(code = 400, message = "Invalid username/password.", response = ErrorDTO.class),
            @ApiResponse(code = 500, message = "Internal failure.", response = ErrorDTO.class) })
    @PostMapping("auth")
    public ResponseEntity<UserDTO> authenticate(@RequestBody @Valid UserForm userForm) {
        log.info("Request authenticate user: {}", userForm);
        return ResponseEntity.ok(userService.authenticate(userForm));
    }
}
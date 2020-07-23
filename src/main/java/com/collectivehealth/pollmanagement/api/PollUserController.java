package com.collectivehealth.pollmanagement.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.collectivehealth.pollmanagement.dto.ErrorDTO;
import com.collectivehealth.pollmanagement.dto.PollDTO;
import com.collectivehealth.pollmanagement.dto.PollUserDTO;
import com.collectivehealth.pollmanagement.form.PollUserForm;
import com.collectivehealth.pollmanagement.service.PollUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("poll")
@Api("poll")
@Log4j2
public class PollUserController {
    
    @Autowired
    private PollUserService pollUserService;

    @ApiOperation(value = "Associate Poll to the User")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Association OK.", response = PollDTO.class),
            @ApiResponse(code = 404, message = "Association failure.", response = ErrorDTO.class),
            @ApiResponse(code = 500, message = "Internal failure.", response = ErrorDTO.class) })
    @PostMapping("association")
    public ResponseEntity<PollUserDTO> update(@RequestBody @Valid PollUserForm pollUserForm, UriComponentsBuilder uriBuilder) {
        log.info("Request associate poll {} to the user {}", pollUserForm);
        PollUserDTO pollUserDTO = pollUserService.associate(pollUserForm);
        if (pollUserDTO.hasErrors()) {
            return ResponseEntity.badRequest().body(pollUserDTO);
        }
        return ResponseEntity.ok(pollUserDTO);
    }

    @ApiOperation(value = "Deassociate Poll and User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Association OK.", response = PollDTO.class),
            @ApiResponse(code = 404, message = "Association failure.", response = ErrorDTO.class),
            @ApiResponse(code = 500, message = "Internal failure.", response = ErrorDTO.class) })    
    @DeleteMapping("association/{id}")
    public ResponseEntity<?> deassociate(@PathVariable Integer id) {
        log.info("Request deassociate poll and user", id);
        PollUserDTO pollUserDTO = pollUserService.deassociate(id);
        if (pollUserDTO.hasErrors()) {
            return ResponseEntity.badRequest().body(pollUserDTO);
        }
        return ResponseEntity.ok(pollUserDTO);
    }

    @ApiOperation(value = "Get list of polls associated to user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Association OK.", response = PollDTO.class),
            @ApiResponse(code = 404, message = "Association failure.", response = ErrorDTO.class),
            @ApiResponse(code = 500, message = "Internal failure.", response = ErrorDTO.class) })    
    @GetMapping("association/{userId}")
    public ResponseEntity<List<PollDTO>> getPolls(@PathVariable Integer userId) {
        log.info("Request polls associated to user", userId);
        List<PollDTO> pollDTOList = pollUserService.listPolls(userId);
        return ResponseEntity.ok(pollDTOList);
    }
}
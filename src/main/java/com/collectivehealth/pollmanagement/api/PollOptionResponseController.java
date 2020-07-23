package com.collectivehealth.pollmanagement.api;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.collectivehealth.pollmanagement.dto.ErrorDTO;
import com.collectivehealth.pollmanagement.dto.PollDTO;
import com.collectivehealth.pollmanagement.dto.PollOptionDTO;
import com.collectivehealth.pollmanagement.form.PollOptionResponseChangeForm;
import com.collectivehealth.pollmanagement.form.PollOptionResponseForm;
import com.collectivehealth.pollmanagement.service.PollOptionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("poll-option/response")
@Api("poll-option/response")
@Log4j2
public class PollOptionResponseController {
    
    @Autowired
    private PollOptionService pollOptionService;

    @ApiOperation(value = "Register response")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Response registered.", response = PollDTO.class),
            @ApiResponse(code = 400, message = "Invalid response.", response = ErrorDTO.class),
            @ApiResponse(code = 500, message = "Internal failure.", response = ErrorDTO.class) })
    @PostMapping
    public ResponseEntity<PollOptionDTO> registerVote(@RequestBody @Valid PollOptionResponseForm pollOptionForm, UriComponentsBuilder uriBuilder) {
        log.info("Register response to option {} of poll {}", pollOptionForm);
        PollOptionDTO pollOptionDTO = pollOptionService.registerVote(pollOptionForm);
        if (pollOptionDTO.hasErrors()) {
            return ResponseEntity.badRequest().body(pollOptionDTO);
        }
        
        URI uri = uriBuilder.path("/poll-option/{id}").buildAndExpand(pollOptionDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(pollOptionDTO);
    }

    @ApiOperation(value = "Change response")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Poll updated.", response = PollDTO.class),
            @ApiResponse(code = 404, message = "Poll not found.", response = ErrorDTO.class),
            @ApiResponse(code = 500, message = "Internal failure.", response = ErrorDTO.class) })
    @PutMapping
    public ResponseEntity<PollOptionDTO> changeVote(@RequestBody @Valid PollOptionResponseChangeForm pollOptionForm) {
        log.info("Request response change to poll option: {}", pollOptionForm);
        PollOptionDTO pollOptionDTO = pollOptionService.changeVote(pollOptionForm);
        if (pollOptionDTO.hasErrors()) {
            return ResponseEntity.badRequest().body(pollOptionDTO);
        }
        return ResponseEntity.ok(pollOptionDTO);
    }
}
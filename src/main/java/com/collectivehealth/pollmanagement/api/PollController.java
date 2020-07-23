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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.collectivehealth.pollmanagement.dto.ErrorDTO;
import com.collectivehealth.pollmanagement.dto.PollDTO;
import com.collectivehealth.pollmanagement.form.PollForm;
import com.collectivehealth.pollmanagement.service.PollService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("poll")
@Api("poll")
@Log4j2
public class PollController {
    
    @Autowired
    private PollService pollService;
    
    @ApiOperation(value = "Get Poll")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Poll found.", response = PollDTO.class),
            @ApiResponse(code = 400, message = "Poll not found.", response = PollDTO.class),
            @ApiResponse(code = 500, message = "Internal failure.", response = PollDTO.class) })
    @GetMapping("{id}")
    public ResponseEntity<PollDTO> getOne(@PathVariable Integer id) {
        log.info("Request get poll: {}", id);
        Optional<PollDTO> poll = pollService.findById(id);
        if (poll.isPresent()) {
            return ResponseEntity.ok(poll.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @ApiOperation(value = "Get Poll List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Poll found.", response = PollDTO.class),
            @ApiResponse(code = 400, message = "Poll not found.", response = PollDTO.class),
            @ApiResponse(code = 500, message = "Internal failure.", response = PollDTO.class) })
    @GetMapping
    public List<PollDTO> getAll() {
        log.info("Request all polls");
        return pollService.findAll();
    }

    @ApiOperation(value = "Create Poll")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Poll created.", response = PollDTO.class),
            @ApiResponse(code = 400, message = "Poll already exists.", response = ErrorDTO.class),
            @ApiResponse(code = 500, message = "Internal failure.", response = ErrorDTO.class) })
    @PostMapping
    public ResponseEntity<PollDTO> create(@RequestBody @Valid PollForm pollForm, UriComponentsBuilder uriBuilder) {
        log.info("Request create poll: {}", pollForm);
        PollDTO pollWithOptionsDTO = pollService.create(pollForm);
        if (pollWithOptionsDTO.hasErrors()) {
            return ResponseEntity.badRequest().body(pollWithOptionsDTO);
        }
        
        URI uri = uriBuilder.path("/poll/{id}").buildAndExpand(pollWithOptionsDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(pollWithOptionsDTO);
    }

    @ApiOperation(value = "Update Poll")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Poll updated.", response = PollDTO.class),
            @ApiResponse(code = 404, message = "Poll not found.", response = ErrorDTO.class),
            @ApiResponse(code = 500, message = "Internal failure.", response = ErrorDTO.class) })
    @PutMapping("{id}")
    public ResponseEntity<PollDTO> update(@PathVariable Integer id, @RequestBody @Valid PollForm pollForm) {
        log.info("Request update poll: {}", id);
        PollDTO pollWithOptionsDTO = pollService.update(id, pollForm);
        if (pollWithOptionsDTO.hasErrors()) {
            return ResponseEntity.badRequest().body(pollWithOptionsDTO);
        }
        return ResponseEntity.ok(pollWithOptionsDTO);
    }
}
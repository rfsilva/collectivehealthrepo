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
import com.collectivehealth.pollmanagement.dto.PollOptionDTO;
import com.collectivehealth.pollmanagement.form.PollOptionForm;
import com.collectivehealth.pollmanagement.service.PollOptionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("poll-option")
@Api("poll-option")
@Log4j2
public class PollOptionController {
    
    @Autowired
    private PollOptionService pollOptionService;
    
    @ApiOperation(value = "Get Option")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Poll option found.", response = PollDTO.class),
            @ApiResponse(code = 400, message = "Poll not found.", response = PollDTO.class),
            @ApiResponse(code = 500, message = "Internal failure.", response = PollDTO.class) })
    @GetMapping("{id}")
    public ResponseEntity<PollOptionDTO> getOne(@PathVariable Integer id) {
        log.info("Request get poll option: {}", id);
        Optional<PollOptionDTO> poll = pollOptionService.findById(id);
        if (poll.isPresent()) {
            return ResponseEntity.ok(poll.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @ApiOperation(value = "Get Option List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Poll option list found.", response = PollDTO.class),
            @ApiResponse(code = 500, message = "Internal failure.", response = PollDTO.class) })
    @GetMapping("poll/{pollId}")
    public List<PollOptionDTO> getAll(@PathVariable Integer pollId) {
        log.info("Request all options of poll");
        return pollOptionService.findByPollId(pollId);
    }

    @ApiOperation(value = "Create Poll Option")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Poll option created.", response = PollDTO.class),
            @ApiResponse(code = 400, message = "Poll option already exists.", response = ErrorDTO.class),
            @ApiResponse(code = 500, message = "Internal failure.", response = ErrorDTO.class) })
    @PostMapping
    public ResponseEntity<PollOptionDTO> create(@RequestBody @Valid PollOptionForm pollOptionForm, UriComponentsBuilder uriBuilder) {
        log.info("Request create option {}", pollOptionForm);
        PollOptionDTO pollOptionDTO = pollOptionService.create(pollOptionForm);
        if (pollOptionDTO.hasErrors()) {
            return ResponseEntity.badRequest().body(pollOptionDTO);
        }
        
        URI uri = uriBuilder.path("/poll-option/{id}").buildAndExpand(pollOptionDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(pollOptionDTO);
    }

    @ApiOperation(value = "Update Poll Option")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Poll updated.", response = PollDTO.class),
            @ApiResponse(code = 404, message = "Poll not found.", response = ErrorDTO.class),
            @ApiResponse(code = 500, message = "Internal failure.", response = ErrorDTO.class) })
    @PutMapping("{id}")
    public ResponseEntity<PollOptionDTO> update(@PathVariable Integer pollOptionId, @RequestBody @Valid PollOptionForm pollOptionForm) {
        log.info("Request update poll option: {}", pollOptionId);
        PollOptionDTO pollOptionDTO = pollOptionService.update(pollOptionId, pollOptionForm);
        if (pollOptionDTO.hasErrors()) {
            return ResponseEntity.badRequest().body(pollOptionDTO);
        }
        return ResponseEntity.ok(pollOptionDTO);
    }
}
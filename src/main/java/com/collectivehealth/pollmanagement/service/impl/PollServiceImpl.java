package com.collectivehealth.pollmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collectivehealth.pollmanagement.dto.PollDTO;
import com.collectivehealth.pollmanagement.dto.PollOptionDTO;
import com.collectivehealth.pollmanagement.enums.RequireAuthentication;
import com.collectivehealth.pollmanagement.form.PollForm;
import com.collectivehealth.pollmanagement.message.MessageConstants;
import com.collectivehealth.pollmanagement.message.MessageManager;
import com.collectivehealth.pollmanagement.model.Poll;
import com.collectivehealth.pollmanagement.model.User;
import com.collectivehealth.pollmanagement.repository.PollRepository;
import com.collectivehealth.pollmanagement.repository.UserRepository;
import com.collectivehealth.pollmanagement.service.PollOptionService;
import com.collectivehealth.pollmanagement.service.PollService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PollServiceImpl implements PollService {

	private final PollOptionService pollOptionService;
    private final PollRepository pollRepository;
    private final UserRepository userRepository;
	
	@Autowired
	public PollServiceImpl(PollRepository pollRepository,
	        UserRepository userRepository,
	        PollOptionService pollOptionService) {
		this.pollRepository = pollRepository;
		this.userRepository = userRepository;
		this.pollOptionService = pollOptionService;
	}
	
	public List<PollDTO> findAll() {
	    List<Poll> result = pollRepository.findAll();
	    return result.stream().map(PollDTO::convert).collect(Collectors.toList());
	}
    
    public Optional<PollDTO> findById(Integer id) {
        log.info("Getting poll by ID {}", id);
        Optional<Poll> opt = pollRepository.findById(id);
        if (opt.isPresent()) {
            List<PollOptionDTO> options = pollOptionService.findByPollId(id);
            PollDTO poll = PollDTO.convert(opt.get());
            poll.setOptions(options);
            return Optional.of(poll);
        }
        return Optional.ofNullable(null);
    }
    
    @Transactional
    public PollDTO create(@Valid PollForm poll) {

        log.info("Check if the poll exists");
        Optional<Poll> opt = pollRepository.findByTitle(poll.getTitle());
        if (opt.isPresent()) {
            log.info("Poll with defined title already exists: {}", poll.getTitle());
            return PollDTO.convert(HttpStatus.BAD_REQUEST.value(), 
                    MessageManager.getMessage(MessageConstants.POLL_TITLE_ALREADY_EXISTS, poll.getTitle()));
        }

        log.info("Check if the user is valid");
        User owner = User.builder().id(poll.getOwnerId()).build();
        Optional<User> userOpt = userRepository.findById(owner.getId());
        if (!userOpt.isPresent()) {
            log.info("Invalid user: {}", poll.getTitle());
            return PollDTO.convert(HttpStatus.BAD_REQUEST.value(), 
                    MessageManager.getMessage(MessageConstants.INVALID_USER, owner.getId()));
        }
                
        log.info("Adding poll {}", poll.getTitle());
        Poll entity = Poll.builder()
                .description(poll.getDescription())
                .owner(owner)
                .requireAuthentication(RequireAuthentication.of(poll.getRequireAuthentication()))
                .title(poll.getTitle())
                .build();

        Poll res = pollRepository.save(entity);
        List<PollOptionDTO> optionsDTO = pollOptionService.create(res, poll.getOptions());
        PollDTO pollDTO = PollDTO.convert(res);
        pollDTO.setOptions(optionsDTO);
        return pollDTO;
    }

    @Override
    @Transactional
    public PollDTO update(Integer pollId, PollForm poll) {
        log.info("Check if the user is valid");
        User owner = User.builder().id(poll.getOwnerId()).build();
        Optional<User> userOpt = userRepository.findById(owner.getId());
        if (!userOpt.isPresent()) {
            log.info("Invalid user: {}", poll.getOwnerId());
            return PollDTO.convert(HttpStatus.BAD_REQUEST.value(), 
                    MessageManager.getMessage(MessageConstants.INVALID_USER, owner.getId()));
        }
        owner = userOpt.get();
        
        Optional<Poll> opt = pollRepository.findById(pollId);
        if (!opt.isPresent()) {
            log.info("Invalid poll: {}", poll.getTitle());
            return PollDTO.convert(HttpStatus.BAD_REQUEST.value(), 
                    MessageManager.getMessage(MessageConstants.INVALID_POLL, pollId));
        }

        Poll entity = opt.get();
        entity.setDescription(poll.getDescription());
        entity.setOwner(owner);
        entity.setRequireAuthentication(RequireAuthentication.of(poll.getRequireAuthentication()));
        entity.setTitle(poll.getTitle());
        pollRepository.save(entity);
        
        return PollDTO.convert(entity);
    }

    @Override
    public List<PollDTO> getPollList(Integer userId) {
        List<Poll> res = pollRepository.findByOwner(userId);
        return res.stream().map(PollDTO::convert).collect(Collectors.toList());
    }
}

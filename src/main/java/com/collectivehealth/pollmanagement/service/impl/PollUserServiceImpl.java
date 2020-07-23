package com.collectivehealth.pollmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collectivehealth.pollmanagement.dto.PollDTO;
import com.collectivehealth.pollmanagement.dto.PollUserDTO;
import com.collectivehealth.pollmanagement.form.PollUserForm;
import com.collectivehealth.pollmanagement.message.MessageConstants;
import com.collectivehealth.pollmanagement.message.MessageManager;
import com.collectivehealth.pollmanagement.model.Poll;
import com.collectivehealth.pollmanagement.model.PollUser;
import com.collectivehealth.pollmanagement.model.User;
import com.collectivehealth.pollmanagement.repository.PollRepository;
import com.collectivehealth.pollmanagement.repository.PollUserRepository;
import com.collectivehealth.pollmanagement.repository.UserRepository;
import com.collectivehealth.pollmanagement.service.PollUserService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PollUserServiceImpl implements PollUserService {

    private final PollRepository pollRepository;
    private final UserRepository userRepository;
    private final PollUserRepository pollUserRepository;
	
	@Autowired
	public PollUserServiceImpl(PollUserRepository pollUserRepository,
	        PollRepository pollRepository,
	        UserRepository userRepository) {
		this.pollUserRepository = pollUserRepository;
        this.pollRepository = pollRepository;
        this.userRepository = userRepository;
	}

    @Override
    @Transactional
    public PollUserDTO associate(PollUserForm pollUserForm) {
        
        final Integer userId = pollUserForm.getUserId();
        final Integer pollId = pollUserForm.getPollId();
        
        log.info("Check if the user is valid");
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            log.info("Invalid user: {}", userId);
            return PollUserDTO.convert(HttpStatus.BAD_REQUEST.value(), 
                    MessageManager.getMessage(MessageConstants.INVALID_USER, userId));
        }
        User user = userOpt.get();
        
        Optional<Poll> pollOpt = pollRepository.findById(pollId);
        if (!pollOpt.isPresent()) {
            log.info("Invalid poll: {}", pollId);
            return PollUserDTO.convert(HttpStatus.BAD_REQUEST.value(), 
                    MessageManager.getMessage(MessageConstants.INVALID_POLL, pollId));
        }
        Poll poll = pollOpt.get();
        
        Optional<PollUser> opt = pollUserRepository.findByPollUser(pollId, userId);
        if (opt.isPresent()) {
            log.info("User {} is already associated to poll {}", userId, pollId);
            return PollUserDTO.convert(HttpStatus.BAD_REQUEST.value(), 
                    MessageManager.getMessage(MessageConstants.USER_ALREADY_ASSOCIATED_TO_POLL, pollId, userId));
        }
        
        PollUser entity = PollUser.builder()
                .poll(poll)
                .user(user)
                .build();
        pollUserRepository.save(entity);
        return PollUserDTO.convert(entity);
    }

    @Override
    @Transactional
    public PollUserDTO deassociate(Integer id) {
        Optional<PollUser> opt = pollUserRepository.findById(id);
        if (!opt.isPresent()) {
            log.info("Invalid poll-user association: {}", id);
            return PollUserDTO.convert(HttpStatus.BAD_REQUEST.value(), 
                    MessageManager.getMessage(MessageConstants.INVALID_POLL_USER_ASSOCIATION, id));
        }
        
        PollUser entity = opt.get();
        pollUserRepository.delete(entity);
        return PollUserDTO.convert(entity);
    }
    
    @Override
    public List<PollDTO> listPolls(Integer userId) {
        List<Poll> pollList = pollUserRepository.getPolls(userId);
        return pollList.stream().map(PollDTO::convert).collect(Collectors.toList());
    }
}

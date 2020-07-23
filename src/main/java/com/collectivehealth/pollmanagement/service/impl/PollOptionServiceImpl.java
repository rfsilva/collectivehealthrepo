package com.collectivehealth.pollmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collectivehealth.pollmanagement.dto.PollOptionDTO;
import com.collectivehealth.pollmanagement.enums.RequireAuthentication;
import com.collectivehealth.pollmanagement.form.PollOptionForm;
import com.collectivehealth.pollmanagement.form.PollOptionResponseChangeForm;
import com.collectivehealth.pollmanagement.form.PollOptionResponseForm;
import com.collectivehealth.pollmanagement.message.MessageConstants;
import com.collectivehealth.pollmanagement.message.MessageManager;
import com.collectivehealth.pollmanagement.model.Poll;
import com.collectivehealth.pollmanagement.model.PollOption;
import com.collectivehealth.pollmanagement.model.PollUser;
import com.collectivehealth.pollmanagement.model.User;
import com.collectivehealth.pollmanagement.repository.PollOptionRepository;
import com.collectivehealth.pollmanagement.repository.PollRepository;
import com.collectivehealth.pollmanagement.repository.PollUserRepository;
import com.collectivehealth.pollmanagement.repository.UserRepository;
import com.collectivehealth.pollmanagement.service.PollOptionService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PollOptionServiceImpl implements PollOptionService {

    private final PollRepository pollRepository;
	private final PollOptionRepository pollOptionRepository;
	private final UserRepository userRepository;
    private final PollUserRepository pollUserRepository;
	
	@Autowired
	public PollOptionServiceImpl(PollOptionRepository pollOptionRepository,
	        PollRepository pollRepository,
	        UserRepository userRepository,
	        PollUserRepository pollUserRepository) {
		this.pollOptionRepository = pollOptionRepository;
		this.pollRepository = pollRepository;
		this.userRepository = userRepository;
		this.pollUserRepository = pollUserRepository;
	}
	
    @Transactional
	public List<PollOptionDTO> create(Poll poll, List<String> options) {
	    log.info("Saving options {} of poll {}", options, poll);
	    return options.stream().map((String op) -> 
            PollOptionDTO.convert(pollOptionRepository.save(PollOption.builder()
                    .description(op)
                    .poll(poll)
                    .totalVotes(0)
                    .build()))
	            ).collect(Collectors.toList());
	}
	
	public List<PollOptionDTO> findByPollId(Integer pollId) {
	    return pollOptionRepository.findByPoll(pollId).stream().map(PollOptionDTO::convert).collect(Collectors.toList());
	}

    @Override
    @Transactional
    public PollOptionDTO create(PollOptionForm pollOption) {
        Integer pollId = pollOption.getPollId();
        Optional<Poll> pollOpt = pollRepository.findById(pollId);
        if (!pollOpt.isPresent()) {
            log.info("Invalid poll: {}", pollId);
            return PollOptionDTO.convert(HttpStatus.BAD_REQUEST.value(), 
                    MessageManager.getMessage(MessageConstants.INVALID_POLL, pollId));
        }
        Poll poll = pollOpt.get();
        PollOption po = PollOption.builder()
                .description(pollOption.getDescription())
                .poll(poll)
                .totalVotes(0)
                .build();
        pollOptionRepository.save(po);
        return PollOptionDTO.convert(po);
    }

    @Override
    public PollOptionDTO update(Integer pollOptionId, PollOptionForm pollOption) {
        Optional<PollOption> pollOptionOpt = pollOptionRepository.findById(pollOptionId);
        if (!pollOptionOpt.isPresent()) {
            log.info("Invalid poll option: {}", pollOptionId);
            return PollOptionDTO.convert(HttpStatus.BAD_REQUEST.value(), 
                    MessageManager.getMessage(MessageConstants.INVALID_POLL_OPTION, pollOptionId));
        }
        PollOption entity = pollOptionOpt.get();
        entity.setDescription(pollOption.getDescription());
        pollOptionRepository.save(entity);
        return PollOptionDTO.convert(entity);
    }

    @Override
    public Optional<PollOptionDTO> findById(Integer pollOptionId) {
        Optional<PollOption> opt = pollOptionRepository.findById(pollOptionId);
        if (opt.isPresent()) {
            return Optional.of(PollOptionDTO.convert(opt.get()));
        }
        return Optional.ofNullable(null);
    }

    @Override
    public PollOptionDTO registerVote(PollOptionResponseForm pollOptionForm) {
        
        Integer pollOptionId = pollOptionForm.getPollOptionId();
        Optional<PollOption> pollOptionOpt = pollOptionRepository.findById(pollOptionId);
        if (!pollOptionOpt.isPresent()) {
            log.info("Invalid poll option: {}", pollOptionId);
            return PollOptionDTO.convert(HttpStatus.BAD_REQUEST.value(), 
                    MessageManager.getMessage(MessageConstants.INVALID_POLL_OPTION, pollOptionId));
        }
        PollOption pollOption = pollOptionOpt.get();
        Poll poll = pollOption.getPoll();

        User user = null;
        if (poll.getRequireAuthentication() == RequireAuthentication.YES) {
            Integer userId = pollOptionForm.getUserId();
            if (userId == null) {
                log.info("Pull requires registered user to response: {}", poll.getId());
                return PollOptionDTO.convert(HttpStatus.BAD_REQUEST.value(), 
                        MessageManager.getMessage(MessageConstants.INVALID_USER, poll.getId()));
            }
            Optional<User> userOpt = userRepository.findById(userId);
            if (!userOpt.isPresent()) {
                log.info("Invalid user: {}", userId);
                return PollOptionDTO.convert(HttpStatus.BAD_REQUEST.value(), 
                        MessageManager.getMessage(MessageConstants.INVALID_USER, poll.getId()));
            }
            user = userOpt.get();
        }
        
        if (user != null && user.getId() != null) {
            Optional<PollUser> opt = pollUserRepository.findByPollUser(poll.getId(), user.getId());
            if (opt.isPresent()) {
                log.info("Duplicate answer by same user: {}", user.getId());
                return PollOptionDTO.convert(HttpStatus.BAD_REQUEST.value(), 
                        MessageManager.getMessage(MessageConstants.DUPLICATE_USER_RESPONSE, poll.getId()));
            }
        }
        pollOption.setTotalVotes(pollOption.getTotalVotes() + 1);
        pollOptionRepository.save(pollOption);
        
        if (user != null) {
            registerVoteToUser(poll, pollOption, user);
        }
        return PollOptionDTO.convert(pollOption);
    }

    @Override
    public PollOptionDTO changeVote(PollOptionResponseChangeForm pollOptionForm) {
        
        Integer pollOptionId = pollOptionForm.getPollOptionId();
        Optional<PollOption> pollOptionOpt = pollOptionRepository.findById(pollOptionId);
        if (!pollOptionOpt.isPresent()) {
            log.info("Invalid poll option: {}", pollOptionId);
            return PollOptionDTO.convert(HttpStatus.BAD_REQUEST.value(), 
                    MessageManager.getMessage(MessageConstants.INVALID_POLL_OPTION, pollOptionId));
        }
        PollOption pollOption = pollOptionOpt.get();
        
        Integer newPollOptionId = pollOptionForm.getNewPollOptionId();
        Optional<PollOption> newPollOptionOpt = pollOptionRepository.findById(newPollOptionId);
        if (!newPollOptionOpt.isPresent()) {
            log.info("Invalid poll option: {}", newPollOptionId);
            return PollOptionDTO.convert(HttpStatus.BAD_REQUEST.value(), 
                    MessageManager.getMessage(MessageConstants.INVALID_POLL_OPTION, newPollOptionId));
        }
        PollOption newPollOption = newPollOptionOpt.get();
        if (!pollOption.getPoll().equals(newPollOption.getPoll())) {
            log.info("Options must be part of the same poll: {} and {}", pollOption.getPoll().getId(), newPollOption.getPoll().getId());
            return PollOptionDTO.convert(HttpStatus.BAD_REQUEST.value(), 
                    MessageManager.getMessage(MessageConstants.OPTIONS_DIFFERENT_POLL, pollOptionId, newPollOptionId));
        }
        
        Poll poll = pollOption.getPoll();
        Integer userId = pollOptionForm.getUserId();
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            log.info("Invalid user: {}", userId);
            return PollOptionDTO.convert(HttpStatus.BAD_REQUEST.value(), 
                    MessageManager.getMessage(MessageConstants.INVALID_USER, poll.getId()));
        }
        User user = userOpt.get();
        
        pollOption.setTotalVotes(pollOption.getTotalVotes() - 1);
        pollOptionRepository.save(pollOption);

        newPollOption.setTotalVotes(newPollOption.getTotalVotes() + 1);
        pollOptionRepository.save(newPollOption);
        
        registerVoteToUser(poll, pollOption, user);
        return PollOptionDTO.convert(pollOption);
    }
    
    private PollUser registerVoteToUser(Poll poll, PollOption pollOption, User user) {
        Optional<PollUser> opt = pollUserRepository.findByPollUser(poll.getId(), user.getId());
        if (opt.isPresent()) {
            PollUser pollUser = opt.get();
            pollUser.setPollOption(pollOption);
            pollUserRepository.save(pollUser);
            return pollUser;
        }
        
        PollUser pollUser = PollUser.builder()
                .poll(poll)
                .pollOption(pollOption)
                .user(user)
                .build();
        pollUserRepository.save(pollUser);
        return pollUser;
    }
}

package com.collectivehealth.pollmanagement.service;

import java.util.List;
import java.util.Optional;

import com.collectivehealth.pollmanagement.dto.PollOptionDTO;
import com.collectivehealth.pollmanagement.form.PollOptionForm;
import com.collectivehealth.pollmanagement.form.PollOptionResponseChangeForm;
import com.collectivehealth.pollmanagement.form.PollOptionResponseForm;
import com.collectivehealth.pollmanagement.model.Poll;

public interface PollOptionService {

    public List<PollOptionDTO> create(Poll poll, List<String> options);
    public PollOptionDTO create(PollOptionForm pollOption);
    public PollOptionDTO update(Integer pollOptionId, PollOptionForm pollOption);
    public Optional<PollOptionDTO> findById(Integer pollOptionId);
    public List<PollOptionDTO> findByPollId(Integer pollId);
    public PollOptionDTO registerVote(PollOptionResponseForm responseForm);
    public PollOptionDTO changeVote(PollOptionResponseChangeForm responseForm);
}

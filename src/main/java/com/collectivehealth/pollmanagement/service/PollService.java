package com.collectivehealth.pollmanagement.service;

import java.util.List;
import java.util.Optional;

import com.collectivehealth.pollmanagement.dto.PollDTO;
import com.collectivehealth.pollmanagement.form.PollForm;

public interface PollService {

    public List<PollDTO> findAll(); 

    public Optional<PollDTO> findById(Integer id); 
    
    public PollDTO create(PollForm poll);
    
    public PollDTO update(Integer pollId, PollForm poll);
    
    public List<PollDTO> getPollList(Integer userId);
}

package com.collectivehealth.pollmanagement.service;

import java.util.List;

import com.collectivehealth.pollmanagement.dto.PollDTO;
import com.collectivehealth.pollmanagement.dto.PollUserDTO;
import com.collectivehealth.pollmanagement.form.PollUserForm;

public interface PollUserService {
    
    public PollUserDTO associate(PollUserForm pollUserForm);
    
    public PollUserDTO deassociate(Integer id);
    
    public List<PollDTO> listPolls(Integer userId);
}

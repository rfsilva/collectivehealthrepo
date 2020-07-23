package com.collectivehealth.pollmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.collectivehealth.pollmanagement.model.PollOption;

public interface PollOptionRepository extends JpaRepository<PollOption, Integer> {
    
    @Query("select o from PollOption o where o.poll.id = :pollId")
    List<PollOption> findByPoll(@Param("pollId") Integer pollId);
}

package com.collectivehealth.pollmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.collectivehealth.pollmanagement.model.Poll;
import com.collectivehealth.pollmanagement.model.PollUser;

public interface PollUserRepository extends JpaRepository<PollUser, Integer> {
    
    @Query("select o from PollUser o where o.user.id = :userId")
    List<PollUser> findByUser(@Param("userId") Integer userId);
    
    @Query("select o from PollUser o where o.poll.id = :pollId and o.user.id = :userId")
    Optional<PollUser> findByPollUser(@Param("pollId") Integer pollId, @Param("userId") Integer userId);
    
    @Query("select o.poll from PollUser o where o.user.id = :userId")
    List<Poll> getPolls(@Param("userId") Integer userId);
}

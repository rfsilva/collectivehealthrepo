package com.collectivehealth.pollmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.collectivehealth.pollmanagement.model.Poll;

public interface PollRepository extends JpaRepository<Poll, Integer> {

    @Query("select p from Poll p where p.title = :title")
    Optional<Poll> findByTitle(@Param("title") String title);

    @Query("select p from Poll p where p.owner.id = :owner")
    List<Poll> findByOwner(@Param("owner") Integer owner);

}

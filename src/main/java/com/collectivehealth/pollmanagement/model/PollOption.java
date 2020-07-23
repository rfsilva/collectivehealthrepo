package com.collectivehealth.pollmanagement.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "TB_POLL_OPTION")
public class PollOption implements Serializable{

    private static final long serialVersionUID = 8969561498226838057L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, unique = true)
    private Integer id;

    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "TOTAL_VOTES")
    private Integer totalVotes;
    
    @ManyToOne
    @JoinColumn(name = "POLL_ID", nullable = false)
    private Poll poll;
}

package com.collectivehealth.pollmanagement.dto;

import com.collectivehealth.pollmanagement.model.PollOption;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(Include.NON_NULL)
public class PollOptionDTO {

    private Integer id;
    private Integer pollId;
    private String description;
    private Integer totalVotes;
    private ErrorDTO error;
    public boolean hasErrors() {
        return error != null && error.getCode() != null;
    }
    
    public static PollOptionDTO convert(PollOption pollOption) {
        return builder()
                .id(pollOption.getId())
                .pollId(pollOption.getPoll().getId())
                .description(pollOption.getDescription())
                .totalVotes(pollOption.getTotalVotes())
                .build();
    }
    
    public static PollOptionDTO convert(Integer code, String description) {
        return PollOptionDTO.builder()
                .error(ErrorDTO.builder()
                        .code(code)
                        .description(description)
                        .build())
                .build();
    }
}

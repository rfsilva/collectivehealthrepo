package com.collectivehealth.pollmanagement.dto;

import java.util.List;

import com.collectivehealth.pollmanagement.model.Poll;
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
public class PollDTO {

    private Integer id;
    private String title;
    private String description;
    private Integer requireAuthentication;
    private Integer ownerId;
    private List<PollOptionDTO> options;
    private ErrorDTO error;
    public boolean hasErrors() {
        return error != null && error.getCode() != null;
    }
    
    public static PollDTO convert(Poll poll) {
        return builder()
                .id(poll.getId())
                .description(poll.getDescription())
                .requireAuthentication(poll.getRequireAuthentication().getValue())
                .title(poll.getTitle())
                .ownerId(poll.getOwner().getId())
                .build();
    }
    
    public static PollDTO convert(Integer code, String description) {
        return PollDTO.builder()
                .error(ErrorDTO.builder()
                        .code(code)
                        .description(description)
                        .build())
                .build();
    }
}

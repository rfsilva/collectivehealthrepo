package com.collectivehealth.pollmanagement.dto;

import com.collectivehealth.pollmanagement.model.PollUser;
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
public class PollUserDTO {

    private Integer id;
    private PollDTO poll;
    private UserDTO user;
    private ErrorDTO error;
    public boolean hasErrors() {
        return error != null && error.getCode() != null;
    }
    
    public static PollUserDTO convert(PollUser pollUser) {
        return builder()
                .id(pollUser.getId())
                .poll(PollDTO.builder().id(pollUser.getPoll().getId()).build())
                .user(UserDTO.builder().id(pollUser.getUser().getId()).build())
                .build();
    }
    
    public static PollUserDTO convert(Integer code, String description) {
        return PollUserDTO.builder()
                .error(ErrorDTO.builder()
                        .code(code)
                        .description(description)
                        .build())
                .build();
    }
}

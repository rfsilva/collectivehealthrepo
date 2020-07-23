package com.collectivehealth.pollmanagement.dto;

import com.collectivehealth.pollmanagement.model.User;
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
public class UserDTO {

    private Integer id;
    private String username;
    private ErrorDTO error;
    public boolean hasErrors() {
        return error != null && error.getCode() != null;
    }
    
    public static UserDTO convert(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
    
    public static UserDTO convert(Integer code, String description) {
        return UserDTO.builder()
                .error(ErrorDTO.builder()
                        .code(code)
                        .description(description)
                        .build())
                .build();
    }
}

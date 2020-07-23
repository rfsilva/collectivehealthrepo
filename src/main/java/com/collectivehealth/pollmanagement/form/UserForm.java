package com.collectivehealth.pollmanagement.form;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(5)
    @Min(50)
    private String username;
    
    @NotNull
    @Min(5)
    private String password;
}

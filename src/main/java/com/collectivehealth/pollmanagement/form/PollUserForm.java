package com.collectivehealth.pollmanagement.form;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PollUserForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty(required = true, notes = "Poll ID")
    private Integer pollId;

    @NotNull
    @ApiModelProperty(required = true, notes = "User ID")
    private Integer userId;
}

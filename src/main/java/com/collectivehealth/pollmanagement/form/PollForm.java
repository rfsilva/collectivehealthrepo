package com.collectivehealth.pollmanagement.form;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
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
public class PollForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(5)
    @Max(50)
    @ApiModelProperty(required = true, notes = "Title")
    private String title;
    
    @NotNull
    @Min(5)
    @Max(100)
    @ApiModelProperty(required = true, notes = "Brief description")
    private String description;

    @NotNull
    @ApiModelProperty(required = true, notes = "Owner of the poll")
    private Integer ownerId;
    
    @ApiModelProperty(required = false, dataType = "java.lang.Integer", allowableValues = "0,1", notes = "Defines if the registration is required to participate. 0 - Not Required, 1 - Required")
    private Integer requireAuthentication;
    
    @NotEmpty
    @ApiModelProperty(required = true, notes = "List of options")
    private List<String> options;
}

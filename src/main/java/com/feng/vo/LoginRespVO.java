package com.feng.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRespVO {

    @ApiModelProperty(value = "用户 id")
    private String id;

    @ApiModelProperty(value = "token")
    private String token;
}

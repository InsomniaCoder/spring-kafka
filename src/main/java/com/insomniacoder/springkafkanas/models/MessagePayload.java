package com.insomniacoder.springkafkanas.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessagePayload {

    @Pattern(regexp = "(type[0-9],)*(type[0-9])", flags = Pattern.Flag.UNICODE_CASE)
    @NotNull
    private String type;
    @NotNull
    private String message;
}
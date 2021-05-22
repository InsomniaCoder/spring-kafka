package com.insomniacoder.springkafkanas.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessagePayload {
    @NotNull
    private String type;
    @NotNull
    private String message;
}
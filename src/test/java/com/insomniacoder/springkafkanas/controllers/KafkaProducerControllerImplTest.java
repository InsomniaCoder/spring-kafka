package com.insomniacoder.springkafkanas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insomniacoder.springkafkanas.models.MessagePayload;
import com.insomniacoder.springkafkanas.services.interfaces.MessageProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = KafkaProducerControllerImpl.class)
class KafkaProducerControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    MessageProducer messageProducer;

    final private String SEND_MESSAGE_URL = "/send-payload";

    @Test
    public void testControllerProduceWithOneMessageTypeShouldGetOK() throws Exception {

        doNothing().when(messageProducer).produceMessage(any());

        MessagePayload payload = MessagePayload.builder().type("type1")
                .message("test").build();

        this.mockMvc.perform(post(SEND_MESSAGE_URL)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk());
        verify(messageProducer).produceMessage(eq(payload));
    }

    @Test
    public void testControllerProduceWithMultipleMessageTypeShouldGetOK() throws Exception {

        doNothing().when(messageProducer).produceMessage(any());

        MessagePayload payload = MessagePayload.builder().type("type1,type2,type3")
                .message("test").build();

        this.mockMvc.perform(post(SEND_MESSAGE_URL)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk());
        verify(messageProducer).produceMessage(eq(payload));
    }

    @Test
    public void testControllerProduceWithEmptyMessageTypeShouldGetBadRequest() throws Exception {

        doNothing().when(messageProducer).produceMessage(any());

        MessagePayload payload = MessagePayload.builder()
                .message("test").build();

        this.mockMvc.perform(post(SEND_MESSAGE_URL)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testControllerProduceWithEmptyMessageShouldGetBadRequest() throws Exception {

        doNothing().when(messageProducer).produceMessage(any());

        MessagePayload payload = MessagePayload.builder().type("type1").build();

        this.mockMvc.perform(post(SEND_MESSAGE_URL)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testControllerProduceWithWrongMessageTypeShouldGetBadRequest() throws Exception {

        MessagePayload payload = MessagePayload.builder().type("type1,,,")
                .message("test").build();

        this.mockMvc.perform(post(SEND_MESSAGE_URL)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testControllerProduceWithWrongMessageFormatShouldGetBadRequest() throws Exception {

        MessagePayload payload = MessagePayload.builder().type("one,two,,")
                .message("test").build();

        this.mockMvc.perform(post(SEND_MESSAGE_URL)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

}
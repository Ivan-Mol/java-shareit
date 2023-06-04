package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.RequestReturnDto;
import ru.practicum.shareit.request.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = RequestController.class)
class RequestControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private RequestService requestServiceMock;

    @SneakyThrows
    @Test
    void getById_requestIsCorrect_returnRequest() {
        RequestReturnDto expected = new RequestReturnDto();
        expected.setId(1L);
        expected.setDescription("description");
        expected.setCreated(LocalDateTime.now());

        when(requestServiceMock.getById(any(), any())).thenReturn(expected);

        String response = mockMvc.perform(get("/requests/{requestId}", expected.getId())
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        RequestReturnDto actual = objectMapper.readValue(response, RequestReturnDto.class);
        assertEquals(expected, actual);
        verify(requestServiceMock).getById(any(), any());
    }

    @SneakyThrows
    @Test
    void getById_requestIsInCorrect_returnNotfoundException() {
        Long requestId = 1L;
        Long userId = 1L;
        when(requestServiceMock.getById(requestId, userId)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        verify(requestServiceMock).getById(requestId, userId);
    }

    @SneakyThrows
    @Test
    void getAllByRequestor_userIsCorrect_returnRequestList() {
        RequestReturnDto expected = new RequestReturnDto();
        expected.setId(1L);
        expected.setDescription("description");
        expected.setCreated(LocalDateTime.now());
        when(requestServiceMock.getAllByRequestor(anyLong())).thenReturn(List.of(expected));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(expected))));
        verify(requestServiceMock).getAllByRequestor(1L);
    }

    @SneakyThrows
    @Test
    void getAll() {
        RequestReturnDto expected = new RequestReturnDto();
        expected.setId(1L);
        expected.setDescription("description");
        expected.setCreated(LocalDateTime.now());
        when(requestServiceMock.getAll(anyLong(), anyInt(), anyInt())).thenReturn(List.of(expected));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(expected))));
        verify(requestServiceMock).getAll(1L, 0, 10);
    }

    @SneakyThrows
    @Test
    void create_requestIsValid_returnRequest() {
        RequestReturnDto expected = new RequestReturnDto();
        expected.setId(1L);
        expected.setDescription("description");
        expected.setCreated(LocalDateTime.now());

        when(requestServiceMock.create(any(), any())).thenReturn(expected);

        String result = mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expected)))

                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)))
                .andReturn().getResponse().getContentAsString();
        assertEquals(result, objectMapper.writeValueAsString(expected));
        verify(requestServiceMock).create(any(), any());
    }

//    @SneakyThrows
//    @Test
//    void createRequest_whenRequestIsInvalid_ValidationException() {
//        RequestReturnDto expected = new RequestReturnDto();
//        expected.setId(1L);
//        expected.setDescription(null);
//        expected.setCreated(LocalDateTime.now());
//
//        mockMvc.perform(post("/requests")
//                        .header("X-Sharer-User-Id", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(expected)))
//                .andExpect(status().isBadRequest());
//        verify(requestServiceMock, never()).create(any(), any());
//    }
}
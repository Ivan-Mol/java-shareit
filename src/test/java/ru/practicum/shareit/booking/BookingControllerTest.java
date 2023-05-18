package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemShortDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = BookingController.class)
class BookingControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private BookingService bookingServiceMock;

    @SneakyThrows
    @Test
    void getBookingById() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);

        ItemShortDto itemShortDto = new ItemShortDto(1L, "itemNameTest");

        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(1L);
        bookingResponseDto.setStart(start);
        bookingResponseDto.setEnd(end);
        bookingResponseDto.setItem(itemShortDto);
        when(bookingServiceMock.getById(bookingResponseDto.getId(), 1L)).thenReturn(bookingResponseDto);

        mockMvc.perform(get("/bookings/{bookingId}", bookingResponseDto.getId())
                        .header("X-Sharer-User-Id", 1L))

                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingResponseDto)));
    }

    @SneakyThrows
    @Test
    void create() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(123123L);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);

        ItemShortDto itemShortDto = new ItemShortDto(1L, "itemNameTest");

        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(1L);
        bookingResponseDto.setStart(start);
        bookingResponseDto.setEnd(end);
        bookingResponseDto.setItem(itemShortDto);
        when(bookingServiceMock.create(bookingDto, 1L)).thenReturn(bookingResponseDto);

        String result = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))

                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(bookingResponseDto), result);
    }

    @SneakyThrows
    @Test
    void approvingByOwner() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);


        ItemShortDto itemShortDto = new ItemShortDto(1L, "itemNameTest");

        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(1L);
        bookingResponseDto.setStart(start);
        bookingResponseDto.setEnd(end);
        bookingResponseDto.setItem(itemShortDto);

        when(bookingServiceMock.approvingByOwner(bookingResponseDto.getId(), 1L, true)).thenReturn(bookingResponseDto);

        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingResponseDto.getId())
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(bookingResponseDto), result);
    }

    @SneakyThrows
    @Test
    void getAllByBooker_isValid() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);

        ItemShortDto itemShortDto = new ItemShortDto(1L, "itemNameTest");

        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(1L);
        bookingResponseDto.setStart(start);
        bookingResponseDto.setEnd(end);
        bookingResponseDto.setItem(itemShortDto);
        when(bookingServiceMock.getAllByBooker(1L, "ALL", 0, 10)).thenReturn(List.of(bookingResponseDto));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bookingResponseDto))));
    }

    @SneakyThrows
    @Test
    void getAllByBooker_StateIsInvalid_ConstraintViolationException() {
        when(bookingServiceMock.getAllByBooker(1L, "someString", 0, 1)).thenThrow(ValidationException.class);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "someString")
                        .param("from", "0")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllByBooker_FromIsInvalid_ConstraintViolationException() {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "-1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getAllByBooker_SizeIsInvalid_ConstraintViolationException() {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "1")
                        .param("size", "9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getAllByOwner_isCorrect() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);

        ItemShortDto itemShortDto = new ItemShortDto(1L, "itemNameTest");

        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(1L);
        bookingResponseDto.setStart(start);
        bookingResponseDto.setEnd(end);
        bookingResponseDto.setItem(itemShortDto);
        when(bookingServiceMock.getAllByOwner(any(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingResponseDto));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bookingResponseDto))));
    }

    @SneakyThrows
    @Test
    void getAllByOwner_StateIsInvalid_ConstraintViolationException() {
        when(bookingServiceMock.getAllByOwner(1L, "someString", 0, 1)).thenThrow(ValidationException.class);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "someString")
                        .param("from", "0")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllByOwner_FromIsInvalid_ConstraintViolationException() {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "-1")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getAllByOwner_SizeIsInvalid_ConstraintViolationException() {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "1")
                        .param("size", "9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void deleteBookingById() {
        mockMvc.perform(delete("/bookings/{id}", 1L))
                .andExpect(status().isOk());
        verify(bookingServiceMock).deleteById(1L);
    }
}
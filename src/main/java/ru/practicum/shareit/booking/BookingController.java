package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingReturnDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @PathVariable Long bookingId) {
        log.debug("received GET /items/{}", bookingId);
        return bookingService.getById(bookingId, userId);
    }

    @PostMapping
    public BookingReturnDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @RequestBody @Valid BookingDto bookingDto) {
        log.debug("received GET /booking by/{}", userId);
        return bookingService.create(bookingDto, userId);
    }

    @GetMapping
    public List<BookingReturnDto> getAllByOwner(@RequestHeader("X-Sharer-User-Id") long bookerId,
                                                @PathParam("state") String state) {
        log.debug("received GET /items/");
        return bookingService.getAllByOwner(bookerId, state);
    }

    @DeleteMapping("/{id}")
    public void deleteBookingById(@PathVariable Long id) {
        log.info("received DELETE /bookings/{id} by id = {}", id);

    }
}

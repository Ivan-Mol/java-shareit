package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long bookingId) {
        log.debug("received GET /booking Id/{}", bookingId);
        return bookingService.getById(bookingId, userId);
    }

    @PostMapping
    public BookingResponseDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestBody @Valid BookingDto bookingDto) {
        log.debug("received POST /booking by user Id/{}", userId);
        return bookingService.create(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approvingByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                               @PathVariable("bookingId") Long bookingId,
                                               @RequestParam Boolean approved) {
        log.debug("received PATCH /booking by/{}", ownerId);
        return bookingService.approvingByOwner(bookingId, ownerId, approved);
    }

    @GetMapping
    public List<BookingResponseDto> getAllByBooker(@RequestHeader("X-Sharer-User-Id") long bookerId,
                                                   @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                   @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(name = "size", defaultValue = "20") @Positive @Max(100) Integer size) {
        log.debug("received GET /AllByBooker with Id/{}", bookerId);
        return bookingService.getAllByBooker(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllByOwner(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                  @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                  @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                  @RequestParam(name = "size", defaultValue = "20") @Min(1) @Max(100) Integer size) {
        log.debug("received GET /AllByOwner with Id/{}", ownerId);
        return bookingService.getAllByOwner(ownerId, state, from, size);
    }

    @DeleteMapping("/{id}")
    public void deleteBookingById(@PathVariable Long id) {
        log.debug("received DELETE /bookings/{id} by id = {}", id);
        bookingService.deleteById(id);
    }
}

package ru.practicum.shareit.request.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    default Request getByIdAndCheck(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Request with " + id + " Id is not found"));
    }

    List<Request> getAllByRequestorId(Long requestorId);

    List<Request> findRequestsByRequestorIdNotOrderByCreatedDesc(Long userId, Pageable pageable);
}

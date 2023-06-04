package ru.practicum.shareit.item.storage;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    String query = " select i from Item i " +
            "where lower(i.name) like lower(concat('%', :search, '%')) " +
            " or lower(i.description) like lower(concat('%', :search, '%')) " +
            " and i.available = true";

    List<Item> findByOwnerIdOrderByIdAsc(Long owner, PageRequest of);

    @Query(query)
    List<Item> getItemsByQuery(@Param("search") String text);

    default Item getItemByIdAndCheck(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Item with " + id + " Id is not found"));
    }

    List<Item> getItemsByRequestIn(List<Request> requests);

    List<Item> getItemsByRequest(Request request);
}
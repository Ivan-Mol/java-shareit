package ru.practicum.shareit.request.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description; //текст запроса, содержащий описание требуемой вещи;
    private LocalDateTime created; //дата и время создания запроса;
    @NotNull
    @JoinColumn(name = "requestor_id")
    private Long requestorId; //пользователь, создавший запрос;

}

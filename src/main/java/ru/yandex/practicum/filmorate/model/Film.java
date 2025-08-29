package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = {"id"})
@ToString
@AllArgsConstructor
public class Film {
    private Long id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @NotBlank(message = "Описание не может быть пустым")
    @Size(max = 200, message = "Максимальная длина описания 200 символов")
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Дата релиза не может быть пустой")
    private LocalDate releaseDate;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @NotNull(message = "Продолжительность не может быть пустой")
    private Duration duration;
}

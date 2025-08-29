package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = {"id"})
@ToString
@AllArgsConstructor
public class User {
    private Long id;
    @Email(message = "Почта должна быть корректной")
    @NotBlank(message = "Почта не может быть пустой")
    private String email;
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^[^ ]+$", message = "Логин не может содержать пробелы")
    private String login;
    private String name;
    @NotNull(message = "Дата рождения не может быть пустой")
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
}

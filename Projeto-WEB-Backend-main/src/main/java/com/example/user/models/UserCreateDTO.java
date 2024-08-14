package com.example.user.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UserCreateDTO(
    @NotBlank(message = "CAMPO OBRIGATORIO:nome!")
    String nome,

    @Min(value = 0, message = "A idade nao pode ser negativa!")
    int idade,

    @Email(message = "INSIRA UM E-MAIL VALIDO!")
    @NotBlank(message = "CAMPO OBRIGATORIO:email!")
    String email,

    @NotBlank(message = "CAMPO OBRIGATORIO:senha!")
    String password
) {}
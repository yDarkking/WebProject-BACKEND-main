package com.example.user.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(
   @NotBlank(message = "CAMPO OBRIGATORIO:nome!")
    @NotNull(message = "CAMPO OBRIGATORIO:nome!")
    String nome,

    @Min(value = 0, message = "A IDADE DEVE SER MAIOR QUE 0!")
    @NotNull(message = "CAMPO OBRIGATORIO:idade!")
    int idade,

    @Email(message = "INSIRA UM E-MAIL VALIDO!")
    @NotBlank(message = "CAMPO OBRIGATORIO:email!")
    String email,

    @NotBlank(message = "CAMPO OBRIGATORIO:senha!")
    @Size(min = 8, max = 128, message = "A senha deve ter entre 8 e 128 caracteres!")
    String password
) {}
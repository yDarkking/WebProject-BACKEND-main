package com.example.user.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.user.exceptions.user.AuthenticationException;
import com.example.user.mappers.UserMapper;
import com.example.user.models.LoginDTO;
import com.example.user.models.User;
import com.example.user.models.UserCreateDTO;
import com.example.user.models.UserGetResponseDTO;
import com.example.user.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @GetMapping("/{id}")
    public UserGetResponseDTO findById(@PathVariable int id) {
        return userMapper.userToUserGetResponseDTO(userService.findById(id));
    }

    @GetMapping
    public List<UserGetResponseDTO> findUsers(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return userService.findAll(pageable).stream()
                .map(userMapper::userToUserGetResponseDTO)
                .collect(Collectors.toList());
    }
    @GetMapping("/pages")
    public List<User> listUsers(Pageable pageable) {
        return userService.listUsers(pageable).getContent();
    }

    @GetMapping("/login")
public ResponseEntity<String> login(@RequestParam("email") String email, @RequestParam("password") String password) {
    try {
        LoginDTO data = new LoginDTO(email, password);
        userService.login(data);
        return ResponseEntity.ok("Login efetuado com sucesso");
    } catch (AuthenticationException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    } catch (RuntimeException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }
}

    @PostMapping
    public UserGetResponseDTO save(@RequestBody @Valid UserCreateDTO data) {
        return userMapper.userToUserGetResponseDTO(userService.save(data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        boolean deleted = userService.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Usuário: " + id + " foi deletado!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
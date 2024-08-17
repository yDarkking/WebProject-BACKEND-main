package com.example.user.services;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.user.exceptions.user.AuthenticationException;
import com.example.user.exceptions.user.NoUsersToListException;
import com.example.user.exceptions.user.UserEmailAlreadyExistsException;
import com.example.user.exceptions.user.UserIdNotFoundException;
import com.example.user.models.LoginDTO;
import com.example.user.models.User;
import com.example.user.models.UserCreateDTO;
import com.example.user.repositories.UserPagesRepository;
import com.example.user.repositories.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserPagesRepository userPagesRepository;

    public User findById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new UserIdNotFoundException("Usuário de Id: " + id + " não foi encontrado!"));
    }

    public List<User> findAll() {
        List<User> allUsers = userRepository.findAll();
        if (allUsers.isEmpty()) {
            throw new NoUsersToListException("Não há usuários para listar!");
        }
        return allUsers;
    }

    public Page<User> listUsers(Pageable pageable) {
        return userPagesRepository.findAll(pageable);
    }

    public User login(LoginDTO data) throws AuthenticationException {
        return userRepository.findByEmail(data.email())
                .filter(user -> BCrypt.checkpw(data.password(), user.getPassword()))
                .orElseThrow(() -> new AuthenticationException("User not found or password incorrect"));
    }
    @Transactional
    public User save(@Valid UserCreateDTO data) {
        if (userRepository.findByEmail(data.email()).isPresent()) {
            throw new UserEmailAlreadyExistsException("Erro! Já existe um usuário com o mesmo email cadastrado");
        }
        return userRepository.save(User.fromDTOWithEncryptedPassword(data));
    }

    public boolean delete(int id) {
        User user = findById(id);
        userRepository.delete(user);
        return true;
    }
}
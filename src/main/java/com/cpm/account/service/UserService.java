package com.cpm.account.service;

import com.cpm.account.dto.user.UserResponse;
import com.cpm.account.entity.UserEntity;
import com.cpm.account.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;

    public Optional<UserEntity> findEntityById(Long id) {
        return repository.findById(id);
    }

    public UserResponse findById(Long id) {
        Optional<UserEntity> user = repository.findUserById(id);
        if (user.isPresent()) {
            return new UserResponse(user.get());
        }
        throw new IllegalStateException("user not exist");
    }

    public List<UserResponse> findAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size))
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }
}

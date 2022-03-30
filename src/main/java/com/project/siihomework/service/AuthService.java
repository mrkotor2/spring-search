package com.project.siihomework.service;

import com.project.siihomework.dto.AuthDto;
import com.project.siihomework.model.Auth;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthService {
    List<Auth> findAllByCriteria(String search);

    ResponseEntity<AuthDto> registry(AuthDto newTransaction);

    List<AuthDto> getAll();

    ResponseEntity<AuthDto> getById(Integer id);

    ResponseEntity<AuthDto> update(AuthDto updatedTransaction, Integer id);

    ResponseEntity<AuthDto> deleteById(Integer id);
}

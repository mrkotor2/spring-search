package com.project.siihomework.controller;

import com.project.siihomework.dto.AuthDto;
import com.project.siihomework.model.Auth;
import com.project.siihomework.service.impl.AuthServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/transactions")
public class TransactionController {

    private final AuthServiceImpl authServiceImpl;

    @Autowired
    public TransactionController(AuthServiceImpl authServiceImpl) {
        this.authServiceImpl = authServiceImpl;
    }


    @GetMapping(value = "/filter/")
    public List<Auth> search(@RequestParam(value = "search") String search) {
        return authServiceImpl.findAllByCriteria(search);
    }

    @GetMapping(value = "/")
    @ResponseStatus(HttpStatus.OK)
    public List<AuthDto> getTransactions() {
        return authServiceImpl.getAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AuthDto> getTransactionById(@PathVariable Integer id) {
        return authServiceImpl.getById(id);
    }

    @PostMapping(value = "/")
    public ResponseEntity<AuthDto> createTransaction(@RequestBody AuthDto newTransaction) {
        return authServiceImpl.registry(newTransaction);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AuthDto> updateTransaction(@RequestBody AuthDto oldTransaction, @PathVariable Integer id) {
        return authServiceImpl.update(oldTransaction, id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<AuthDto> deleteTransaction(@PathVariable Integer id) {
        return authServiceImpl.deleteById(id);
    }
}

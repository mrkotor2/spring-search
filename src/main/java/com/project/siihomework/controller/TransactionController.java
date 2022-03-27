package com.project.siihomework.controller;

import com.project.siihomework.dto.AuthDto;
import com.project.siihomework.model.Auth;
import com.project.siihomework.search.AuthSpecificationsBuilder;
import com.project.siihomework.service.AuthService;
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

    private final AuthService authService;

    @Autowired
    public TransactionController(AuthService authService) {
        this.authService = authService;
    }


    @GetMapping( value = "/filter/")
    public List<Auth> search(@RequestParam(value = "search") String search) {
        return authService.findAllByCriteria(search);
    }
    
//    @GetMapping(method = RequestMethod.GET, value = "/users/spec")
//    public List<Auth> findAllBySpecification(@RequestParam(value = "search") String search) {
//        AuthSpecificationsBuilder builder = new AuthSpecificationsBuilder();
//        String operationSetExper = Joiner.on("|")
//                .join(SearchOperation.SIMPLE_OPERATION_SET);
//        Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
//        Matcher matcher = pattern.matcher(search + ",");
//        while (matcher.find()) {
//            builder.with(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
//        }
//
//        Specification<Auth> spec = builder.build();
//        return dao.findAll(spec);
//    }

    @GetMapping(value = "/")
    @ResponseStatus(HttpStatus.OK)
    public List<AuthDto> getTransactions() {
        return authService.getAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AuthDto> getTransactionById(@PathVariable Integer id) {
        return authService.getById(id);
    }

    @PostMapping("/")
    public ResponseEntity<AuthDto> createTransaction(@RequestBody AuthDto newTransaction) {
        return authService.registry(newTransaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthDto> updateTransaction(@RequestBody AuthDto oldTransaction, @PathVariable Integer id) {
        return authService.update(oldTransaction, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AuthDto> deleteTransaction(@PathVariable Integer id) {
        return authService.deleteById(id);
    }
}

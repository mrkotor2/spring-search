package com.project.siihomework.service.impl;

import com.project.siihomework.dto.AuthDto;
import com.project.siihomework.model.Auth;
import com.project.siihomework.repository.TransactionRepository;
import com.project.siihomework.search.AuthSpecificationsBuilder;
import com.project.siihomework.service.AuthService;
import com.project.siihomework.translator.AuthTranslator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public AuthServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Auth> findAllByCriteria(String search) {

        log.info("Search string " + search);

        AuthSpecificationsBuilder builder = new AuthSpecificationsBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)([:!<>~])(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(4),
                    matcher.group(3),
                    matcher.group(5));
        }

        Specification<Auth> spec = builder.build();
        return transactionRepository.findAll(spec);
    }

    @Override
    public ResponseEntity<AuthDto> registry(AuthDto newTransaction) {

        log.info("Transaction started " + newTransaction);

        Auth auth = new Auth();
        new AuthTranslator().fromDto(newTransaction, auth);
        log.info("Transaction ended successfully " + auth);

        transactionRepository.save(auth);
        log.info("Transaction saved successfully " + auth);

        new AuthTranslator().toDto(auth, newTransaction);

        return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
    }

    @Override
    public List<AuthDto> getAll() {

        List<Auth> allTransact = transactionRepository.findAll();
        List<AuthDto> allTransactDto = new ArrayList<>();

        for (Auth transaction : allTransact) {
            AuthDto authDto = new AuthDto();
            new AuthTranslator().toDto(transaction, authDto);
            allTransactDto.add(authDto);
        }
        return allTransactDto;
    }

    @Override
    public ResponseEntity<AuthDto> getById(Integer id) {

        if (!transactionRepository.existsById(id)) {
            throw new EntityNotFoundException("NO SUCH Transaction exist");
        }

        Auth auth = transactionRepository.getById(id);
        AuthDto authDto = new AuthDto();
        log.info("Auth  " + auth);

        new AuthTranslator().toDto(auth, authDto);

        return new ResponseEntity<>(authDto, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<AuthDto> update(AuthDto updatedTransaction, Integer id) {

        if (!transactionRepository.existsById(id)) {
            throw new EntityNotFoundException("NO SUCH Transaction");
        }

        Auth oldTransaction = transactionRepository.getById(id);
        oldTransaction.setData(updatedTransaction.getData());
        oldTransaction.setType(updatedTransaction.getType());
        oldTransaction.setActor(updatedTransaction.getActor());

        log.info("Transaction with id: " + id + " is updated successfully before the save");

        Auth newTrans = transactionRepository.save(oldTransaction);
        AuthDto authDto = new AuthDto();
        new AuthTranslator().toDto(newTrans, authDto);

        return new ResponseEntity<>(authDto, HttpStatus.CREATED);

    }

    @Override
    public ResponseEntity<AuthDto> deleteById(Integer id) {

        if (!transactionRepository.existsById(id)) {
            throw new EntityNotFoundException("NO SUCH Transaction");
        }

        Auth auth = transactionRepository.getById(id);
        AuthDto authDto = new AuthDto();
        log.info("Auth  " + auth);


        new AuthTranslator().toDto(auth, authDto);


        log.info("Auth exists " + auth);
        transactionRepository.deleteById(auth.getId());
        log.info("Auth deleted " + auth);


        return new ResponseEntity<>(authDto, HttpStatus.GONE);

    }


}

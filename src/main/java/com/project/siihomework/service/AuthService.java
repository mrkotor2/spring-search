package com.project.siihomework.service;

import com.project.siihomework.dto.AuthDto;
import com.project.siihomework.model.Auth;
import com.project.siihomework.repository.TransactionRepository;
import com.project.siihomework.search.AuthSpecificationsBuilder;
import com.project.siihomework.search.SearchCriteria;
import com.project.siihomework.translator.AuthTranslator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class AuthService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public AuthService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Auth> findAllByCriteria(String search) {
        log.info("Search string " + search);

        AuthSpecificationsBuilder builder = new AuthSpecificationsBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(3));
//                    matcher.group(4),
//                    matcher.group(5));
        }

        Specification<Auth> spec = builder.build();
        log.info("SPEC " + String.valueOf(spec));
        return transactionRepository.findAll(spec);
//        List<SearchCriteria> params = new ArrayList<SearchCriteria>();
//        if (search != null) {
//            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
//            Matcher matcher = pattern.matcher(search + ",");
//            while (matcher.find()) {
//                params.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
//            }
//        }
//        return service.searchUser(params);
    }

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

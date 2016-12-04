package com.example.controllers;

import com.example.db.AccountRepository;
import com.example.db.ImageRepository;
import com.example.models.Account;
import com.example.models.Image;
import com.example.models.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Created by mvukosav on 4.12.2016..
 */
@RestController
@RequestMapping("/accounts")
public class AccountRestController {

    private final AccountRepository accountRepository;
    private final ImageRepository imageRepository;

    @Autowired
    AccountRestController(AccountRepository accountRepository, ImageRepository imageRepository) {
        this.accountRepository = accountRepository;
        this.imageRepository = imageRepository;
    }

    /**
     * @param userId of searched account
     * @return account
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{userId}")
    Account getAccount(@PathVariable Long userId) {
        this.validateUser(userId);
        return accountRepository.findOne(userId);
    }

    @RequestMapping(method = RequestMethod.POST)
    Account createAccount(@RequestBody Account input) {
        return accountRepository.save(input);
    }

    @RequestMapping(method = RequestMethod.PUT)
    Account updateAccount(@RequestBody Account input) {
        validateUser(input.getId());
        Account account = accountRepository.findAccountById(input.getId());
        account.username = input.username;
        account.password = input.password;
        accountRepository.save(account);
        return account;
    }

    //    localhost:8080/accounts/3/?access_token=b3dd0426-e5d3-473e-a567-93956cece181
    @RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
    ResponseEntity<Account> deleteAccount(@PathVariable Long userId) {
        this.validateUser(userId);
        Account account = accountRepository.findAccountById(userId);
        if (account != null) {
            Collection<Image> images = imageRepository.findByAccountUsername(account.username);
            for (Image image : images) {
                imageRepository.delete(image);
            }
            accountRepository.delete(account);
            return new ResponseEntity<Account>(HttpStatus.OK);
        }

        return new ResponseEntity<Account>(HttpStatus.NOT_FOUND);
    }

    /**
     * Get all accounts
     *
     * @return accounts list
     */
    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<Collection<Account>> getAccounts() {
        Collection<Account> accounts = this.accountRepository.findAll();
        if (accounts.isEmpty()) {
            return new ResponseEntity<Collection<Account>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<Collection<Account>>(accounts, HttpStatus.OK);
    }

//    @RequestMapping(method = RequestMethod.GET)
//    ResponseEntity<?> add(@PathVariable Long userId) {
//        this.validateUser(userId);
//        return this.accountRepository
//                .findById(userId)
//                .map(account -> {
//                    Account account1 = account;
//
//                    //header
//                    URI location = ServletUriComponentsBuilder
//                            .fromCurrentRequest().path("/{id}")
//                            .buildAndExpand(account1.getId()).toUri();
//
//                    return ResponseEntity.created(location).build();
//                })
//                .orElse(ResponseEntity.noContent().build());
//    }


    private void validateUser(Long userId) {
        this.accountRepository.findById(userId)
                .orElseThrow(
                        () -> new UserNotFoundException(String.valueOf(userId)));
    }

}

package com.example;

import com.example.db.AccountRepository;
import com.example.db.ImageRepository;
import com.example.models.Image;
import com.example.models.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

/**
 * Created by mvukosav on 4.12.2016..
 */
@RestController
@RequestMapping("/{userId}/images")
public class ImageRestController {

    private final ImageRepository imageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    ImageRestController(ImageRepository imageRepository, AccountRepository accountRepository) {
        this.imageRepository = imageRepository;
        this.accountRepository = accountRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Image> getImages(@PathVariable String userId) {
        this.validateUser(userId);
        return this.imageRepository.findByAccountUsername(userId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{imageId}")
    Image getImage(@PathVariable String userId, @PathVariable Long imageId) {
        this.validateUser(userId);
        return this.imageRepository.findOne(Long.valueOf(userId));
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@PathVariable String userId, @RequestBody Image input) {
        this.validateUser(userId);
        return this.accountRepository
                .findByUsername(userId)
                .map(account -> {
                    Image result = imageRepository.save(new Image(account, input.name));

                    //header
                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest().path("/{id}")
                            .buildAndExpand(result.getId()).toUri();

                    return ResponseEntity.created(location).build();
                })
                .orElse(ResponseEntity.noContent().build());
    }

    private void validateUser(String userId) {
        this.accountRepository.findByUsername(userId)
                .orElseThrow(
                        () -> new UserNotFoundException(userId));
    }
}

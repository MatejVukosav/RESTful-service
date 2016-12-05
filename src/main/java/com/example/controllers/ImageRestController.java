package com.example.controllers;

import com.example.db.AccountRepository;
import com.example.db.ImageRepository;
import com.example.models.Image;
import com.example.models.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    /**
     * Get all images from user with id userId
     * @param userId id from user
     * @return images
     */
    @RequestMapping(method = RequestMethod.GET)
    Collection<Image> getImages(@PathVariable Long userId) {
        this.validateUser(userId);
        return this.accountRepository.findById(userId).get().getImages();
    }

//    @RequestMapping(method = RequestMethod.GET, value = "/{imageId}")
//    Image getImageById(@PathVariable Long userId, @PathVariable Long imageId) {
//        this.validateUser(userId);
//        return this.imageRepository.findOne(Long.valueOf(imageId));
//    }

    /**
     *
     * @param userId id from owner of image
     * @param imageId id of image
     * @return image with imageId from user with id userId
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{imageId}")
    ResponseEntity<Image> getUserImageById(@PathVariable Long userId, @PathVariable Long imageId) {
        this.validateUser(userId);
        Collection<Image> images = this.accountRepository.findById(userId).get().getImages();
        for (Image image : images) {
            if (image.getId().equals(imageId)) {
                return new ResponseEntity<Image>(image, HttpStatus.OK);
            }
        }
        return new ResponseEntity<Image>(HttpStatus.NOT_FOUND);
    }

    /**
     * Add another image to account  with id userId
     * @param userId id of usew
     * @param input image to be added to user
     * @return image if image is added else no content
     */
    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@PathVariable Long userId, @RequestBody Image input) {
        this.validateUser(userId);
        return this.accountRepository
                .findById(userId)
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

    /**
     * Validate if user exists
     * @param userId id from user to be validated
     */
    private void validateUser(Long userId) {
        this.accountRepository.findById(userId)
                .orElseThrow(
                        () -> new UserNotFoundException(String.valueOf(userId)));
    }

}

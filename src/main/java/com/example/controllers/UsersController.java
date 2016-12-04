package com.example.controllers;

import com.example.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by mvukosav on 4.12.2016..
 */
@RestController
@RequestMapping("/users")
public class UsersController {


    /**
     * Get user data
     *
     * @param id from user to be returned
     * @return user
     */
    @RequestMapping(value = "/{id}", method = GET)
    public User getUser(@PathVariable("id") long id) {
        User user = new User("Matej", 3);
        return user;
    }

    /**
     * Create new user
     *
     * @param user to be created
     * @return success message
     */
    @RequestMapping(method = POST)
    public String postUser(@RequestBody User user) {
        return "azuriran";
    }

    /**
     * Update user
     *
     * @param user to be updated
     * @return success message
     */
    @RequestMapping(value = "/", method = PUT)
    public String putUser(@RequestBody User user) {
        return "sve oke";
    }

    /**
     * Delete user
     *
     * @param id from user to be deleted
     * @return success message
     */
    @RequestMapping(value = "/{id}", method = DELETE)
    public String deleteUser(@PathVariable("id") long id) {
        return "obrisan";
    }
}

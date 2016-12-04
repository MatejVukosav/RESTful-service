package com.example;

import com.example.db.AccountRepository;
import com.example.db.ImageRepository;
import com.example.models.Account;
import com.example.models.Image;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class Application {


    // Tutorial   https://spring.io/guides/tutorials/bookmarks/
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init(AccountRepository accountRepository, ImageRepository imageRepository) {
        return (evt) -> Arrays.asList(
                "matej123,jack,pero".split(","))
                .forEach(
                        a -> {
                            Account account = accountRepository.save(new Account(a,
                                    "password"));
                            imageRepository.save(new Image(account, "slika1"));
                            imageRepository.save(new Image(account, "slika2"));
                        });
    }
}

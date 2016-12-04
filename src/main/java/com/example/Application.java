package com.example;

import com.example.db.AccountRepository;
import com.example.db.ImageRepository;
import com.example.models.Account;
import com.example.models.Image;
import com.example.models.LogModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

@SpringBootApplication
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class Application {
    File file = new File("log_data.txt");

    // Tutorial   https://spring.io/guides/tutorials/bookmarks/
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Filter loggingFilter() {
        AbstractRequestLoggingFilter f = new AbstractRequestLoggingFilter() {

            @Override
            protected void beforeRequest(HttpServletRequest request, String message) {
//                System.out.println("beforeRequest: " + message);

                final String servletPath = request.getServletPath();
                final String userAgent = request.getHeader("User-Agent");
                LogModel logModel = new LogModel(userAgent, servletPath);
                System.out.println("beforeRequest: " + logModel.toString());


                FileOutputStream fileOutputStream = null;
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    fileOutputStream = new FileOutputStream(file, true);
                    fileOutputStream.write('\n');
                    fileOutputStream.write(logModel.toString().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
//                PrintWriter out = null;
//                try {
//                    out = new PrintWriter(file);
//                    out.println(logModel.toString());
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (out != null) {
//                        out.close();
//                    }
//                }

            }

            @Override
            protected void afterRequest(HttpServletRequest request, String message) {
//                System.out.println("afterRequest: " + message);
            }
        };
        f.setIncludeClientInfo(true);
        f.setIncludePayload(true);
        f.setIncludeQueryString(true);

        f.setBeforeMessagePrefix("BEFORE REQUEST  [");
        f.setAfterMessagePrefix("AFTER REQUEST    [");
        f.setAfterMessageSuffix("]\n");
        return f;
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

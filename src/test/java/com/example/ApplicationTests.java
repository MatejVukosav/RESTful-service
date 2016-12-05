package com.example;

import com.example.db.AccountRepository;
import com.example.db.ImageRepository;
import com.example.models.Account;
import com.example.models.Image;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {Application.class})
@WebAppConfiguration
public class ApplicationTests {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    private MockMvc mockMvc;
    private String username = "matej123";
    private String password = "password";
    private String pictureName1 = "slika_test_1";
    private String pictureName2 = "slika_test_2";
    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    private Account account;
    private List<Image> images = new ArrayList<>();

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);
    }

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).dispatchOptions(true).build();

//        this.mockMvc = webAppContextSetup(webApplicationContext)
//                .build();
        this.imageRepository.deleteAllInBatch();
        this.accountRepository.deleteAllInBatch();

        this.account = accountRepository.save(new Account(username, password));
        this.images.add(imageRepository.save(new Image(account, pictureName1)));
        this.images.add(imageRepository.save(new Image(account, pictureName2)));

    }

    @Test
    public void userNotFound() throws Exception {
        mockMvc.perform(get("/api/accounts/10"))
                .andExpect(status().isNotFound());
    }



    @Test
    public void getUserImageById() throws Exception {
        int userId = 4;

        mockMvc.perform(get("/api/" + userId + "/images/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.[*]", hasSize(2)))
                .andExpect(jsonPath("$.[*].name", containsInAnyOrder(images.get(0).getName(), images.get(1).getName())))
                .andExpect(jsonPath("$.[*].id", containsInAnyOrder(images.get(0).getId().intValue(), images.get(1).getId().intValue())));
    }

    @Test
    public void getAccountById() throws Exception {
        int userId = 4;

        mockMvc.perform(get("/api/accounts/" + userId + "/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(userId)))
                .andExpect(jsonPath("$.username", is(username)))
                .andExpect(jsonPath("$.images", hasSize(2)))
                .andExpect(jsonPath("$.images[*].name", containsInAnyOrder(images.get(0).getName(), images.get(1).getName())))
                .andExpect(jsonPath("$.images[*].id", containsInAnyOrder(images.get(0).getId().intValue(), images.get(1).getId().intValue())));
    }


//    @Test
    public void getAccountToken() throws Exception {

        String basicDigestHeaderValue = "Basic " + new String(Base64.encodeBase64(("my-trusted-client:secret").getBytes()));
        mockMvc.perform(
                post("/oauth/token?grant_type=password" + "&username=" + username + "&password=" + password)
                        .header("Authorization", basicDigestHeaderValue).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.access_token", instanceOf(String.class)))
                .andExpect(jsonPath("$.token_type", is("bearer")))
                .andExpect(jsonPath("$.refresh_token", instanceOf(String.class)))
                .andExpect(jsonPath("$.expires_in", instanceOf(Integer.class)))
                .andExpect(jsonPath("$.scope", is("read write trust")));
    }

    String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}

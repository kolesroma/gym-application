package com.epam.ai.xstack.kolesnyk.cucumber;

import com.epam.ai.xstack.kolesnyk.AiDrivenXStackApplication;
import com.epam.ai.xstack.kolesnyk.entity.UserEntity;
import com.epam.ai.xstack.kolesnyk.exception.BruteForceTryingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest(classes = AiDrivenXStackApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoginSteps {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private ResultActions mockMvcPerform;

    private RequestBuilder badPostRequest;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @When("^client makes good POST request to /auth/login$")
    public void clientMakesGoodPOSTRequestToAuthLogin() throws Exception {
        UserEntity login = new UserEntity();
        login.setUsername("kolesroma");
        login.setPassword("qwerty");
        mockMvcPerform = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .content(asJsonString(login))
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Then("the client receives status code of {int}")
    public void theClientReceivesStatusCodeOf(Integer statusCode) throws Exception {
        mockMvcPerform
                .andExpect(MockMvcResultMatchers.status().is(statusCode));
    }

    @And("the client receives jwt")
    public void theClientReceivesJwt() throws Exception {
        String jwtPattern = "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_.+/=]+$";
        mockMvcPerform
                .andExpect(MockMvcResultMatchers.content().string(
                        Matchers.matchesPattern(jwtPattern)
                ));
    }

    @When("^client makes bad POST request to /auth/login$")
    public void clientMakesBadPOSTRequestToAuthLogin() throws Exception {
        UserEntity login = new UserEntity();
        login.setUsername("notExistingUsername");
        login.setPassword("wrongPassword");
        badPostRequest = MockMvcRequestBuilders.post("/auth/login")
                .content(asJsonString(login))
                .contentType(MediaType.APPLICATION_JSON);
    }

    @And("login failed so client is getting suspicious")
    public void isGettingSuspicious() throws Exception {
        Throwable exception = Assertions.assertThrows(
                Exception.class,
                () -> mockMvc.perform(badPostRequest)
        );

        Assertions.assertTrue(
                getRootException(exception) instanceof BruteForceTryingException
        );
    }

    @When("client makes bad POST request to \\/auth\\/login {int} times")
    public void clientMakesBadPOSTRequestToAuthLoginTimes(int times) {
        UserEntity login = new UserEntity();
        login.setUsername("notExistingUsername");
        login.setPassword("wrongPassword");
        badPostRequest = MockMvcRequestBuilders.post("/auth/login")
                .content(asJsonString(login))
                .contentType(MediaType.APPLICATION_JSON);

        for (int i = 0; i < times; i++) {
            mvcPerformIgnoreThrowing(badPostRequest);
        }
    }

    @Then("client is temporary banned to access server")
    public void clientIsTemporaryBannedToAccessServer() {
        Throwable exception = Assertions.assertThrows(
                Exception.class,
                () -> mockMvc.perform(badPostRequest)
        );

        Assertions.assertTrue(
                getRootException(exception) instanceof AccessDeniedException
        );
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Throwable getRootException(Throwable exception) {
        Throwable rootCause = exception;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

    private void mvcPerformIgnoreThrowing(RequestBuilder requestBuilder) {
        try {
            mockMvc.perform(requestBuilder);
        } catch (Exception ignored) {
        }
    }

}

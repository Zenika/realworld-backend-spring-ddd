package io.realworld.backend.application.service;

import io.realworld.backend.rest.api.UserResponseData;
import io.restassured.http.Header;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class WebLayerUserServiceTest {

    @LocalServerPort
    private int port;
    @Test
    void register_should_create_profile() {
        final String token = with()
            .body("{ \"user\": { \"email\": \"testing@training.com\"," +
                " \"password\": \"test\", \"username\": \"TestingTraining\" " +
                "} }")
            .header(new Header("Content-Type", "application/json"))
            .when()
            .post("http://localhost:"+port+"/api/users")
            .then()
            .statusCode(200)
            .extract()
            .jsonPath().get("user.token");

        with()
            .header(new Header("Authorization", "Bearer " + token))
            .get("http://localhost:"+port+"/api/user")
            .then()
            .statusCode(200)
            .body("user.email", equalTo("testing@training.com"));

        with()
            .header(new Header("Authorization", "Bearer " + token))
            .header(new Header("Content-Type", "application/json"))
            .body("{\"article\": {\"body\": \"Toto\", \"title\": \"Test\", \"description\": \"Desc\", \"tagList\": []}}")
            .post("http://localhost:"+port+"/api/articles")
            .then()
            .statusCode(200)
            .body("article.title", equalTo("Test"));

    }
}

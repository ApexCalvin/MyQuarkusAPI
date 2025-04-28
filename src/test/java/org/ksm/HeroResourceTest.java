package org.ksm;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class HeroResourceTest {

    @Test
    void getAll_return200OK() {
        given()
          .when().get("/hero")
          .then().statusCode(200);
    }

    @Test
    void getById_returnResponseBody() {
    }

    @Test
    void create_return201CREATED_andResponseBody() {
    }

    @Test
    void deleteById_return404NotFound_andResponseBody() {
    }

    @Test
    void deleteById_return200OK_andResponseBody() {
    }

    @Test
    void replaceById_return200OK_andResponseBody() {
    }

    @Test
    void replaceById_return404NotFound_andResponseBody() {
    }

    @Test
    void updateById_return200OK_andResponseBody() {
    }

    @Test
    void updateById_return404NotFound_andResponseBody() {
    }
}
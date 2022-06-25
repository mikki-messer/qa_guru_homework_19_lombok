package com.mikkimesser;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.mikkimesser.Specifications.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsMapContaining.hasKey;

public class ReqresInTests {

    Faker faker = new Faker();

    @Test
    @DisplayName("Проверка получения данных существующего пользователя по id")
    public void singleUserEmailByIdTest() {
        String endpoint = "https://reqres.in/api/users/3";

        given()
                .when()
                .get(endpoint)
                .then()
                .statusCode(200)
                .body("data.id", is(3))
                .body("data.first_name", is("Emma"))
                .body("data.last_name", is("Wong"))
                .body("data.email", is("emma.wong@reqres.in"))
        ;
    }

    @Test
    @DisplayName("Проверка структуры массива data для элемента списка ресурсов")
    public void resourceListTest() {
        String endpoint = "/unknown";

        given()
                .when()
                .spec(requestSpecification)
                .get(endpoint)
                .then()
                .spec(responseSpecification200)
                .body("data[0]", hasKey("id"))
                .body("data[0]", hasKey("year"))
                .body("data[0]", hasKey("name"))
                .body("data[0]", hasKey("color"))
                .body("data[0]", hasKey("pantone_value"));
    }

    @Test
    @DisplayName("Проверка эндпойнта создания пользователя")
    public void createUserTest() {
        String endpoint = "/user";

        String name = faker.name().firstName();
        String job = faker.job().position();

        String payload = String.format("{ \"name\": \"%s\", \"job\": \"%s\" }", name, job);

        given()
                .spec(requestSpecification)
                .body(payload)
                .when()
                .post(endpoint)
                .then()
                .spec(responseSpecification201)
                .body("name", is(name))
                .body("job", is(job));
    }

    @Test
    @DisplayName("Проверка статуса ответа при удалении пользователя")
    public void deleteUserTest() {
        String endpoint = "/users/4";

        given()
                .spec(requestSpecification)
                .when()
                .delete(endpoint)
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("Проверка обновления пользователя с помощью PUT")
    public void updateUserTest() {
        String endpoint = "/users/4";

        String name = faker.name().firstName();
        String job = faker.job().position();

        String payload = String.format("{ \"name\": \"%s\", \"job\": \"%s\" }", name, job);

        given()
                .spec(requestSpecification)
                .body(payload)
                .when()
                .put(endpoint)
                .then()
                .spec(responseSpecification200)
                .body("name", is(name))
                .body("job", is(job));
    }

}

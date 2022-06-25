package com.mikkimesser;

import com.github.javafaker.Faker;
import com.mikkimesser.models.UserData;
import com.mikkimesser.models.UserTiny;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.mikkimesser.Specifications.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsMapContaining.hasKey;

public class ReqresInTests {

    Faker faker = new Faker();

    @Test
    @DisplayName("Проверка получения данных существующего пользователя по id")
    public void singleUserEmailByIdTest() {

        String endpoint = "/users/3";

        UserData userData = given()
                .when()
                .spec(requestSpecification)
                .get(endpoint)
                .then()
                .spec(responseSpecification200)
                .extract().as(UserData.class);

        assertThat(userData.getData().getId(), equalTo(3));
        assertThat(userData.getData().getFirstName(), equalTo("Emma"));
        assertThat(userData.getData().getLastName(), equalTo("Wong"));
        assertThat(userData.getData().getEmail(), equalTo("emma.wong@reqres.in"));
    }

    @ValueSource(strings = {
            "george.bluth@reqres.in",
            "janet.weaver@reqres.in",
            "eve.holt@reqres.in"
    })
    @ParameterizedTest(name = "Проверка, что в список пользователей с id < 5 входит пользователь с email {0}")
    public void checkUserEmailsWithIdBelow5(String expectedEmail) {
        String endpoint = "/users";

        given()
                .when()
                .spec(requestSpecification)
                .get(endpoint)
                .then()
                .spec(responseSpecification200)
                .body("data.findAll{it.id < 5}.email", hasItem(expectedEmail));


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

        UserTiny payload = new UserTiny();
        payload.setJob(job);
        payload.setName(name);

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

        UserTiny payload = new UserTiny();
        payload.setJob(job);
        payload.setName(name);

        UserTiny userTiny = given()
                .spec(requestSpecification)
                .body(payload)
                .when()
                .put(endpoint)
                .then()
                .spec(responseSpecification200)
                .extract().as(UserTiny.class);

        assertThat(userTiny.getName(), equalTo(name));
        assertThat(userTiny.getJob(), equalTo(job));
    }

}

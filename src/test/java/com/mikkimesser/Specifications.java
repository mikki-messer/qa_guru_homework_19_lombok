package com.mikkimesser;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.http.ContentType.JSON;

public class Specifications {
    public static RequestSpecification requestSpecification = with()
            .baseUri("https://reqres.in")
            .basePath("/api")
            .contentType(JSON)
            .log().all(true);

    public static ResponseSpecification responseSpecification200 = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(LogDetail.ALL)
            .build();

    public static ResponseSpecification responseSpecification201 = new ResponseSpecBuilder()
            .expectStatusCode(201)
            .log(LogDetail.ALL)
            .build();
}

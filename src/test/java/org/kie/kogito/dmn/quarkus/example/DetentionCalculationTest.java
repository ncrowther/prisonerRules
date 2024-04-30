/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.dmn.quarkus.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class DetentionCalculationTest {

    private String readTestData(String testFile) {

        final String testDataDir = "E://IBAMOE//kogito-examples-main//kogito-examples-main//kogito-quarkus-examples//dmn-detention//src//test//resources//";
        String prisoner = "";

        try {

            prisoner = new String(Files.readAllBytes(Paths.get(
                    testDataDir + testFile + ".json")));
            System.out.println(prisoner);
        } catch (java.io.IOException e) {
            System.out.println("Failed to read test data");
        }

        return prisoner;
    }

    @Test
    public void testEvaluateDetentionEndDate() {

        String inputJson = readTestData("test1");
        given()
                .body(inputJson)
                .contentType(ContentType.JSON)
                .when()
                .post("/DetentionCalculation")
                .then()
                .statusCode(200)
                .body("CalculatedTitles[2].endDate", is("2024-02-09"));
    }

    @Test
    public void testEvaluateExecutedDetention() {

        String inputJson = readTestData("test1");
        given()
                .body(inputJson)
                .contentType(ContentType.JSON)
                .when()
                .post("/DetentionCalculation")
                .then()
                .statusCode(200)
                .body("UpdatedPrisonerTitles.status", is("executed"));
    }

    @Test
    public void testEvaluatePendingDetention() {

        String inputJson = readTestData("test2");
        given()
                .body(inputJson)
                .contentType(ContentType.JSON)
                .when()
                .post("/DetentionCalculation")
                .then()
                .statusCode(200)
                .body("UpdatedPrisonerTitles.status", is("in progress"));
    }    

}

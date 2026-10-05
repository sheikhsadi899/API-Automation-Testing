package tests;

import constants.Endpoints;
import constants.Params;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Pet;
import org.testng.annotations.Test;
import utils.RandomUtils;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class PetCrudApiKeyTest extends BaseTest {
    private static final String API_KEY_HEADER = "api_key";
    private static final String SPECIAL_KEY = "special-key";
    private static final String INVALID_API_KEY = "invalid-key";

    @Test(description = "Task 2 - Pet CRUD lifecycle")
    public void petCrudLifecycle() {
        String petName = RandomUtils.getRandomAlphabeticString();

        Pet newPet = new Pet(null, null, petName, List.of(), List.of(), "available");

        Response createResponse = given()
                .spec(getBaseReqSpec())
                .contentType(ContentType.JSON)
                .body(newPet)
                .when()
                .post(Endpoints.PET);

        createResponse.then()
                .statusCode(200)
                .contentType(ContentType.JSON);

        Pet createdPet = createResponse.as(Pet.class);
        Long createdPetId = createdPet.getId();
        assertNotNull(createdPetId, "Created pet should have an ID");

        createdPetIds.add(createdPetId);
        assertEquals(createdPet.getName(), petName);
        assertEquals(createdPet.getStatus(), "available");

        createdPet.setStatus("sold");

        Response updateResponse = given()
                .spec(getBaseReqSpec())
                .contentType(ContentType.JSON)
                .body(createdPet)
                .when()
                .put(Endpoints.PET);

        updateResponse.then()
                .statusCode(200)
                .contentType(ContentType.JSON);

        Pet updatedPet =updateResponse.as(Pet.class);
        assertEquals(updatedPet.getStatus(), "sold");

        Response getResponse = given()
                .spec(getBaseReqSpec())
                .pathParam(Params.ID, createdPetId)
                .when()
                .get(Endpoints.PET_BY_ID);

        getResponse.then()
                .statusCode(200)
                .contentType(ContentType.JSON);

        Pet retrievedPet = getResponse.as(Pet.class);
        assertEquals(retrievedPet.getId(), createdPetId);
        assertEquals(retrievedPet.getName(), petName);
        assertEquals(retrievedPet.getStatus(), "sold");
    }

    @Test(description = "Task 2 - Validate allowed Pet status")
    public void validatePetStatus() {

        String petName = RandomUtils.getRandomAlphabeticString();

        Pet newPet = new Pet(null, null, petName, List.of(), List.of(), "available");

        Response response = given()
                .spec(getBaseReqSpec())
                .contentType(ContentType.JSON)
                .body(newPet)
                .when()
                .post(Endpoints.PET);

        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON);

        Pet createdPet = response.as(Pet.class);
        Long createdPetId = createdPet.getId();
        createdPetIds.add(createdPetId);

        String status = createdPet.getStatus();
        assertTrue(status.equals("available") || status.equals("pending") || status.equals("sold"), "Unexpected Pet status: " + status);
    }

    @Test(description = "Task 2 - Delete Pet using API key")
    public void deletePetWithApiKey() {

        Pet newPet = new Pet(null, null, RandomUtils.getRandomAlphabeticString(), List.of(), List.of(), "available");

        Response createResponse = given()
                .spec(getBaseReqSpec())
                .contentType(ContentType.JSON)
                .body(newPet)
                .when()
                .post(Endpoints.PET);

        createResponse.then().statusCode(200);
        Pet createdPet = createResponse.as(Pet.class);

        Long petId = createdPet.getId();
        createdPetIds.add(petId);

        Response deleteResponse = given()
                .spec(getBaseReqSpec())
                .header(API_KEY_HEADER, SPECIAL_KEY)
                .pathParam(Params.ID, petId)
                .when()
                .delete(Endpoints.PET_BY_ID);

        deleteResponse.then().statusCode(200);
        createdPetIds.remove(petId);

        Response getResponse = given()
                .spec(getBaseReqSpec())
                .pathParam(Params.ID, petId)
                .when()
                .get(Endpoints.PET_BY_ID);

        getResponse.then().statusCode(404);
    }

    @Test(description = "Task 2 - Delete Pet without API key")
    public void deletePetWithoutApiKey() {

        Pet newPet = new Pet(null, null, RandomUtils.getRandomAlphabeticString(), List.of(), List.of(), "available");

        Response createResponse = given()
                .spec(getBaseReqSpec())
                .contentType(ContentType.JSON)
                .body(newPet)
                .when()
                .post(Endpoints.PET);

        createResponse.then()
                .statusCode(200);

        Pet createdPet = createResponse.as(Pet.class);
        Long petId = createdPet.getId();

        createdPetIds.add(petId);

        Response deleteResponse = given()
                .spec(getBaseReqSpec())
                .pathParam(Params.ID, petId)
                .when()
                .delete(Endpoints.PET_BY_ID);

        assertTrue(deleteResponse.statusCode() == 200, "Unexpected status code: " + deleteResponse.statusCode());
        if (deleteResponse.statusCode() == 200) {
            createdPetIds.remove(petId);
        }
    }

    @Test(description = "Task 2 - Delete Pet with invalid API key")
    public void deletePetWithInvalidApiKey() {

        Pet newPet = new Pet(null, null, RandomUtils.getRandomAlphabeticString(), List.of(), List.of(), "available");

        Response createResponse = given()
                .spec(getBaseReqSpec())
                .contentType(ContentType.JSON)
                .body(newPet)
                .when()
                .post(Endpoints.PET);


        createResponse.then().statusCode(200);
        Pet createdPet = createResponse.as(Pet.class);
        Long petId = createdPet.getId();
        createdPetIds.add(petId);

        Response deleteResponse = given()
                .spec(getBaseReqSpec())
                .header(API_KEY_HEADER, INVALID_API_KEY)
                .pathParam(Params.ID, petId)
                .when()
                .delete(Endpoints.PET_BY_ID);

        assertTrue(deleteResponse.statusCode() == 200, "Unexpected status code: " + deleteResponse.statusCode());
        if (deleteResponse.statusCode() == 200) {
            createdPetIds.remove(petId);
        }
    }
}
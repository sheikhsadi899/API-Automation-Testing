package tests;

import constants.Endpoints;
import constants.Params;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import models.Pet;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public abstract class BaseTest {
    private static final String API_URL = "https://petstore.swagger.io";
    protected final List<Long> createdPetIds = new ArrayList<>();
    protected final List<Long> createdOrderIds = new ArrayList<>();
    protected Pet pet;

    @BeforeMethod
    public void generatePet() {
        pet = new Pet();
        pet.setName(RandomUtils.getRandomAlphabeticString());
    }

    @AfterMethod
    public void deleteCreatedPets() {
        createdPetIds.forEach(this::deletePetById);
        createdOrderIds.forEach(this::deleteOrderById);

        // Clear the list after teardown: TestNG reuses one instance of this class across
        // every @Test method in a subclass, so leftover ids would otherwise stick around
        // and get "deleted" again (uselessly) on every subsequent test's teardown.
        createdPetIds.clear();
        createdOrderIds.clear();
    }

    protected void deletePetById(Long petId) {
        given()
                .spec(getBaseReqSpec())
                .pathParam(Params.ID, petId)
                .when()
                .delete(Endpoints.PET_BY_ID);
    }
    protected void deleteOrderById(Long orderId) {

        given()
                .spec(getBaseReqSpec())
                .pathParam(Params.ORDER_ID, orderId)
                .when()
                .delete(Endpoints.STORE_ORDER_BY_ID);
    }
    protected RequestSpecification getBaseReqSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(API_URL)
                .setBasePath(Endpoints.BASE_PATH)
                .setAccept(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }
}

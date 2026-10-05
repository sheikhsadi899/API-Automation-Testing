package tests;

import constants.Endpoints;
import constants.JsonPaths;
import constants.Params;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.ApiError;
import models.Order;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class StoreOrderTest extends BaseTest {

    @Test(description = "Task 1 - Verify store inventory structure")
    public void verifyStoreInventory() {

        Response response = given()
                .spec(getBaseReqSpec())
                .when()
                .get(Endpoints.STORE_INVENTORY);

        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON);

        Integer available = response.jsonPath().getInt(JsonPaths.INVENTORY_AVAILABLE);
        Integer pending = response.jsonPath().getInt(JsonPaths.INVENTORY_PENDING);
        Integer sold = response.jsonPath().getInt(JsonPaths.INVENTORY_SOLD);

        assertNotNull(available, "Available inventory should exist");
        assertNotNull(pending, "Pending inventory should exist");
        assertNotNull(sold, "Sold inventory should exist");

        assertTrue(available >= 0, "Available inventory cannot be negative");
        assertTrue(pending >= 0, "Pending inventory cannot be negative");
        assertTrue(sold >= 0, "Sold inventory cannot be negative");
    }

    @Test(description = "Task 1 - Complete Store Order E2E lifecycle")
    public void createGetAndDeleteOrder() {

        Response inventoryResponse = given()
                .spec(getBaseReqSpec())
                .when()
                .get(Endpoints.STORE_INVENTORY);

        inventoryResponse.then()
                .statusCode(200)
                .contentType(ContentType.JSON);

        Long petId = 1L;
        Integer quantity = 1;
        String shipDate = java.time.Instant.now().toString();

        Order orderRequest = new Order(null, petId, quantity, shipDate, "placed", false);

        Response createResponse = given()
                .spec(getBaseReqSpec())
                .contentType(ContentType.JSON)
                .body(orderRequest)
                .when()
                .post(Endpoints.STORE_ORDER);

        createResponse.then()
                .statusCode(200)
                .contentType(ContentType.JSON);

        Order createdOrder = createResponse.as(Order.class);
        Long createdOrderId = createdOrder.getId();
        assertNotNull(createdOrderId, "Created order should have an ID");
        createdOrderIds.add(createdOrderId);

        assertEquals(createdOrder.getPetId(), orderRequest.getPetId());
        assertEquals(createdOrder.getQuantity(), orderRequest.getQuantity());
        assertEquals(createdOrder.getStatus(), orderRequest.getStatus());
        assertEquals(createdOrder.getComplete(), orderRequest.getComplete());

        Response getResponse = given()
                .spec(getBaseReqSpec())
                .pathParam(Params.ORDER_ID, createdOrderId)
                .when()
                .get(Endpoints.STORE_ORDER_BY_ID);

        getResponse.then()
                .statusCode(200)
                .contentType(ContentType.JSON);

        Order retrievedOrder = getResponse.as(Order.class);

        assertEquals(retrievedOrder.getId(), createdOrderId);
        assertEquals(retrievedOrder.getPetId(), orderRequest.getPetId());
        assertEquals(retrievedOrder.getQuantity(), orderRequest.getQuantity());
        assertEquals(retrievedOrder.getStatus(), orderRequest.getStatus());
        assertEquals(retrievedOrder.getComplete(), orderRequest.getComplete());

        Response deleteResponse = given()
                .spec(getBaseReqSpec())
                .pathParam(Params.ORDER_ID, createdOrderId)
                .when()
                .delete(Endpoints.STORE_ORDER_BY_ID);

        deleteResponse.then()
                .statusCode(200);

        createdOrderIds.remove(createdOrderId);
    }

    @Test(description = "Task 1 - Get non-existent order")
    public void getNonExistentOrder() {

        long nonExistentOrderId = 999999999L;

        Response response = given()
                .spec(getBaseReqSpec())
                .pathParam(Params.ORDER_ID, nonExistentOrderId)
                .when()
                .get(Endpoints.STORE_ORDER_BY_ID);

        response.then()
                .statusCode(404)
                .contentType(ContentType.JSON);

        ApiError error = response.as(ApiError.class);

        assertNotNull(error.getCode());
        assertNotNull(error.getType());
        assertNotNull(error.getMessage());
        assertTrue(!error.getMessage().isBlank(), "Error message should not be empty");
    }

    @Test(description = "Task 1 - Create order with invalid quantity")
    public void createOrderWithInvalidQuantity() {

        Order invalidOrder = new Order(null, 1L, -1, java.time.Instant.now().toString(), "placed", false);

        Response response = given()
                .spec(getBaseReqSpec())
                .contentType(ContentType.JSON)
                .body(invalidOrder)
                .when()
                .post(Endpoints.STORE_ORDER);

        int statusCode = response.statusCode();

        assertTrue(statusCode == 200 || statusCode == 400, "Unexpected status code: " + statusCode);

        if (statusCode == 200) {
            Order createdOrder = response.as(Order.class);

            assertNotNull(createdOrder.getId());

            createdOrderIds.add(createdOrder.getId());
        }
    }
}
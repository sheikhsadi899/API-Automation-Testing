package constants;

import lombok.experimental.UtilityClass;

// Use these only for trivial single-field lookups (e.g. grabbing the created
// pet's id right after a POST). For anything with nested fields - like a pet's
// category name - deserialize the response into a Model class (see models/) and
// assert on that instead of chaining JsonPath lookups (e.g. avoid
// .body("category.name", equalTo(...))-style assertions). See the
// "Deserialization over JSONPath" requirement.
@UtilityClass
public class JsonPaths {
    public final String ID = "id";

    // ADDED: Store inventory JSON paths
    public final String INVENTORY_AVAILABLE = "available";
    public final String INVENTORY_PENDING = "pending";
    public final String INVENTORY_SOLD = "sold";
    // ADDED: Error response
    public final String ERROR_CODE = "code";
    public final String ERROR_TYPE = "type";
    public final String ERROR_MESSAGE = "message";
}

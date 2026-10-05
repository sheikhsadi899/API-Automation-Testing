package constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Endpoints {

    public final String BASE_PATH = "/v2";

    // PET ENDPOINTS
    public final String PET = "/pet";
    public final String PET_BY_ID = "%s/{%s}".formatted(PET, Params.ID);

    // STORE ENDPOINTS
    public final String STORE_INVENTORY = "/store/inventory";
    public final String STORE_ORDER = "/store/order";
    public final String STORE_ORDER_BY_ID = "%s/{%s}".formatted(STORE_ORDER, Params.ORDER_ID);
}
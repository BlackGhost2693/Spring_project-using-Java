package kristian9577.cardealer.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Offer with this vehicle already exists.")
public class OfferWithThisVehicleAlreadyExistsException extends RuntimeException {
    private int statusCode;

    public OfferWithThisVehicleAlreadyExistsException() {
        this.statusCode = 409;
    }

    public OfferWithThisVehicleAlreadyExistsException(String message) {
        super(message);
        this.statusCode = 409;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

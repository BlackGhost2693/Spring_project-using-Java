package kristian9577.cardealer.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Vehicle not found!")
public class VehicleNotFoundException extends RuntimeException {
    private int statusCode;

    public VehicleNotFoundException() {
        this.statusCode = 404;
    }

    public VehicleNotFoundException(String message) {
        super(message);
        this.statusCode = 404;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

package kristian9577.cardealer.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Offer not found!")
public class OfferNotFoundException extends RuntimeException {
    private int statusCode;

    public OfferNotFoundException() {
        this.statusCode = 404;
    }

    public OfferNotFoundException(String message) {
        super(message);
        this.statusCode = 404;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

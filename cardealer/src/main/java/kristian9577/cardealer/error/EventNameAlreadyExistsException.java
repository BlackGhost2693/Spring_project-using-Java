package kristian9577.cardealer.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Event with this name exists.")
public class EventNameAlreadyExistsException extends RuntimeException {
    private int statusCode;

    public EventNameAlreadyExistsException() {
        this.statusCode = 409;
    }

    public EventNameAlreadyExistsException(String message) {
        super(message);
        this.statusCode = 409;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

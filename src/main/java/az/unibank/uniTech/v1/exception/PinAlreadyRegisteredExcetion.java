package az.unibank.uniTech.v1.exception;

public class PinAlreadyRegisteredExcetion extends RuntimeException {
    public PinAlreadyRegisteredExcetion(String message) {
        super(message);
    }
}

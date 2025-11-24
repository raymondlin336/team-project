package use_case.quote.data_access_interface;

public class QuoteRetrievalException extends Exception {
    public QuoteRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}

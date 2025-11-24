package use_case.quote.data_access;

public class QuoteRetrievalException extends Exception {
    public QuoteRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}

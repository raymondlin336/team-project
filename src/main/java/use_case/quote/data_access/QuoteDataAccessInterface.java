package use_case.quote.data_access;

public interface QuoteDataAccessInterface {
    Quote fetchQuote() throws QuoteRetrievalException;
}


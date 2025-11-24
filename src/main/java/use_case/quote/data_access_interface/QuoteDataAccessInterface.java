package use_case.quote.data_access_interface;

import entity.Quote;

public interface QuoteDataAccessInterface {
    Quote fetchQuote() throws QuoteRetrievalException;
}


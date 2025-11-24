package use_case.quote.data_access;

import entity.Quote;

public interface QuoteDataAccessInterface {
    Quote fetchQuote() throws QuoteRetrievalException;
}


package use_case.quote.output;

public interface GetDailyQuoteOutputBoundary {
    void presentSuccess(GetDailyQuoteOutputData outputData);
    void presentFailure(String errorMessage);
}

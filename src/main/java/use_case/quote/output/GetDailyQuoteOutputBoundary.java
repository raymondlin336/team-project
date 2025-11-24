package use_case.quote;

public interface GetDailyQuoteOutputBoundary {
    void presentSuccess(GetDailyQuoteOutputData outputData);
    void presentFailure(String errorMessage);
}

package use_case.quote.interactor;

import entity.Quote;
import use_case.quote.data_access_interface.QuoteDataAccessInterface;
import use_case.quote.data_access_interface.QuoteRetrievalException;
import use_case.quote.input.GetDailyQuoteInputBoundary;
import use_case.quote.input.GetDailyQuoteInputData;
import use_case.quote.output.GetDailyQuoteOutputBoundary;
import use_case.quote.output.GetDailyQuoteOutputData;

public class GetDailyQuoteInteractor implements GetDailyQuoteInputBoundary {

    private final QuoteDataAccessInterface quoteDataAccess;
    private final GetDailyQuoteOutputBoundary presenter;

    public GetDailyQuoteInteractor(QuoteDataAccessInterface quoteDataAccess,
                                   GetDailyQuoteOutputBoundary presenter) {
        this.quoteDataAccess = quoteDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(GetDailyQuoteInputData inputData) {
        try {
            // Pass the forceUpdate flag from inputData to the DAO
            Quote quote = quoteDataAccess.fetchQuote(inputData.isForceUpdate());

            GetDailyQuoteOutputData outputData =
                    new GetDailyQuoteOutputData(quote.getText(), quote.getAuthor());
            presenter.presentSuccess(outputData);
        } catch (QuoteRetrievalException e) {
            presenter.presentFailure("Couldn't load a quote right now. Take a deep breath anyway 🙂");
        }
    }
}

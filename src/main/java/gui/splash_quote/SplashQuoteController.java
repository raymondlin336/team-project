package gui.splash_quote;

import use_case.quote.input.GetDailyQuoteInputBoundary;
import use_case.quote.input.GetDailyQuoteInputData;

public class SplashQuoteController {

    private final GetDailyQuoteInputBoundary interactor;

    public SplashQuoteController(GetDailyQuoteInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void loadQuote() {
        interactor.execute(new GetDailyQuoteInputData());
    }


}

package gui.splash_quote;

import use_case.quote.output.GetDailyQuoteOutputBoundary;
import use_case.quote.output.GetDailyQuoteOutputData;

public class SplashQuotePresenter implements GetDailyQuoteOutputBoundary {

    private final SplashQuoteViewModel viewModel;

    public SplashQuotePresenter(SplashQuoteViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSuccess(GetDailyQuoteOutputData outputData) {
        viewModel.setQuoteText(outputData.getText());
        viewModel.setQuoteAuthor(outputData.getAuthor());
        viewModel.setErrorMessage("");
    }

    @Override
    public void presentFailure(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
        viewModel.setQuoteText("Welcome back!");
        viewModel.setQuoteAuthor("");
    }
}

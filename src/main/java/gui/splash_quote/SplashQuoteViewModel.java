package gui.splash_quote;

public class SplashQuoteViewModel {

    private String quoteText = "Loading quote...";
    private String quoteAuthor = "";
    private String errorMessage = "";

    public String getQuoteText() { return quoteText; }
    public String getQuoteAuthor() { return quoteAuthor; }
    public String getErrorMessage() { return errorMessage; }

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }

    public void setQuoteAuthor(String quoteAuthor) {
        this.quoteAuthor = quoteAuthor;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

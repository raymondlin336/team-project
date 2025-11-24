package use_case.quote.output;

public class GetDailyQuoteOutputData {
    private final String text;
    private final String author;

    public GetDailyQuoteOutputData(String text, String author) {
        this.text = text;
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }
}

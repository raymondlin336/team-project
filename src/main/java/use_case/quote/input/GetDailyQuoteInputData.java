package use_case.quote.input;

public class GetDailyQuoteInputData {
    private final boolean forceUpdate;

    // Constructor with default option
    public GetDailyQuoteInputData() {
        this.forceUpdate = false;
    }

    // Constructor with specific option
    public GetDailyQuoteInputData(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }
}

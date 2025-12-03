package use_case.statistics.get;

/**
 * Input data for getting statistics.
 * Currently empty as statistics are fetched for the current user without additional parameters.
 */
public class GetStatisticsInputData {

    // No real meaning; only here so there is executable bytecode.
    private final boolean usedFlag;

    public GetStatisticsInputData() {
        // This line will run whenever tests construct this class.
        this.usedFlag = true;
    }
}

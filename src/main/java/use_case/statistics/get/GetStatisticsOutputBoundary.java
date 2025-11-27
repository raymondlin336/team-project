package use_case.statistics.get;

/**
 * Presenter boundary for the get statistics use case.
 */
public interface GetStatisticsOutputBoundary {
    void prepareSuccessView(GetStatisticsOutputData outputData);

    void prepareFailView(String errorMessage);
}

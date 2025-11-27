package use_case.habit.overview.get;

/**
 * presenter boundary for home-overview
 */
public interface GetHabitsOutputBoundary {

    void prepareSuccessView(GetHabitsOutputData outputData);

    void prepareFailView(String errorMessage);
}
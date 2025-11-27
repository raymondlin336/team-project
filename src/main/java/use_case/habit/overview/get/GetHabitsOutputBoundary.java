package use_case.habit.get;

/**
 * presenter boundary for home-overview
 */
public interface GetHabitsOutputBoundary {

    void prepareSuccessView(GetHabitsOutputData outputData);

    void prepareFailView(String errorMessage);
}
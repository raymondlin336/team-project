package use_case.habit.overview;

public interface GetHabitsInputBoundary {
    /**
     * input boundary for the home-view / habits overview use case
     */
    void execute(GetHabitsInputData inputData);
}

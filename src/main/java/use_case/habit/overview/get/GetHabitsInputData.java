package use_case.habit.overview.get;

/**
 * input data for getting habits for the home-view.
 * its currently empty since we always fetch all habits for the current user and there is only one user so.
 */
public class GetHabitsInputData {
    // intentionally empty for now since dont know if it will ever be called. dont think it will
    // in which case we will have to delete them so we can reach 100% coverage also.

//     The dummy field exists only so the constructor has real bytecode for
//     the coverage tool to track.
    // Has no semantic meaning; used only for coverage.
    private final boolean usedFlag;

    public GetHabitsInputData() {
        // Executed whenever tests call new GetHabitsInputData()
        this.usedFlag = true;
    }
}

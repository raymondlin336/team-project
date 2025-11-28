package gui.home;

public class HomeViewInterface {
    // gui/home/HomeViewInterface.java
    package gui.home;

    import entity.Habit;
    import java.util.List;

    public interface HomeViewInterface {
        void showTasks(List<Habit> habits);
        void refreshAll(); // if you want to re-render everything
    }
}

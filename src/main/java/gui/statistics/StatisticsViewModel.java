package gui.statistics;

import main.Habit;
import java.util.ArrayList;

public class StatisticsViewModel {
    private ArrayList<Habit> habits = new ArrayList<Habit>();
    public StatisticsViewModel(ArrayList<Habit> habits) {
        for (Habit habit : habits) {
            if (!(habit.freq == Habit.Freq.Once)) {
                this.habits.add(habit);
            }
        }
    }
    public int numOfTasks(){
        return this.habits.size();
    }
    public Habit getTask(int i){
        return this.habits.get(i);
    }
}

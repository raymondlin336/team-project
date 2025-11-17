package gui.home;

import gui.task.TaskController;
import main.Habit;

public class HomeViewController extends TaskController {
    public HomeViewController(Boolean log_messages) {
        super(log_messages);
    }
    public void markTaskDone(Boolean task_done, Habit habit) {
        /// Mark task done for the day/week/month
        if (habit.freq == Habit.Freq.Every_day) {
            /// Mark
        } else if (habit.freq == Habit.Freq.Every_week) {
            /// Mark done
        } else if (habit.freq == Habit.Freq.Every_month) {
            /// Mark done
        }

    }

    public void markTaskNotDone(Boolean task_done, Habit habit) {
        /// Mark task not done for the day/week/month
        if (habit.freq == Habit.Freq.Every_day) {
            /// Mark
        } else if (habit.freq == Habit.Freq.Every_week) {
            /// Mark done
        } else if (habit.freq == Habit.Freq.Every_month) {
            /// Mark done
        }

    }
}

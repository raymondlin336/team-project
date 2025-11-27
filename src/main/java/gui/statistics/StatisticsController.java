package gui.statistics;

import gui.ScreenManager;
import use_case.statistics.get.GetStatisticsInputData;
import use_case.statistics.get.GetStatisticsInteractor;

public class StatisticsController {
    private ScreenManager screenManager;
    private GetStatisticsInteractor getStatisticsInteractor;
    public void addScreenManager(ScreenManager screenManager, GetStatisticsInteractor getStatisticsInteractor) {
        this.screenManager = screenManager;
        this.getStatisticsInteractor = getStatisticsInteractor;
    }
    public void goHome(){
        screenManager.showHomeView();
        System.out.println("Statistics Controller called, go to home page.");
    }
    public void goSettings() {
        System.out.println("Statistics Controller called, go to settings page.");
    }
    public void updateStatistics(){
        getStatisticsInteractor.execute(new GetStatisticsInputData());
    }
}

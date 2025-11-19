package gui.statistics;

import gui.ScreenManager;

public class StatisticsController {
    private ScreenManager screenManager;
    public void addScreenManager(ScreenManager screenManager){
        this.screenManager = screenManager;
    }
    public void goHome(){
        screenManager.showHomeView();
        System.out.println("Statistics Controller called, go to home page.");
    }
    public void goSettings() {
        System.out.println("Statistics Controller called, go to settings page.");
    }
}

package entity;

public enum Freq {
    Once, Daily, Weekly, Monthly;

    public static String getType(Freq freq) {
        String type = "Once";
        if (freq.equals(Daily)) {
            type = "Day";
        } else if (freq.equals(Weekly)) {
            type = "Week";
        } else if (freq.equals(Monthly)) {
            type = "Month";
        }
        return type;
    }
}

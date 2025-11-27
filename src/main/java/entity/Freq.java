package entity;

import javax.management.RuntimeErrorException;

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

    public static Freq fromString(String s) {
        switch (s) {
            case "Once": {
                return Once;
            }
            case "Day": {
                return Daily;
            }
            case "Week": {
                return Weekly;
            }
            case "Month": {
                return Monthly;
            }
            default: {
                throw new RuntimeErrorException(null, "Could not parse String as Freq!");
            }
        }
    }
}

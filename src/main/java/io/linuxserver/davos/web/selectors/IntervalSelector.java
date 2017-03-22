package io.linuxserver.davos.web.selectors;

public enum IntervalSelector {

    MINS_1(1, "Every minute"),
    MINS_5(5, "Every 5 minutes"),
    MINS_15(15, "Every 15 minutes"), 
    MINS_30(30, "Every 30 minutes"), 
    EVERY_HOUR(60, "Every hour"),
    EVERY_2_HOURS(120, "Every two hours"), 
    TWICE_A_DAY(720, "Twice a day"), 
    EVERY_DAY(1440, "Once a day");
    
    public static final IntervalSelector[] ALL = { MINS_1, MINS_5, MINS_15, MINS_30, EVERY_HOUR, 
            EVERY_2_HOURS, TWICE_A_DAY, EVERY_DAY };
    
    private IntervalSelector(int minutes, String text) {
        this.minutes = minutes;
        this.text = text;
    }
    
    private final int minutes;
    private final String text;
    
    public int getMinutes() {
        return minutes;
    }
    
    public String getText() {
        return text;
    }
}


package com.dkramer.utils;

/**
 * Enum containing the various types of messages that the
 * logger can have.
 */
public enum MessageLevel {
    INFO(true),
    WARNING(true),
    ERROR(true),
    FATAL_ERROR(true),
    HEADER;   // will always be true to display important info!

    private boolean enabled;    // control if MessageLevel type is enabled for capture

    MessageLevel() {
        enabled = true;
    }

    MessageLevel(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     *
     * @return a String that shows which MessageLevels are enabled.
     */
    public static String getEnabled() {
        String s = String.format("MessageLevels Enabled: [%s:%b] \t [%s:%b] \t [%s:%b] \t [%s:%b]",
                INFO, INFO.isEnabled(), WARNING, WARNING.isEnabled(), ERROR,
                ERROR.isEnabled(), FATAL_ERROR, FATAL_ERROR.isEnabled());
        return s;
    }

    /**
     *
     * @return String representation of enum value
     */
    public String toString() {
        switch (this) {
            case INFO:
                return "INFO";
            case WARNING:
                return "WARNING";
            case ERROR:
                return "ERROR";
            case FATAL_ERROR:
                return "FATAL_ERROR";
            case HEADER:
                return "";
        }
        // we shouldn't ever reach this, unless a new MessageLevel has been added
        // and there is not a case to return the string version!
        return "Specified message level does not have a proper toString()!";
    }
}

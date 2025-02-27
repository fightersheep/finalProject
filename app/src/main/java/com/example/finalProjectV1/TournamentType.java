package com.example.finalProjectV1;
public enum TournamentType {
    SINGLE_ELIMINATION("Single Elimination"),
    DOUBLE_ELIMINATION("Double Elimination"),
    ROUND_ROBIN("Round Robin"),
    SWISS_SYSTEM("Swiss System"),
    LEAGUE("League");

    private final String displayName;

    TournamentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TournamentType fromString(String text) {
        for (TournamentType type : TournamentType.values()) {
            if (type.displayName.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return SINGLE_ELIMINATION; // Default value
    }
}


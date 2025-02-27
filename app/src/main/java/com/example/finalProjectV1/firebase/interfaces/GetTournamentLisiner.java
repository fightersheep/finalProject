package com.example.finalProjectV1.firebase.interfaces;

import com.example.finalProjectV1.classes.Tournament;

public interface GetTournamentLisiner {
    void onTournamentFound(Tournament tournament);
    void onError(String error);
}

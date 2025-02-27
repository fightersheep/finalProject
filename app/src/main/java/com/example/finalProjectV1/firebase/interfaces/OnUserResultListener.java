package com.example.finalProjectV1.firebase.interfaces;

import com.example.finalProjectV1.classes.FullUser;

public interface OnUserResultListener {
    FullUser onUserFound(FullUser fullUser);
    void onUserNotFound();
    void onSearchError(String error);
}

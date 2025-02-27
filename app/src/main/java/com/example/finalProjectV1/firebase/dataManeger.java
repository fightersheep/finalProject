package com.example.finalProjectV1.firebase;

import android.util.Log;

import com.example.finalProjectV1.classes.FullUser;

public class dataManeger {

    private static FullUser fullUser;
    public static FullUser getUser() {
        return fullUser;
    }

    public static void setUser(FullUser fullUser) {
        dataManeger.fullUser = fullUser.Clone();
        Log.i("user", "setUser: user added");
    }
}

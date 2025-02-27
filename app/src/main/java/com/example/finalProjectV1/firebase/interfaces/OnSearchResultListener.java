package com.example.finalProjectV1.firebase.interfaces;

import com.example.finalProjectV1.classes.FullUser;

import java.util.List;

    public interface OnSearchResultListener {
        void onSearchComplete(List<FullUser> fullUsers);
        void onSearchError(String error);
    }


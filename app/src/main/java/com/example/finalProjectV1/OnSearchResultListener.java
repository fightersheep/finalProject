package com.example.finalProjectV1;

import java.util.List;

    public interface OnSearchResultListener {
        void onSearchComplete(List<User> users);
        void onSearchError(String error);
    }


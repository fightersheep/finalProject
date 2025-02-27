package com.example.finalProjectV1.firebase.interfaces;

import java.util.List;

public interface OnFriendListListener {
    void onListReceived(List<String> stringList);
    void onError(String error);
    void onFriendAdded();
}

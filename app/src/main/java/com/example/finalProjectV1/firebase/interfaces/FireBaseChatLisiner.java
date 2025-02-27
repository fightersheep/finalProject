package com.example.finalProjectV1.firebase.interfaces;

import com.example.finalProjectV1.classes.message;

import java.util.List;

public interface FireBaseChatLisiner {
    void onChatsLoaded(List<message> messages);
    void onChatAdded();
    void onChatUpdated();
    void onChatDeleted();
    void onError(String error);
}

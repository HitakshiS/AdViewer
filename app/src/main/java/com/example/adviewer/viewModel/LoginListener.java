package com.example.adviewer.model;

public interface LoginListener {
    void onStarted();

    void onSuccess();

    void onFailure(String message);

}

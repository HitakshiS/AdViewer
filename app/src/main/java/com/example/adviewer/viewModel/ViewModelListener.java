package com.example.adviewer.viewModel;

public interface LoginListener {
    void onStarted();

    void onSuccess();

    void onFailure(String message);

    void navigate(String message);

}

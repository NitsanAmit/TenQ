package com.postpc.tenq.viewmodels;

public class ViewModelError {

    private final String errorMessage;

    public ViewModelError(String message) {
        this.errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

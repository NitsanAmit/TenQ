package com.postpc.tenq.services.player;

public class PlayerError {
    private PlayerErrorType type;
    private Throwable throwable;

    public PlayerError(PlayerErrorType type, Throwable throwable) {
        this.type = type;
        this.throwable = throwable;
    }

    public PlayerErrorType getType() {
        return type;
    }

    public Throwable getThrowable() {
        return throwable;
    }

}

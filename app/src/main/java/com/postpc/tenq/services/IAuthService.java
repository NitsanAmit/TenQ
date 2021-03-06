package com.postpc.tenq.services;

import com.postpc.tenq.models.Room;
import com.postpc.tenq.models.User;

public interface IAuthService {

    User getCurrentUser();

    String getCurrentUserId();

    void saveCurrentUser(User user);

    void removeCurrentUser();

    boolean isCurrentUserHost(Room room);

}

package com.postpc.tenq.services;

import com.postpc.tenq.models.UserProfile;

public interface IAuthService {

    UserProfile getCurrentUser();

    String getCurrentUserId();

    void saveCurrentUser(UserProfile user);

    void removeCurrentUser();

}

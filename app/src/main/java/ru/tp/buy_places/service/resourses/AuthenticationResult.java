package ru.tp.buy_places.service.resourses;

/**
 * Created by Ivan on 16.07.2015.
 */
public class AuthenticationResult implements Resource {
    private final long mId;
    private final String mUsername;

    public AuthenticationResult(long id, String username) {
        mId = id;
        mUsername = username;
    }

    public long getId() {
        return mId;
    }

    public String getUsername() {
        return mUsername;
    }
}

package ru.tp.buy_places;

import ru.tp.buy_places.resourses.Resource;

/**
 * Created by Ivan on 22.04.2015.
 */
public class Response {
    private final int status;
    private final String message;
    private final Resource data;

    public Response(int status, String message, Resource data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Resource getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }
}
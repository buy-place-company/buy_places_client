package ru.tp.buy_places;

/**
 * Created by Ivan on 20.04.2015.
 */
public class RestMethodResult<T> {
    private int statusCode = 0;
    private String statusMsg;
    private T resource;

    public RestMethodResult(int statusCode, String statusMsg, T resource) {
        super();
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
        this.resource = resource;
    }


    public int getStatusCode() {
        return statusCode;
    }


    public String getStatusMsg() {
        return statusMsg;
    }


    public T getResource() {
        return resource;
    }
}

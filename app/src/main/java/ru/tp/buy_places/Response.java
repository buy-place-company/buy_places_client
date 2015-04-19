package ru.tp.buy_places;

import java.util.List;
import java.util.Map;

/**
 * Created by Ivan on 20.04.2015.
 */
public class Response {
    public int status;

    /**
     * The HTTP headers received in the response
     */
    public Map<String, List<String>> headers;

    /**
     * The response body, if any
     */
    public byte[] body;

    protected Response(int status,  Map<String, List<String>> headers, byte[] body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }
}

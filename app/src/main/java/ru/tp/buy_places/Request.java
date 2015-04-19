package ru.tp.buy_places;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ivan on 20.04.2015.
 */
public class Request {
    private URI requestUri;
    private Map<String, List<String>> headers;
    private byte[] body;
    private RestMethodFactory.Method method;

    public Request(RestMethodFactory.Method method, URI requestUri, Map<String, List<String>> headers,
                   byte[] body) {
        super();
        this.method = method;
        this.requestUri = requestUri;
        this.headers = headers;
        this.body = body;
    }

    public RestMethodFactory.Method getMethod() {
        return method;
    }


    public URI getRequestUri() {
        return requestUri;
    }


    public Map<String, List<String>> getHeaders() {
        return headers;
    }


    public byte[] getBody() {
        return body;
    }


    public void addHeader(String key, List<String> value) {

        if (headers == null) {
            headers = new HashMap<String, List<String>>();
        }
        headers.put(key, value);
    }
}

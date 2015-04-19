package ru.tp.buy_places;

/**
 * Created by Ivan on 20.04.2015.
 */
public interface RestMethod<T extends Resource> {
    RestMethodResult<T> execute();
}

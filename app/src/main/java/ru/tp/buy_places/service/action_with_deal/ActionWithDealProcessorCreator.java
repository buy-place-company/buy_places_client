package ru.tp.buy_places.service.action_with_deal;

import android.content.Context;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

/**
 * Created by Ivan on 16.07.2015.
 */
public class ActionWithDealProcessorCreator extends ProcessorCreator {

    public ActionWithDealProcessorCreator(Context context, Processor.OnProcessorResultListener listener) {
        super(context, listener);
    }

    @Override
    public Processor createProcessor() {
        return null;
    }
}

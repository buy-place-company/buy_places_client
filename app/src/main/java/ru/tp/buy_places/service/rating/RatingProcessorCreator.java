package ru.tp.buy_places.service.rating;

import android.content.Context;

import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.ProcessorCreator;

/**
 * Created by Ivan on 20.07.2015.
 */
public class RatingProcessorCreator extends ProcessorCreator {

    private final long mLimit;
    private final long mOffset;

    public RatingProcessorCreator(Context context, Processor.OnProcessorResultListener listener, long limit, long offset) {
        super(context, listener);
        mLimit = limit;
        mOffset = offset;
    }

    @Override
    public Processor createProcessor() {
        return new RatingProcessor(mContext, mListener, mLimit, mOffset);
    }
}

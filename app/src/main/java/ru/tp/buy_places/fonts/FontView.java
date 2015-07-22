package ru.tp.buy_places.fonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import ru.tp.buy_places.R;

/**
 * Created by home on 22.07.2015.
 */
public class FontView extends TextView{
    public FontView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Fonts work as a combination of particular family and the style.
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Fonts);
        String family = a.getString(R.styleable.Fonts_fontFamily);
        int style = a.getInt(R.styleable.Fonts_android_textStyle, -1);
        a.recycle();

        // Set the typeface based on the family and the style combination.
        setTypeface(FontManager.getInstance().get(family, style));
    }
}

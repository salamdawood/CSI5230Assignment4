package edu.csi5230.salamdawood.csi5230assignment4;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by salamdawood on 9/26/17.
 */

public class TTTButton extends Button implements Observer {
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    int index = 0;

    public TTTButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        String packageName = "http://schemas.android.com/apk/res-auto";
        index = attrs.getAttributeIntValue(packageName, "index", index);

    }

    @Override
    public void update(Observable observable, Object arg) {
        String symbol = (String) arg;
        setText(symbol);
    }
}

package org.selfconference.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import org.selfconference.android.R;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class DrawerView extends LinearLayout {
    public DrawerView(Context context) {
        super(context);
        init();
    }

    public DrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(LOLLIPOP)
    public DrawerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.view_drawer, this);
        setOrientation(VERTICAL);
    }
}

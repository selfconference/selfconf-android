package org.selfconference.android.settings;

import android.annotation.TargetApi;
import android.content.Context;
import android.preference.PreferenceCategory;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import org.selfconference.android.R;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static org.selfconference.android.utils.ResourceProvider.getColor;

public class SettingsCategoryHeader extends PreferenceCategory {

    public SettingsCategoryHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SettingsCategoryHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SettingsCategoryHeader(Context context) {
        super(context);
    }

    @TargetApi(LOLLIPOP) public SettingsCategoryHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override protected void onBindView(@NonNull View view) {
        super.onBindView(view);
        final TextView titleTextView = (TextView) view.findViewById(android.R.id.title);
        if (titleTextView != null) {
            titleTextView.setAllCaps(true);
            titleTextView.setTextColor(getColor(R.color.accent));
        }
    }
}

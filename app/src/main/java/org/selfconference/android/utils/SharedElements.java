package org.selfconference.android.utils;

import android.support.annotation.StringRes;
import android.support.v4.util.Pair;
import android.view.View;

import com.google.common.collect.Lists;

import java.util.List;

import static org.selfconference.android.utils.ResourceProvider.getString;

public class SharedElements {
    private final List<Pair<View, String>> sharedElements;

    private SharedElements(Builder builder) {
        sharedElements = builder.sharedElements;
    }

    public Pair<View, String>[] toArray() {
        return sharedElements.toArray(new Pair[sharedElements.size()]);
    }

    public static final class Builder {
        private final List<Pair<View, String>> sharedElements = Lists.newArrayList();

        public Builder add(View view, @StringRes int stringResId) {
            sharedElements.add(Pair.create(view, getString(stringResId)));
            return this;
        }

        public SharedElements build() {
            return new SharedElements(this);
        }
    }
}

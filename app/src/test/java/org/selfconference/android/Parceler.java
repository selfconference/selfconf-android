package org.selfconference.android;

import android.os.Parcel;
import android.os.Parcelable;

public final class Parceler {

    public static <T extends Parcelable> Container<T> testParceling(T object, Parcelable.Creator<T> creator) {
        final Parcel parcel = Parcel.obtain();
        object.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        final T parceled = creator.createFromParcel(parcel);

        return new Container<>(object, parceled);
    }

    private Parceler() {
        throw new AssertionError("No instances.");
    }

    public static final class Container<T extends Parcelable> {
        public final T original;
        public final T parceled;

        public Container(T original, T parceled) {
            this.original = original;
            this.parceled = parceled;
        }
    }
}

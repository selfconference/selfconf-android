package org.selfconference.android.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Objects.equal;

public class Room implements Parcelable {
    private final int id;
    private final String name;

    public static Room emptyRoom() {
        return Room.builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    private Room(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
    }

    private Room(Parcel parcel) {
        this.id = parcel.readInt();
        this.name = parcel.readString();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return equal(id, room.id) &&
                equal(name, room.name);
    }

    @Override public int hashCode() {
        return Objects.hashCode(id, name);
    }

    @Override public String toString() {
        return toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .toString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public static final Parcelable.Creator<Room> CREATOR = new Creator<Room>() {
        @Override public Room createFromParcel(Parcel source) {
            return new Room(source);
        }

        @Override public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    public static final class Builder {
        private int id;
        private String name = "";

        private Builder() {
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Room build() {
            return new Room(this);
        }
    }
}

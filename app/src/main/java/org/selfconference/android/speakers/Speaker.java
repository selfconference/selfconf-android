package org.selfconference.android.speakers;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.Objects;

import org.selfconference.android.session.Session;

import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Objects.equal;
import static com.google.common.collect.Lists.newArrayList;

public class Speaker implements Parcelable {
    private final int id;
    private final String name;
    private final String twitter;
    private final String bio;
    private final String photo;
    private final List<Session> sessions;

    public static Builder builder() {
        return new Builder();
    }

    private Speaker(Builder builder) {
        id = builder.id;
        name = builder.name;
        twitter = builder.twitter;
        bio = builder.bio;
        photo = builder.photo;
        sessions = builder.sessions;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getBio() {
        return bio;
    }

    public String getPhoto() {
        return photo;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    @Override public String toString() {
        return toStringHelper(this)
                .add("twitter", twitter)
                .add("id", id)
                .add("name", name)
                .add("sessions", sessions)
                .toString();
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Speaker that = (Speaker) o;

        return equal(this.id, that.id) &&
                equal(this.name, that.name) &&
                equal(this.twitter, that.twitter) &&
                equal(this.bio, that.bio) &&
                equal(this.photo, that.photo) &&
                equal(this.sessions, that.sessions);
    }

    @Override public int hashCode() {
        return Objects.hashCode(id, name, twitter, bio, photo, sessions);
    }

    public static final class Builder {
        private int id = -1;
        private String name = "";
        private String twitter = "";
        private String bio = "";
        private String photo = "";
        private List<Session> sessions = newArrayList();

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

        public Builder twitter(String twitter) {
            this.twitter = twitter;
            return this;
        }

        public Builder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public Builder photo(String photo) {
            this.photo = photo;
            return this;
        }

        public Builder sessions(List<Session> sessions) {
            this.sessions = sessions;
            return this;
        }

        public Speaker build() {
            return new Speaker(this);
        }
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.twitter);
        dest.writeString(this.bio);
        dest.writeString(this.photo);
        dest.writeList(this.sessions);
    }

    private Speaker(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.twitter = in.readString();
        this.bio = in.readString();
        this.photo = in.readString();
        this.sessions = newArrayList();
        in.readList(this.sessions, Integer.class.getClassLoader());
    }

    public static final Creator<Speaker> CREATOR = new Creator<Speaker>() {
        @Override public Speaker createFromParcel(Parcel source) {
            return new Speaker(source);
        }

        @Override public Speaker[] newArray(int size) {
            return new Speaker[size];
        }
    };
}

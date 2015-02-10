package org.selfconference.android.schedule;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.lang.reflect.Type;

public class Session implements Parcelable {
    private final String name;
    private final String title;
    private final String anchor;
    private final String room;
    private final DateTime beginning;
    private final int slots;

    private Session(Builder builder) {
        name = builder.name;
        title = builder.title;
        anchor = builder.anchor;
        room = builder.room;
        beginning = builder.beginning;
        slots = builder.slots;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getAnchor() {
        return anchor;
    }

    public String getRoom() {
        return room;
    }

    public DateTime getBeginning() {
        return beginning;
    }

    public int getSlots() {
        return slots;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.title);
        dest.writeString(this.anchor);
        dest.writeString(this.room);
        dest.writeLong(this.beginning.getMillis());
        dest.writeInt(this.slots);
    }

    private Session(Parcel in) {
        this.name = in.readString();
        this.title = in.readString();
        this.anchor = in.readString();
        this.room = in.readString();
        this.beginning = new DateTime(in.readLong());
        this.slots = in.readInt();
    }

    public static final Parcelable.Creator<Session> CREATOR = new Parcelable.Creator<Session>() {
        public Session createFromParcel(@NonNull Parcel source) {
            return new Session(source);
        }

        @NonNull
        public Session[] newArray(int size) {
            return new Session[size];
        }
    };

    public static final class Builder {
        private String name = "";
        private String title = "";
        private String anchor = "";
        private String room = "";
        private DateTime beginning = DateTime.now();
        private int slots;

        public Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder anchor(String anchor) {
            this.anchor = anchor;
            return this;
        }

        public Builder room(String room) {
            this.room = room;
            return this;
        }

        public Builder beginning(DateTime beginning) {
            this.beginning = beginning;
            return this;
        }

        public Builder slots(int slots) {
            this.slots = slots;
            return this;
        }

        public Session build() {
            return new Session(this);
        }
    }

    public static class Deserializer implements JsonDeserializer<Session> {
        @Override
        public Session deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            final JsonObject jsonObject = json.getAsJsonObject();
            final JsonElement name = jsonObject.get("name");
            final JsonElement title = jsonObject.get("title");
            final JsonElement room = jsonObject.get("room");
            final JsonElement beginning = jsonObject.get("beginning");
            final JsonElement anchor = jsonObject.get("anchor");
            final JsonElement slots = jsonObject.get("slots");
            DateTime dateTime;
            try {
                dateTime = DateTime.parse(beginning.getAsString(), DateTimeFormat.forPattern("MM/dd/YY HH:mm:ss"));
            } catch (Exception e) {
                dateTime = DateTime.now();
            }
            return new Builder()
                    .name(name.getAsString())
                    .title(title.getAsString())
                    .room(room.getAsString())
                    .beginning(dateTime)
                    .anchor(anchor.getAsString())
                    .slots(slots.getAsInt())
                    .build();
        }
    }
}

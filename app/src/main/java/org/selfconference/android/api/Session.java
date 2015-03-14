package org.selfconference.android.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.Objects;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.joda.time.DateTime;
import org.selfconference.android.utils.DateTimeHelper;

import java.lang.reflect.Type;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Objects.equal;
import static com.google.common.collect.Lists.newArrayList;
import static org.joda.time.DateTime.now;

public class Session implements Parcelable {
    private final int id;
    private final String title;
    private final String room;
    private final String description;
    private final DateTime beginning;
    private final List<Integer> speakers;

    private Session(Builder builder) {
        id = builder.id;
        title = builder.title;
        room = builder.room;
        description = builder.description;
        beginning = builder.beginning;
        speakers = builder.speakers;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getRoom() {
        return room;
    }

    public String getDescription() {
        return description;
    }

    public DateTime getBeginning() {
        return beginning;
    }

    public List<Integer> getSpeakerIds() {
        return speakers;
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("id", id)
                .add("title", title)
                .add("room", room)
                .add("beginning", beginning)
                .add("speakers", speakers)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session that = (Session) o;

        return equal(this.id, that.id) &&
                equal(this.title, that.title) &&
                equal(this.room, that.room) &&
                equal(this.description, that.description) &&
                equal(this.beginning, that.beginning) &&
                equal(this.speakers, that.speakers);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, title, room, description, beginning, speakers);
    }

    public static class Deserializer implements JsonDeserializer<Session> {
        @Override
        public Session deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            final JsonObject jsonObject = json.getAsJsonObject();
            final JsonElement id = jsonObject.get("id");
            final JsonElement title = jsonObject.get("title");
            final JsonElement room = jsonObject.get("room");
            final JsonElement beginning = jsonObject.get("beginning");
            final JsonElement description = jsonObject.get("description");
            final JsonElement speakers = jsonObject.get("speakers");
            DateTime dateTime;
            try {
                dateTime = DateTimeHelper.parseDateTime(beginning.getAsString());
            } catch (Exception e) {
                dateTime = now().withZone(DateTimeHelper.EST);
            }
            final List<Integer> listOfSpeakers = new Gson().fromJson(speakers.getAsJsonArray(), new TypeToken<List<Integer>>(){}.getType());
            return new Builder()
                    .id(id.getAsInt())
                    .title(title.getAsString())
                    .room(room.getAsString())
                    .beginning(dateTime)
                    .description(description.getAsString())
                    .speakers(listOfSpeakers)
                    .build();
        }
    }

    public static final class Builder {
        private int id;
        private String title = "";
        private String room = "";
        private String description = "";
        private DateTime beginning = now();
        private List<Integer> speakers = newArrayList();

        public Builder() {
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder room(String room) {
            this.room = room;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder beginning(DateTime beginning) {
            this.beginning = beginning;
            return this;
        }

        public Builder speakers(List<Integer> speakers) {
            this.speakers = speakers;
            return this;
        }

        public Session build() {
            return new Session(this);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.room);
        dest.writeString(this.description);
        dest.writeSerializable(this.beginning);
        dest.writeList(this.speakers);
    }

    private Session(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.room = in.readString();
        this.description = in.readString();
        this.beginning = (DateTime) in.readSerializable();
        this.speakers = newArrayList();
        in.readList(this.speakers, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Session> CREATOR = new Parcelable.Creator<Session>() {
        public Session createFromParcel(Parcel source) {
            return new Session(source);
        }

        public Session[] newArray(int size) {
            return new Session[size];
        }
    };
}

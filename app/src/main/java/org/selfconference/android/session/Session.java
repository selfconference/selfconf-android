package org.selfconference.android.session;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.Objects;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.joda.time.DateTime;
import org.selfconference.android.speakers.Speaker;
import org.selfconference.android.utils.DateTimeHelper;

import java.lang.reflect.Type;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Objects.equal;
import static com.google.common.collect.Lists.newArrayList;
import static org.joda.time.DateTime.now;
import static org.selfconference.android.session.Room.emptyRoom;

public class Session implements Parcelable {
    private final int id;
    private final String title;
    private final Room room;
    private final String description;
    private final boolean isKeynote;
    private final DateTime beginning;
    private final List<Speaker> speakers;

    public static Builder builder() {
        return new Builder();
    }

    private Session(Builder builder) {
        id = builder.id;
        title = builder.title;
        room = builder.room;
        description = builder.description;
        isKeynote = builder.isKeynote;
        beginning = builder.beginning;
        speakers = builder.speakers;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Room getRoom() {
        return room;
    }

    public String getDescription() {
        return description;
    }

    public boolean isKeynote() {
        return isKeynote;
    }

    public DateTime getBeginning() {
        return beginning;
    }

    public List<Speaker> getSpeakers() {
        return speakers;
    }

    @Override public String toString() {
        return toStringHelper(this)
                .add("id", id)
                .add("title", title)
                .add("room", room)
                .add("beginning", beginning)
                .add("speakers", speakers)
                .toString();
    }

    @Override public boolean equals(Object o) {
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

    @Override public int hashCode() {
        return Objects.hashCode(id, title, room, description, beginning, speakers);
    }

    public static class Deserializer implements JsonDeserializer<Session> {
        @Override public Session deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            final JsonObject jsonObject = json.getAsJsonObject();
            final JsonElement id = jsonObject.get("id");
            final JsonElement title = jsonObject.get("name");
            final JsonElement roomJsonObject = jsonObject.get("room");
            final JsonElement beginning = jsonObject.get("slot");
            final JsonElement description = jsonObject.get("abstract");
            final JsonElement speakers = jsonObject.get("speakers");
            final JsonElement keynote = jsonObject.get("keynote");
            final Room room = context.deserialize(roomJsonObject, Room.class);
            return new Builder()
                    .id(id.getAsInt())
                    .title(title.getAsString())
                    .room(room)
                    .beginning(determineDateTime(beginning))
                    .description(description.getAsString())
                    .isKeynote(determineKeynote(keynote))
                    .speakers(determineSpeakers(context, speakers))
                    .build();
        }

        private static List<Speaker> determineSpeakers(JsonDeserializationContext context, JsonElement jsonElement) {
            final List<Speaker> speakers = newArrayList();
            if (jsonElement == null) {
                return speakers;
            } else {
                final JsonArray jsonArray = jsonElement.getAsJsonArray();
                for (JsonElement element : jsonArray) {
                    final Speaker speaker = context.deserialize(element, Speaker.class);
                    speakers.add(speaker);
                }
                return speakers;
            }
        }

        private static DateTime determineDateTime(JsonElement jsonElement) {
            try {
                return DateTimeHelper.parseDateTime(jsonElement.getAsString());
            } catch (Exception e) {
                return now();
            }
        }

        private static boolean determineKeynote(JsonElement jsonElement) {
            return jsonElement != null &&
                    !jsonElement.isJsonNull() &&
                    jsonElement.getAsBoolean();
        }
    }

    public static final class Builder {
        private int id;
        private String title = "";
        private Room room = emptyRoom();
        private String description = "";
        private boolean isKeynote = false;
        private DateTime beginning = now();
        private List<Speaker> speakers = newArrayList();

        private Builder() {
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder room(Room room) {
            this.room = room;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder isKeynote(boolean isKeynote) {
            this.isKeynote = isKeynote;
            return this;
        }

        public Builder beginning(DateTime beginning) {
            this.beginning = beginning;
            return this;
        }

        public Builder speakers(List<Speaker> speakers) {
            this.speakers = speakers;
            return this;
        }

        public Session build() {
            return new Session(this);
        }
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeParcelable(this.room, 0);
        dest.writeString(this.description);
        dest.writeInt(this.isKeynote ? 1 : 0);
        dest.writeSerializable(this.beginning);
        dest.writeList(this.speakers);
    }

    private Session(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.room = in.readParcelable(Room.class.getClassLoader());
        this.description = in.readString();
        this.isKeynote = in.readInt() == 1;
        this.beginning = (DateTime) in.readSerializable();
        this.speakers = newArrayList();
        in.readList(this.speakers, Speaker.class.getClassLoader());
    }

    public static final Parcelable.Creator<Session> CREATOR = new Parcelable.Creator<Session>() {
        @Override public Session createFromParcel(Parcel source) {
            return new Session(source);
        }

        @Override public Session[] newArray(int size) {
            return new Session[size];
        }
    };
}

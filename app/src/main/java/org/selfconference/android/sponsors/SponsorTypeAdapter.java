package org.selfconference.android.sponsors;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

public final class SponsorTypeAdapter extends TypeAdapter<Sponsor> {
    private static final Gson GSON = new Gson();
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LINK = "link";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_SPONSOR_LEVELS = "sponsor_levels";

    @Override public void write(JsonWriter out, Sponsor value) throws IOException {

    }

    @Override public Sponsor read(JsonReader in) throws IOException {
        final Sponsor.Builder builder = Sponsor.builder();
        final Set<SponsorLevel> sponsorLevelsSet = newHashSet();

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case KEY_ID:
                    builder.id(in.nextInt());
                    break;
                case KEY_NAME:
                    builder.name(in.nextString());
                    break;
                case KEY_LINK:
                    builder.link(in.nextString());
                    break;
                case KEY_PHOTO:
                    builder.photo(in.nextString());
                    break;
                case KEY_SPONSOR_LEVELS:
                    in.beginArray();
                    while (in.hasNext()) {
                        final SponsorLevel sponsorLevel = GSON.fromJson(in, SponsorLevel.class);
                        sponsorLevelsSet.add(sponsorLevel);
                    }
                    builder.sponsorLevels(newArrayList(sponsorLevelsSet));
                    in.endArray();
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();
        return builder.build();
    }
}

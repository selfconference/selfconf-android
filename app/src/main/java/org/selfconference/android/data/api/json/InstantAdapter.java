package org.selfconference.data.api.json;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import org.selfconference.util.Instants;
import org.threeten.bp.Instant;

public final class InstantAdapter {
  @ToJson String toJson(Instant instant) {
    return instant.toString();
  }

  @FromJson Instant fromJson(String json) {
    return Instants.fromEstString(json);
  }
}

package org.selfconference.android.data.api.json;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import org.selfconference.android.data.api.model.Vote;

public final class VoteAdapter {
  @ToJson String toJson(Vote vote) {
    return String.valueOf(vote.toInt());
  }

  @FromJson Vote fromJson(String json) {
    Integer asInt = Integer.valueOf(json);
    return Vote.parseInt(asInt);
  }
}

package org.selfconference.android.data;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import java.util.List;
import org.selfconference.android.data.api.model.Room;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Slot;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.ui.session.Day;
import org.selfconference.android.ui.session.SessionAdapter;
import org.selfconference.android.util.Instants;
import org.threeten.bp.Instant;
import rx.Observable;
import rx.functions.Func1;

public final class Sessions {

  public static Func1<List<Session>, Observable<? extends List<Session>>> sortedForDay(Day day) {
    return sessions -> Observable.from(sessions) //
        .filter(session -> {
          Slot slot = Optional.fromNullable(session.slot()).or(Slot.empty());
          return Instants.areOnSameDay(slot.time(), day.getStartTime());
        }) //
        .toSortedList((session, session2) -> {
          Slot s1 = Optional.fromNullable(session.slot()).or(Slot.empty());
          Slot s2 = Optional.fromNullable(session2.slot()).or(Slot.empty());
          return s1.compareTo(s2);
        });
  }

  public static Func1<List<Session>, ImmutableListMultimap<Instant, Session>> groupBySlotTime() {
    return sessions -> Multimaps.index(sessions, session -> {
      return Optional.fromNullable(session.slot()).or(Slot.empty()).time();
    });
  }

  public static Func1<ImmutableListMultimap<Instant, Session>, Observable<? extends List<SessionAdapter.ViewModel>>> toViewModels() {
    return sessions -> Observable.from(sessions.asMap().entrySet()) //
        .map(entry -> {
          ImmutableList.Builder<SessionAdapter.ViewModel> models = ImmutableList.builder();
          models.add(SessionAdapter.Header.withText(Instants.miniTimeString(entry.getKey())));
          models.addAll(Collections2.transform(entry.getValue(), session -> {
            List<Speaker> speakers =
                Optional.fromNullable(session.speakers()).or(ImmutableList.of());
            List<String> speakerNames = Lists.transform(speakers, Speaker::name);
            Room room = Optional.fromNullable(session.room()).or(Room.empty());
            return SessionAdapter.ListItem.builder()
                .session(session)
                .lineOne(session.name())
                .lineTwo(Joiner.on(" / ").join(speakerNames))
                .lineThree(room.name())
                .build();
          }));
          return models.build();
        }) //
        .concatMapIterable(Funcs.identity()) //
        .toList();
  }

  private Sessions() {
    throw new AssertionError("No instances.");
  }
}

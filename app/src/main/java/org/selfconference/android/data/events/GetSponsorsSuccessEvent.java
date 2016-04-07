package org.selfconference.android.data.events;

import com.google.common.collect.ImmutableList;
import org.selfconference.android.sponsors.Sponsor;

public final class GetSponsorsSuccessEvent {

  public final ImmutableList<Sponsor> sponsors;

  public GetSponsorsSuccessEvent(ImmutableList<Sponsor> sponsors) {
    this.sponsors = sponsors;
  }
}

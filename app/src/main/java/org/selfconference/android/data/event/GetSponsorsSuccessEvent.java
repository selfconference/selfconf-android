package org.selfconference.android.data.event;

import com.google.common.collect.ImmutableList;
import org.selfconference.android.data.api.model.Sponsor;

/** Posts when a {@link org.selfconference.android.data.job.GetSponsorsJob} succeeds. */
public final class GetSponsorsSuccessEvent {

  public final ImmutableList<Sponsor> sponsors;

  public GetSponsorsSuccessEvent(ImmutableList<Sponsor> sponsors) {
    this.sponsors = sponsors;
  }
}

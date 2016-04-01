package org.selfconference.android.sponsors;

import com.google.common.collect.ComparisonChain;
import java.util.Collections;
import java.util.Comparator;

/**
 * A {@link Comparator} used to compare two {@link Sponsor}s in the following order:
 *
 * <ul>
 *  <li>Orders by lowest {@link SponsorLevel#order()}, which indicates the highest level
 * of sponsorship. Ex: <strong>Platinum</strong> has an order of 1, <strong>Gold</strong> has an
 * order of 2, etc.</li>
 *  <li>If the two sponsors share the lowest common denominator {@link SponsorLevel#order()},
 *  they are ordered alphabetically using {@link SponsorLevel#name()}.</li>
 * </ul>
 */
final class SponsorComparator implements Comparator<Sponsor> {
  @Override public int compare(Sponsor lhs, Sponsor rhs) {
    SponsorLevel lhsMin = Collections.min(lhs.sponsorLevels());
    SponsorLevel rhsMin = Collections.min(rhs.sponsorLevels());
    int result = ComparisonChain.start() //
        .compare(lhsMin, rhsMin) //
        .result();
    if (result == 0) {
      return ComparisonChain.start() //
          .compare(lhs.name(), rhs.name()) //
          .result();
    }
    return result;
  }
}

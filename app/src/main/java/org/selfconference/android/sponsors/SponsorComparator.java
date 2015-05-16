package org.selfconference.android.sponsors;

import com.google.common.collect.ComparisonChain;

import java.util.Collections;
import java.util.Comparator;

final class SponsorComparator implements Comparator<Sponsor> {
    @Override public int compare(Sponsor lhs, Sponsor rhs) {
        final SponsorLevel lhsMin = Collections.min(lhs.getSponsorLevels());
        final SponsorLevel rhsMin = Collections.min(rhs.getSponsorLevels());
        final int result = ComparisonChain.start()
                .compare(lhsMin, rhsMin)
                .result();
        if (result == 0) {
            return ComparisonChain.start()
                    .compare(lhs.getName(), rhs.getName())
                    .result();
        }
        return result;
    }
}

package org.selfconference.android.brand;

import com.google.common.base.Objects;

import org.selfconference.android.R;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Objects.equal;
import static java.lang.Integer.toHexString;
import static org.selfconference.android.utils.ResourceProvider.getColor;

public final class BrandColor {
    private static final int[][] COLORS = new int[][]{
            {R.color.primary, R.color.primary_dark},
            {R.color.accent, R.color.accent_dark},
            {R.color.purple, R.color.purple_dark},
            {R.color.red, R.color.red_dark}
    };

    private final int primary;
    private final int secondary;

    public static BrandColor withIdentifier(final int id) {
        return new BrandColor(
                getPrimaryColorForPosition(id),
                getSecondaryColorForPosition(id)
        );
    }

    private BrandColor(int primary, int secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    public int getPrimary() {
        return primary;
    }

    public int getSecondary() {
        return secondary;
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BrandColor that = (BrandColor) o;
        return equal(primary, that.primary) &&
                equal(secondary, that.secondary);
    }

    @Override public int hashCode() {
        return Objects.hashCode(primary, secondary);
    }

    @Override public String toString() {
        return toStringHelper(this)
                .add("primary", toHexString(primary))
                .add("secondary", toHexString(secondary))
                .toString();
    }

    private static int getPrimaryColorForPosition(int position) {
        return getColor(COLORS[position % COLORS.length][0]);
    }

    private static int getSecondaryColorForPosition(int position) {
        return getColor(COLORS[position % COLORS.length][1]);
    }
}

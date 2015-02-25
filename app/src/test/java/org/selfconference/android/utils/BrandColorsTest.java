package org.selfconference.android.utils;

import android.content.Context;

import org.assertj.core.api.AbstractAssert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.util.Pair;
import org.selfconference.android.CustomTestRunner;
import org.selfconference.android.R;

import static java.lang.Integer.toHexString;
import static org.selfconference.android.ResourceUtils.getColor;
import static org.selfconference.android.utils.BrandColorsTest.BrandColorsAssert.assertThat;

@Ignore
@RunWith(CustomTestRunner.class)
@Config(emulateSdk = 18, manifest = "app/src/main/AndroidManifest.xml")
public class BrandColorsTest {

    @Test
    public void firstBrandColorIsRed() throws Exception {
        final Pair<Integer, Integer> colors = getColorsForPosition(0);
        assertThat(colors)
                .hasPrimaryColor(R.color.red)
                .hasSecondaryColor(R.color.red_dark);
    }

    @Test
    public void secondBrandColorIsOrange() throws Exception {
        final Pair<Integer, Integer> colors = getColorsForPosition(1);
        assertThat(colors)
                .hasPrimaryColor(R.color.accent)
                .hasSecondaryColor(R.color.accent_dark);
    }

    @Test
    public void thirdBrandColorIsGreen() throws Exception {
        final Pair<Integer, Integer> colors = getColorsForPosition(2);
        assertThat(colors)
                .hasPrimaryColor(R.color.primary)
                .hasSecondaryColor(R.color.primary_dark);
    }

    @Test
    public void fourthBrandColorIsPurple() throws Exception {
        final Pair<Integer, Integer> colors = getColorsForPosition(3);
        assertThat(colors)
                .hasPrimaryColor(R.color.purple)
                .hasSecondaryColor(R.color.purple_dark);
    }

    private static Pair<Integer, Integer> getColorsForPosition(int position) {
        final Context context = Robolectric.application;
        return Pair.create(
                BrandColors.getPrimaryColorForPosition(context, position),
                BrandColors.getSecondaryColorForPosition(context, position)
        );
    }

    static final class BrandColorsAssert extends AbstractAssert<BrandColorsAssert, Pair<Integer, Integer>> {
        protected BrandColorsAssert(Pair<Integer, Integer> actual) {
            super(actual, BrandColorsAssert.class);
        }

        public static BrandColorsAssert assertThat(Pair<Integer, Integer> colorPair) {
            return new BrandColorsAssert(colorPair);
        }

        public BrandColorsAssert hasPrimaryColor(int colorResId) {
            isNotNull();
            final int expectedColor = getColor(colorResId);
            if (actual.first != expectedColor) {
                failWithMessage("Expected primary color was <%s> but actual was <%s>", toHexString(expectedColor), toHexString(actual.first));
            }
            return this;
        }

        public BrandColorsAssert hasSecondaryColor(int colorResId) {
            isNotNull();
            final int expectedColor = getColor(colorResId);
            if (actual.second != expectedColor) {
                failWithMessage("Expected secondary color was <%s> but actual was <%s>", toHexString(expectedColor), toHexString(actual.second));
            }
            return this;
        }
    }
}
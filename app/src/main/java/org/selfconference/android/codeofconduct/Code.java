package org.selfconference.android.codeofconduct;

import android.support.annotation.StringRes;
import org.selfconference.android.R;

public enum Code {
  CODE_OF_CONDUCT(R.string.coc_main_title, R.string.coc_main_subtitle),
  SHORT_VERSION(R.string.coc_short_version_title, R.string.coc_short_version_subtitle),
  LONG_VERSION(R.string.coc_long_version_title, R.string.coc_long_version_subtitle),
  CONTACT_INFORMATION(R.string.coc_contact_information_title,
      R.string.coc_contact_information_subtitle);

  private final int title;
  private final int subtitle;

  Code(int title, int subtitle) {
    this.title = title;
    this.subtitle = subtitle;
  }

  @StringRes public int getTitle() {
    return title;
  }

  @StringRes public int getSubtitle() {
    return subtitle;
  }
}

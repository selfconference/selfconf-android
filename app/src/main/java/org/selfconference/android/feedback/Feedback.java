package org.selfconference.android.feedback;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.common.base.Objects;
import org.selfconference.android.feedback.VoteButton.Vote;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Objects.equal;

public final class Feedback implements Parcelable {
  @Vote private final int vote;
  private final String comments;

  public Feedback(@Vote int vote, String comments) {
    this.vote = vote;
    this.comments = comments;
  }

  private Feedback(Parcel parcel) {
    @Vote int vote = parcel.readInt();
    this.vote = vote;
    this.comments = parcel.readString();
  }

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Feedback feedback = (Feedback) o;
    return equal(vote, feedback.vote) && equal(comments, feedback.comments);
  }

  @Override public int hashCode() {
    return Objects.hashCode(vote, comments);
  }

  @Override public String toString() {
    return toStringHelper(this) //
        .add("vote", vote) //
        .add("comments", comments) //
        .toString();
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.vote);
    dest.writeString(this.comments);
  }

  public static final Creator<Feedback> CREATOR = new Creator<Feedback>() {
    @Override public Feedback createFromParcel(Parcel source) {
      return new Feedback(source);
    }

    @Override public Feedback[] newArray(int size) {
      return new Feedback[size];
    }
  };
}

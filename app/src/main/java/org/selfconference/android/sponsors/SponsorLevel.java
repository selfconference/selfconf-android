package org.selfconference.android.sponsors;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Objects.equal;

public final class SponsorLevel implements Parcelable, Comparable<SponsorLevel> {
  private final int id;
  private final String name;
  private final int order;

  public static Builder builder() {
    return new Builder();
  }

  private SponsorLevel(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.order = builder.order;
  }

  private SponsorLevel(Parcel parcel) {
    this.id = parcel.readInt();
    this.name = parcel.readString();
    this.order = parcel.readInt();
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getOrder() {
    return order;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeString(this.name);
    dest.writeInt(this.order);
  }

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SponsorLevel that = (SponsorLevel) o;
    return equal(id, that.id) &&
        equal(order, that.order) &&
        equal(name, that.name);
  }

  @Override public int hashCode() {
    return Objects.hashCode(id, name, order);
  }

  @Override public String toString() {
    return toStringHelper(this) //
        .add("id", id) //
        .add("name", name) //
        .add("order", order) //
        .toString();
  }

  public static final Creator<SponsorLevel> CREATOR = new Creator<SponsorLevel>() {
    @Override public SponsorLevel createFromParcel(Parcel source) {
      return new SponsorLevel(source);
    }

    @Override public SponsorLevel[] newArray(int size) {
      return new SponsorLevel[size];
    }
  };

  @Override public int compareTo(@NonNull SponsorLevel that) {
    return ComparisonChain.start() //
        .compare(this.order, that.order) //
        .result();
  }

  public static final class Builder {
    private int id;
    private String name = "";
    private int order;

    private Builder() {
    }

    public Builder id(int id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder order(int order) {
      this.order = order;
      return this;
    }

    public SponsorLevel build() {
      return new SponsorLevel(this);
    }
  }
}

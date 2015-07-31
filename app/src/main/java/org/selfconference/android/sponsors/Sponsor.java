package org.selfconference.android.sponsors;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.common.base.Objects;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Objects.equal;
import static com.google.common.collect.Lists.newArrayList;

public class Sponsor implements Parcelable {
  private final int id;
  private final String name;
  private final String link;
  private final String photo;
  private final List<SponsorLevel> sponsorLevels;

  public static Builder builder() {
    return new Builder();
  }

  private Sponsor(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.link = builder.link;
    this.photo = builder.photo;
    this.sponsorLevels = builder.sponsorLevels;
  }

  private Sponsor(Parcel parcel) {
    this.id = parcel.readInt();
    this.name = parcel.readString();
    this.link = parcel.readString();
    this.photo = parcel.readString();
    this.sponsorLevels = newArrayList();
    parcel.readList(sponsorLevels, SponsorLevel.class.getClassLoader());
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getLink() {
    return link;
  }

  public String getPhoto() {
    return photo;
  }

  public List<SponsorLevel> getSponsorLevels() {
    return sponsorLevels;
  }

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Sponsor sponsor = (Sponsor) o;
    return equal(id, sponsor.id) &&
        equal(name, sponsor.name) &&
        equal(link, sponsor.link) &&
        equal(photo, sponsor.photo) &&
        equal(sponsorLevels, sponsor.sponsorLevels);
  }

  @Override public int hashCode() {
    return Objects.hashCode(id, name, link, photo, sponsorLevels);
  }

  @Override public String toString() {
    return toStringHelper(this) //
        .add("id", id)
        .add("name", name)
        .add("link", link)
        .add("photo", photo)
        .add("sponsorLevels", sponsorLevels)
        .toString();
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(name);
    dest.writeString(link);
    dest.writeString(photo);
    dest.writeList(sponsorLevels);
  }

  public static final Creator<Sponsor> CREATOR = new Creator<Sponsor>() {
    @Override public Sponsor createFromParcel(Parcel source) {
      return new Sponsor(source);
    }

    @Override public Sponsor[] newArray(int size) {
      return new Sponsor[size];
    }
  };

  public static final class Builder {
    private int id;
    private String name = "";
    private String link = "";
    private String photo = "";
    private List<SponsorLevel> sponsorLevels = newArrayList();

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

    public Builder link(String link) {
      this.link = link;
      return this;
    }

    public Builder photo(String photo) {
      this.photo = photo;
      return this;
    }

    public Builder sponsorLevels(List<SponsorLevel> sponsorLevels) {
      this.sponsorLevels = sponsorLevels;
      return this;
    }

    public Sponsor build() {
      return new Sponsor(this);
    }
  }
}

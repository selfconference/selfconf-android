package org.selfconference.android.ui.sponsor;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.squareup.picasso.Picasso;
import java.util.List;
import org.selfconference.android.App;
import org.selfconference.android.R;
import org.selfconference.android.data.api.model.Sponsor;
import org.selfconference.android.data.api.model.SponsorLevel;
import org.selfconference.android.ui.misc.ButterKnifeViewHolder;
import rx.subscriptions.CompositeSubscription;

public final class SponsorAdapter extends RecyclerView.Adapter<SponsorAdapter.ViewHolder> {
  public interface OnSponsorClickListener {
    void onSponsorClicked(final Sponsor sponsor);
  }

  private final Picasso picasso;
  private final CompositeSubscription compositeSubscription = new CompositeSubscription();
  private final List<Sponsor> sponsors = Lists.newArrayList();
  private OnSponsorClickListener onSponsorClickListener;

  public SponsorAdapter(Picasso picasso) {
    this.picasso = picasso;
  }

  public void setSponsors(List<Sponsor> sponsors) {
    this.sponsors.clear();
    this.sponsors.addAll(sponsors);
    notifyDataSetChanged();
  }

  public void setOnSponsorClickListener(OnSponsorClickListener sponsorClickListener) {
    this.onSponsorClickListener = sponsorClickListener;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.include_sponsor_row, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    Sponsor sponsor = sponsors.get(position);

    holder.itemView.setOnClickListener(v -> {
      if (onSponsorClickListener != null) {
        onSponsorClickListener.onSponsorClicked(sponsor);
      }
    });

    holder.itemView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
      @Override public boolean onPreDraw() {
        holder.itemView.getViewTreeObserver().removeOnPreDrawListener(this);

        picasso.load(sponsor.photo())
            .resize(holder.sponsorLogo.getWidth(), holder.sponsorLogo.getHeight())
            .centerInside()
            .into(holder.sponsorLogo);
        return true;
      }
    });

    holder.sponsorName.setText(sponsor.name());

    String formattedSponsorLevels = formattedSponsorLevels(sponsor);
    holder.sponsorType.setText(formattedSponsorLevels);
  }

  @Override public int getItemCount() {
    return sponsors.size();
  }

  @Override public void onViewDetachedFromWindow(ViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
    compositeSubscription.unsubscribe();
  }

  private String formattedSponsorLevels(Sponsor sponsor) {
    List<String> sponsorLevelNames = Lists.transform(sponsor.sponsor_levels(), SponsorLevel::name);
    String sponsorLevels = Joiner.on(", ").join(sponsorLevelNames);
    int numSponsorLevels = sponsor.sponsor_levels().size();
    Resources resources = App.context().getResources();
    return resources.getQuantityString(R.plurals.sponsor_levels, numSponsorLevels, sponsorLevels);
  }

  static final class ViewHolder extends ButterKnifeViewHolder {

    @BindView(R.id.sponsor_logo) ImageView sponsorLogo;
    @BindView(R.id.sponsor_name) TextView sponsorName;
    @BindView(R.id.sponsor_type) TextView sponsorType;

    ViewHolder(View itemView) {
      super(itemView);
    }
  }
}

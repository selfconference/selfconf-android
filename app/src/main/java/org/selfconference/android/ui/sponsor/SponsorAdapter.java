package org.selfconference.android.ui.sponsor;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.Locale;
import org.selfconference.android.App;
import org.selfconference.android.R;
import org.selfconference.android.data.api.model.Sponsor;
import org.selfconference.android.data.api.model.SponsorLevel;
import org.selfconference.android.ui.misc.ButterKnifeViewHolder;
import org.selfconference.android.ui.misc.FilterableAdapter;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

public class SponsorAdapter extends FilterableAdapter<Sponsor, SponsorAdapter.ViewHolder> {
  public interface OnSponsorClickListener {
    void onSponsorClicked(final Sponsor sponsor);
  }

  private final Picasso picasso;
  private final CompositeSubscription compositeSubscription = new CompositeSubscription();
  private OnSponsorClickListener onSponsorClickListener;

  public SponsorAdapter(Picasso picasso) {
    this.picasso = picasso;
  }

  public void setOnSponsorClickListener(OnSponsorClickListener sponsorClickListener) {
    this.onSponsorClickListener = sponsorClickListener;
  }

  @Override protected Func1<Sponsor, Boolean> filterPredicate(final String query) {
    return new Func1<Sponsor, Boolean>() {
      @Override public Boolean call(Sponsor sponsor) {
        return sponsor.name() //
            .toLowerCase(Locale.US) //
            .contains(query.toLowerCase(Locale.US));
      }
    };
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.include_sponsor_row, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(final ViewHolder holder, int position) {
    final Sponsor sponsor = getFilteredData().get(position);

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (onSponsorClickListener != null) {
          onSponsorClickListener.onSponsorClicked(sponsor);
        }
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

  @Override public void onViewDetachedFromWindow(ViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
    compositeSubscription.unsubscribe();
  }

  private String formattedSponsorLevels(Sponsor sponsor) {
    Function<SponsorLevel, String> sponsorLevelToName = new Function<SponsorLevel, String>() {
      @Override public String apply(SponsorLevel sponsorLevel) {
        return sponsorLevel.name();
      }
    };
    List<String> sponsorLevelNames = Lists.transform(sponsor.sponsor_levels(), sponsorLevelToName);
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

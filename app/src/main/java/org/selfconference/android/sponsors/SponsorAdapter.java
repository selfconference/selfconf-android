package org.selfconference.android.sponsors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import com.squareup.picasso.Picasso;
import java.util.Locale;
import javax.inject.Inject;
import org.selfconference.android.ButterKnifeViewHolder;
import org.selfconference.android.FilterableAdapter;
import org.selfconference.android.R;
import rx.Observable;
import rx.Observable.Transformer;
import rx.Subscription;
import rx.functions.Func1;
import rx.observables.StringObservable;
import rx.subscriptions.CompositeSubscription;

import static org.selfconference.android.utils.ResourceProvider.getQuantityString;

public class SponsorAdapter extends FilterableAdapter<Sponsor, SponsorAdapter.ViewHolder> {
  public interface OnSponsorClickListener {
    void onSponsorClicked(final Sponsor sponsor);
  }

  @Inject Picasso picasso;

  private final CompositeSubscription compositeSubscription = new CompositeSubscription();
  private OnSponsorClickListener onSponsorClickListener;

  public void setOnSponsorClickListener(OnSponsorClickListener sponsorClickListener) {
    this.onSponsorClickListener = sponsorClickListener;
  }

  @Override protected Func1<Sponsor, Boolean> filterPredicate(final String query) {
    return sponsor -> sponsor.getName() //
        .toLowerCase(Locale.US) //
        .contains(query.toLowerCase(Locale.US));
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.include_sponsor_row, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(final ViewHolder holder, int position) {
    final Sponsor sponsor = getFilteredData().get(position);

    holder.itemView.setOnClickListener(v -> {
      if (onSponsorClickListener != null) {
        onSponsorClickListener.onSponsorClicked(sponsor);
      }
    });

    holder.itemView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
      @Override public boolean onPreDraw() {
        holder.itemView.getViewTreeObserver().removeOnPreDrawListener(this);

        picasso.load(sponsor.getPhoto())
            .resize(holder.sponsorLogo.getWidth(), holder.sponsorLogo.getHeight())
            .centerInside()
            .into(holder.sponsorLogo);
        return true;
      }
    });

    holder.sponsorName.setText(sponsor.getName());
    final Subscription sponsorNameSubscription = Observable.from(sponsor.getSponsorLevels()) //
        .map(SponsorLevel::getName) //
        .compose(joinStringsWith(", ")) //
        .map(s -> {
          return getQuantityString(R.plurals.sponsor_levels, sponsor.getSponsorLevels().size(), s);
        }) //
        .subscribe(holder.sponsorType::setText);
    compositeSubscription.add(sponsorNameSubscription);
  }

  @Override public void onViewDetachedFromWindow(ViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
    compositeSubscription.unsubscribe();
  }

  private static Transformer<String, String> joinStringsWith(String delimiter) {
    return stringObservable -> StringObservable.join(stringObservable, delimiter);
  }

  static final class ViewHolder extends ButterKnifeViewHolder {

    @Bind(R.id.sponsor_logo) ImageView sponsorLogo;
    @Bind(R.id.sponsor_name) TextView sponsorName;
    @Bind(R.id.sponsor_type) TextView sponsorType;

    ViewHolder(View itemView) {
      super(itemView);
    }
  }
}

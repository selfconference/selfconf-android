package org.selfconference.android.sponsors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.InjectView;
import com.squareup.picasso.Picasso;
import java.util.Locale;
import javax.inject.Inject;
import org.selfconference.android.ButterKnifeViewHolder;
import org.selfconference.android.FilterableAdapter;
import org.selfconference.android.R;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
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
    return new Func1<Sponsor, Boolean>() {
      @Override public Boolean call(Sponsor sponsor) {
        return sponsor.getName() //
            .toLowerCase(Locale.US) //
            .contains(query.toLowerCase(Locale.US));
      }
    };
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.include_sponsor_row, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(final ViewHolder holder, int position) {
    final Sponsor sponsor = getFilteredData().get(position);

    holder.itemView.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        if (onSponsorClickListener != null) {
          onSponsorClickListener.onSponsorClicked(sponsor);
        }
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
    final Observable<String> sponsorNames = Observable.from(sponsor.getSponsorLevels()) //
        .map(new Func1<SponsorLevel, String>() {
          @Override public String call(SponsorLevel sponsorLevel) {
            return sponsorLevel.getName();
          }
        });

    final Subscription sponsorNameSubscription = StringObservable.join(sponsorNames, ", ") //
        .map(new Func1<String, String>() {
          @Override public String call(String s) {
            final int sponsorLevels = sponsor.getSponsorLevels().size();
            return getQuantityString(R.plurals.sponsor_levels, sponsorLevels, s);
          }
        }) //
        .subscribe(new Action1<String>() {
          @Override public void call(String s) {
            holder.sponsorType.setText(s);
          }
        });
    compositeSubscription.add(sponsorNameSubscription);
  }

  @Override public void onViewDetachedFromWindow(ViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
    compositeSubscription.unsubscribe();
  }

  public static final class ViewHolder extends ButterKnifeViewHolder {

    @InjectView(R.id.sponsor_logo) public ImageView sponsorLogo;
    @InjectView(R.id.sponsor_name) public TextView sponsorName;
    @InjectView(R.id.sponsor_type) public TextView sponsorType;

    public ViewHolder(View itemView) {
      super(itemView);
    }
  }
}

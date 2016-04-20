package org.selfconference.android.ui.speaker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import com.squareup.picasso.Picasso;
import java.util.Locale;
import org.selfconference.android.R;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.ui.misc.ButterKnifeViewHolder;
import org.selfconference.android.ui.misc.FilterableAdapter;
import org.selfconference.android.ui.transform.CircularTransformation;
import org.selfconference.android.util.PlaceholderDrawable;
import rx.functions.Func1;

import static android.text.Html.fromHtml;
import static android.view.View.GONE;
import static android.view.ViewTreeObserver.OnPreDrawListener;

public final class SpeakerAdapter
    extends FilterableAdapter<Speaker, SpeakerAdapter.SpeakerViewHolder> {
  public interface OnSpeakerClickListener {
    void onSpeakerClick(Speaker speaker);
  }

  private final Picasso picasso;
  private final boolean showDescription;

  private OnSpeakerClickListener onSpeakerClickListener;

  public SpeakerAdapter(Picasso picasso, boolean showDescription) {
    super();
    this.picasso = picasso;
    this.showDescription = showDescription;
  }

  public void setOnSpeakerClickListener(OnSpeakerClickListener onSpeakerClickListener) {
    this.onSpeakerClickListener = onSpeakerClickListener;
  }

  @Override protected Func1<Speaker, Boolean> filterPredicate(final String query) {
    return new Func1<Speaker, Boolean>() {
      @Override public Boolean call(Speaker speaker) {
        return speaker.name().toLowerCase(Locale.US).contains(query.toLowerCase(Locale.US));
      }
    };
  }

  @Override public SpeakerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.include_row_single_item_with_avatar, parent, false);
    return new SpeakerViewHolder(view);
  }

  @Override public void onBindViewHolder(final SpeakerViewHolder holder, int position) {
    final Speaker speaker = getFilteredData().get(position);

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (onSpeakerClickListener != null) {
          onSpeakerClickListener.onSpeakerClick(speaker);
        }
      }
    });
    holder.speakerName.setText(speaker.name());

    holder.itemView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
      @Override public boolean onPreDraw() {
        holder.itemView.getViewTreeObserver().removeOnPreDrawListener(this);

        picasso.load(speaker.photo())
            .resize(holder.speakerPhoto.getWidth(), holder.speakerPhoto.getHeight())
            .centerCrop()
            .transform(new CircularTransformation(speaker.photo()))
            .placeholder(PlaceholderDrawable.forId(speaker.id()))
            .into(holder.speakerPhoto);
        return true;
      }
    });
    if (showDescription) {
      holder.speakerDescription.setText(fromHtml(speaker.bio()));
    } else {
      holder.speakerDescription.setVisibility(GONE);
    }
  }

  static final class SpeakerViewHolder extends ButterKnifeViewHolder {

    @Bind(R.id.row_icon) ImageView speakerPhoto;
    @Bind(R.id.row_title) TextView speakerName;
    @Bind(R.id.row_subtitle) TextView speakerDescription;

    SpeakerViewHolder(View itemView) {
      super(itemView);
    }
  }
}

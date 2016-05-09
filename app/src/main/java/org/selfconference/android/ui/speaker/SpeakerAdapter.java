package org.selfconference.android.ui.speaker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
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
import static android.view.ViewTreeObserver.OnPreDrawListener;

public final class SpeakerAdapter
    extends FilterableAdapter<Speaker, SpeakerAdapter.SpeakerViewHolder> {
  public interface OnSpeakerClickListener {
    void onSpeakerClick(Speaker speaker);
  }

  private final Picasso picasso;

  private OnSpeakerClickListener onSpeakerClickListener;

  public SpeakerAdapter(Picasso picasso) {
    super();
    this.picasso = picasso;
  }

  public void setOnSpeakerClickListener(OnSpeakerClickListener onSpeakerClickListener) {
    this.onSpeakerClickListener = onSpeakerClickListener;
  }

  @Override protected Func1<Speaker, Boolean> filterPredicate(final String query) {
    return speaker -> speaker.name().toLowerCase(Locale.US).contains(query.toLowerCase(Locale.US));
  }

  @Override public SpeakerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.include_row_single_item_with_avatar, parent, false);
    return new SpeakerViewHolder(view);
  }

  @Override public void onBindViewHolder(final SpeakerViewHolder holder, int position) {
    final Speaker speaker = getFilteredData().get(position);

    holder.itemView.setOnClickListener(v -> {
      if (onSpeakerClickListener != null) {
        onSpeakerClickListener.onSpeakerClick(speaker);
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
    holder.speakerDescription.setText(fromHtml(speaker.bio()));
  }

  static final class SpeakerViewHolder extends ButterKnifeViewHolder {

    @BindView(R.id.row_icon) ImageView speakerPhoto;
    @BindView(R.id.row_title) TextView speakerName;
    @BindView(R.id.row_subtitle) TextView speakerDescription;

    SpeakerViewHolder(View itemView) {
      super(itemView);
    }
  }
}

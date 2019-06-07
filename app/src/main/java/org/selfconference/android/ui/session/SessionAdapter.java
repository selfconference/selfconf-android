package org.selfconference.ui.session;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.auto.value.AutoValue;
import com.google.common.collect.Lists;
import org.selfconference.R;
import org.selfconference.data.api.model.Session;
import org.selfconference.ui.misc.ButterKnifeViewHolder;
import java.util.List;
import butterknife.BindView;

public class SessionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  public interface OnSessionClickListener {
    void onSessionClick(Session event);
  }

  public interface OnSessionLongClickListener {
    void onSessionLongClick(Session session);
  }

  public interface ViewModel {
    ViewType viewType();
  }

  enum ViewType {
    HEADER,
    LIST_ITEM;

    static ViewType fromInt(int position) {
      return ViewType.values()[position];
    }
  }

  @AutoValue public static abstract class Header implements ViewModel {
    public static Header withText(String text) {
      return new AutoValue_SessionAdapter_Header(text);
    }

    public abstract String text();

    @Override public ViewType viewType() {
      return ViewType.HEADER;
    }
  }

  @AutoValue public static abstract class ListItem implements ViewModel {
    public static Builder builder() {
      return new AutoValue_SessionAdapter_ListItem.Builder();
    }

    public abstract Session session();

    public abstract String lineOne();

    public abstract String lineTwo();

    public abstract String lineThree();

    @Override public ViewType viewType() {
      return ViewType.LIST_ITEM;
    }

    @AutoValue.Builder public static abstract class Builder {
      public abstract Builder session(Session session);

      public abstract Builder lineOne(String lineOne);

      public abstract Builder lineTwo(String lineTwo);

      public abstract Builder lineThree(String lineThree);

      public abstract ListItem build();
    }
  }

  private final List<ViewModel> viewModels = Lists.newArrayList();

  private OnSessionClickListener onSessionClickListener;
  private OnSessionLongClickListener onSessionLongClickListener;

  public SessionAdapter() {
  }

  public void setViewModels(List<ViewModel> viewModels) {
    this.viewModels.clear();
    this.viewModels.addAll(viewModels);
    notifyDataSetChanged();
  }

  public void setOnSessionClickListener(OnSessionClickListener onSessionClickListener) {
    this.onSessionClickListener = onSessionClickListener;
  }

  public void setOnSessionLongClickListener(OnSessionLongClickListener onSessionLongClickListener) {
    this.onSessionLongClickListener = onSessionLongClickListener;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    ViewType type = ViewType.fromInt(viewType);
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    switch (type) {
      case HEADER:
        View headerView = inflater.inflate(R.layout.text_header, parent, false);
        return new HeaderViewHolder(headerView);
      case LIST_ITEM:
        View listItemView = inflater.inflate(R.layout.include_session_row, parent, false);
        return new ListItemViewHolder(listItemView);
      default:
        throw new IllegalArgumentException("Invalid ViewType: " + type);
    }
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    ViewModel viewModel = viewModels.get(position);
    ViewType viewType = viewModel.viewType();
    switch (viewType) {
      case HEADER:
        Header header = (Header) viewModel;
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        headerViewHolder.textView.setText(header.text());
        break;
      case LIST_ITEM:
        ListItem listItem = (ListItem) viewModel;
        ListItemViewHolder listItemViewHolder = (ListItemViewHolder) holder;
        listItemViewHolder.itemView.setOnClickListener(v -> {
          if (onSessionClickListener != null) {
            onSessionClickListener.onSessionClick(listItem.session());
          }
        });
        listItemViewHolder.itemView.setOnLongClickListener(v -> {
          if (onSessionLongClickListener != null) {
            onSessionLongClickListener.onSessionLongClick(listItem.session());
            return true;
          }
          return false;
        });
        listItemViewHolder.lineOne.setText(listItem.lineOne());
        listItemViewHolder.lineTwo.setText(listItem.lineTwo());
        listItemViewHolder.lineThree.setText(listItem.lineThree());
        break;
      default:
        throw new IllegalArgumentException("Invalid ViewType: " + viewType);
    }
  }

  @Override public int getItemViewType(int position) {
    return viewModels.get(position).viewType().ordinal();
  }

  @Override public int getItemCount() {
    return viewModels.size();
  }

  static final class HeaderViewHolder extends ButterKnifeViewHolder {
    @BindView(R.id.text) TextView textView;

    HeaderViewHolder(View itemView) {
      super(itemView);
    }
  }

  static final class ListItemViewHolder extends ButterKnifeViewHolder {
    @BindView(R.id.lineOne) TextView lineOne;
    @BindView(R.id.lineTwo) TextView lineTwo;
    @BindView(R.id.lineThree) TextView lineThree;

    ListItemViewHolder(View itemView) {
      super(itemView);
    }
  }
}

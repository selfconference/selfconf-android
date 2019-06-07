package org.selfconference.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.common.collect.ImmutableList;
import org.selfconference.R;
import org.selfconference.ui.decorator.IntentDecorator;
import org.selfconference.ui.misc.ButterKnifeViewHolder;
import org.selfconference.ui.viewmodel.TwoLineListItem;
import butterknife.BindView;
import butterknife.ButterKnife;

public final class ExternalIntentActivity extends AppCompatActivity {
  public static final String ACTION = "org.selfconference.intent.EXTERNAL_INTENT";
  public static final String EXTRA_BASE_INTENT = "debug_base_intent";

  public static Intent createIntent(Intent baseIntent) {
    Intent intent = new Intent();
    intent.setAction(ACTION);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.putExtra(EXTRA_BASE_INTENT, baseIntent);
    return intent;
  }

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.recycler_view) RecyclerView recyclerView;

  private Intent baseIntent;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.debug_external_intent_activity);
    ButterKnife.bind(this);

    toolbar.inflateMenu(R.menu.debug_external_intent);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
          case R.id.debug_launch:
            startActivity(baseIntent);
            finish();
            return true;
          default:
            return false;
        }
      }
    });

    baseIntent = getIntent().getParcelableExtra(EXTRA_BASE_INTENT);
    IntentDecorator intentDecorator = IntentDecorator.decorate(baseIntent);

    ImmutableList<TwoLineListItem> listItems = ImmutableList.<TwoLineListItem>builder() //
        .add(TwoLineListItem.create("Action", intentDecorator.action()))
        .add(TwoLineListItem.create("Data", intentDecorator.data()))
        .add(TwoLineListItem.create("Extras", intentDecorator.extras()))
        .add(TwoLineListItem.create("Flags", intentDecorator.flags()))
        .build();

    TwoLineListItemAdapter adapter = new TwoLineListItemAdapter(listItems);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
  }

  static final class TwoLineListItemAdapter
      extends RecyclerView.Adapter<TwoLineListItemViewHolder> {

    private final ImmutableList<TwoLineListItem> listItems;

    TwoLineListItemAdapter(ImmutableList<TwoLineListItem> listItems) {
      this.listItems = listItems;
    }

    @Override public TwoLineListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.debug_two_line_list_item, parent, false);
      return new TwoLineListItemViewHolder(view);
    }

    @Override public void onBindViewHolder(TwoLineListItemViewHolder holder, int position) {
      TwoLineListItem listItem = listItems.get(position);

      holder.lineOne.setText(listItem.lineOne());
      holder.lineTwo.setText(listItem.lineTwo());
    }

    @Override public int getItemCount() {
      return listItems.size();
    }
  }

  static final class TwoLineListItemViewHolder extends ButterKnifeViewHolder {

    @BindView(R.id.lineOne) TextView lineOne;
    @BindView(R.id.lineTwo) TextView lineTwo;

    TwoLineListItemViewHolder(View itemView) {
      super(itemView);
    }
  }
}

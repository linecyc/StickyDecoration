package com.linecy.stickydecoration;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by linecy.
 */
public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.NameViewHolder> {
  private List<String> list = new ArrayList<>();

  @NonNull @Override
  public NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new NameViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linear, parent, false));
  }

  @Override public void onBindViewHolder(@NonNull NameViewHolder holder, int position) {
    holder.bindData(list.get(position));
  }

  class NameViewHolder extends ViewHolder {
    private TextView name;

    public NameViewHolder(View itemView) {
      super(itemView);
      name = itemView.findViewById(R.id.textView);
    }

    void bindData(String str) {
      name.setText(str);
    }
  }

  @Override public int getItemCount() {
    return list.size();
  }

  public void refreshData(List<String> list) {
    this.list.clear();
    if (list != null && list.size() > 0) {
      this.list.addAll(list);
    }
    notifyDataSetChanged();
  }
}

package com.linecy.stickydecoration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.linecy.module.decoration.GroupInfo;
import com.linecy.module.decoration.LinearStickyDecoration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by linecy.
 */
public class LinearActivity extends AppCompatActivity {

  public static final String EXTRA_DATA = "extra_data";

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recycler);
    boolean isSticky = getIntent().getBooleanExtra(EXTRA_DATA, true);
    RecyclerView recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    LinearAdapter adapter = new LinearAdapter();
    recyclerView.setAdapter(adapter);
    LinearStickyDecoration stickyDecoration = new LinearStickyDecoration(this);
    stickyDecoration.setHeaderBackground(ContextCompat.getColor(this, R.color.black_translucent));
    stickyDecoration.setSticky(isSticky);
    recyclerView.addItemDecoration(stickyDecoration);

    adapter.refreshData(createData(40));
    List<GroupInfo> list = new ArrayList<>();
    list.add(new GroupInfo("第一组", 5));
    list.add(new GroupInfo("第二组", 10));
    list.add(new GroupInfo("第三组", 5));
    list.add(new GroupInfo("第四组", 20));

    stickyDecoration.setGroupInfo(list);
  }

  private List<String> createData(int size) {
    List<String> list = new ArrayList<>(size);
    for (int i = 1; i <= size; i++) {
      list.add("第 " + i + " 个");
    }
    return list;
  }
}

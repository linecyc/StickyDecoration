package com.linecy.module.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import java.util.List;

/**
 * @author by linecy.
 */
public class GridStickyDecoration extends LinearStickyDecoration {

  private int spanCount = -1;
  private GridLayoutManager.SpanSizeLookup spanSizeLookup;
  private boolean needRefresh = false;
  //保存每个分组最后一个行和当前分组数据
  private SparseArray<GroupInfo> lastRows = new SparseArray<>();

  public GridStickyDecoration(Context context) {
    super(context);
  }

  public GridStickyDecoration(View headerView) {
    super(headerView);
  }

  private void init(RecyclerView parent) {
    GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
    spanCount = manager.getSpanCount();
    manager.setSpanSizeLookup(spanSizeLookup);
    //当前子view是当前分组最后一个，且子view底部高度小于头部高度时，发生碰撞
    int end;
    int total = 0;//当前总长度
    for (int j = 0; j < groups.size(); j++) {
      int currLen = groups.get(j).length;
      total += currLen;
      int remainder = currLen % spanCount;//当前余数
      if (remainder != 0) {
        end = total - remainder;
      } else {
        end = total - spanCount;
      }
      GroupInfo info = groups.get(j);
      lastRows.put(end, info);
    }
  }

  /**
   * 重新判断GridLayoutManager 下应该是每个分组第一行都需预留空间
   *
   * @param parent 当前recyclerView
   * @param view 当前子view
   */
  @Override protected boolean needHeaderSpace(RecyclerView parent, View view) {
    if (needRefresh) {
      init(parent);
      needRefresh = false;
    }
    int pos = parent.getChildAdapterPosition(view);
    int start = 0;
    int count = 0;
    for (int i = 0; i < groups.size(); i++) {
      //grid下当前行都应该预留空间
      //在某个分组里面
      count += groups.get(i).length;
      if (pos < count) {
        //在当前分组的第一排
        return start <= pos && pos < start + spanCount;
      }
      start = count;
    }
    return false;
  }

  @Override protected void startCollision(RecyclerView parent, Canvas c, View child, int left,
      int bottomMargin, int childPosition) {
    int bottom = child.getBottom() + bottomMargin;
    GroupInfo info = lastRows.get(childPosition, null);
    if (bottom < headerHeight && null != info) {
      c.save();
      c.translate(left, bottom - headerHeight);
      drawHeader(parent, c, info);
      c.restore();
    } else {
      int count = 0;
      for (int j = 0; j < groups.size(); j++) {
        count += groups.get(j).length;
        if (childPosition < count) {
          drawHeader(parent, c, groups.get(j));
          break;
        }
      }
    }
  }

  @Override public void setGroupInfo(List<GroupInfo> list) {
    super.setGroupInfo(list);
    needRefresh = true;
    spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        GroupInfo info = endSArr.get(position, null);
        //每组最后一个设置span
        if (info != null) {
          if (info.length % spanCount != 0) {
            return spanCount + 1 - info.length % spanCount;
          } else {
            return 1;
          }
        } else {
          return 1;
        }
      }
    };
  }
}

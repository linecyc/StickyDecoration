package com.linecy.module.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by linecy.
 */
public class LinearStickyDecoration extends RecyclerView.ItemDecoration {

  private View headerView;//头

  int headerHeight;//头部高

  private boolean isCustom;//是否自定义头
  private boolean isSticky;//是否是粘性头

  List<GroupInfo> groups = new ArrayList<>();//保存分组信息
  private SparseArray<GroupInfo> startArr = new SparseArray<>();//分组与起始节点对于关系
  SparseArray<GroupInfo> endSArr = new SparseArray<>();//分组与结束节点对于关系

  public LinearStickyDecoration(Context context) {
    TextView view = new TextView(context);
    int padding = dip2px(context, 16f);
    view.setPadding(padding, padding, padding, padding);
    view.setTextColor(0xff4c7ee9);//默认文字颜色
    this.headerView = view;
    this.isCustom = false;
  }

  public LinearStickyDecoration(View headerView) {
    if (headerView == null) {
      throw new IllegalArgumentException("The header view must not be null.");
    }
    this.headerView = headerView;
    this.isCustom = true;
  }

  @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
      RecyclerView.State state) {
    super.getItemOffsets(outRect, view, parent, state);

    measureHeader(parent);
    if (needHeaderSpace(parent, view)) {
      outRect.top = headerHeight;
    }
  }

  @Override public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
    super.onDrawOver(c, parent, state);
    int childCount = parent.getChildCount();
    int left = parent.getLeft() + parent.getPaddingLeft();
    for (int i = 0; i < childCount; i++) {
      View child = parent.getChildAt(i);
      RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
      int top = child.getTop() - lp.topMargin;
      int pos = parent.getChildAdapterPosition(child);
      if (i > 0 || !isSticky) {
        //判断起始位置，因为只有每个分组的起始位置才添加头部
        GroupInfo info = startArr.get(pos, null);
        if (null != info) {
          c.save();
          c.translate(left, top - headerHeight);//移动到预留位置
          drawHeader(parent, c, info);
          c.restore();
        }
      } else {
        startCollision(parent, c, child, left, lp.bottomMargin, pos);
      }
    }
  }

  /**
   * 测量头部
   */
  private void measureHeader(RecyclerView parent) {
    int childWidth;
    if (headerView.getLayoutParams() == null) {
      headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.WRAP_CONTENT));
      childWidth = headerView.getMeasuredWidth();
    } else {
      childWidth = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
    }

    int childHeight;
    if (headerView.getLayoutParams().height > 0) {
      childHeight = View.MeasureSpec.makeMeasureSpec(headerView.getLayoutParams().height,
          View.MeasureSpec.EXACTLY);
    } else {
      childHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);//未指定
    }

    headerView.measure(childWidth, childHeight);
    headerView.layout(0, 0, headerView.getMeasuredWidth(), headerView.getMeasuredHeight());
    headerHeight = headerView.getMeasuredHeight();
  }

  /**
   * 绘制头
   *
   * @param parent recyclerView
   * @param c canvas
   * @param info group info
   */
  void drawHeader(RecyclerView parent, Canvas c, GroupInfo info) {

    if (isCustom) {
      updateHeaderView(headerView, info);//填充数据
      measureHeader(parent);//填充数据后重新测量View
      headerView.draw(c);
    } else {
      if (headerView instanceof TextView) {
        ((TextView) headerView).setText(info.key);
        ((TextView) headerView).setTypeface(Typeface.DEFAULT_BOLD);
      }
      measureHeader(parent);//填充数据后重新测量View
      headerView.draw(c);
    }
  }

  /**
   * 开始绘制头部
   *
   * @param parent recyclerView
   * @param c canvas
   */
  protected void startCollision(RecyclerView parent, Canvas c, View child, int left,
      int bottomMargin, int childPosition) {

    //碰撞临界点
    //当为分组最后一个，同时子view底部小于头部高度，则平移头部，否则悬浮在顶部
    int end = 0;
    int count = -1;
    for (int j = 0; j < groups.size(); j++) {
      end += groups.get(j).length;
      //当前位置小于某分组的额最后一个节点的话，那么就包含在这个分组内，就返回分组下标
      if (childPosition < end) {
        count = j;
        break;
      }
    }
    View currEndView = parent.getChildAt(end - (childPosition + 1));//当前分组的最后一个view
    int bottom;
    //如果出现最后一个view，且最后一个view的底部小于header的高度的话，表明发生碰撞，下一个的header顶出了上一个的header
    if (null != currEndView
        && (bottom = currEndView.getBottom()
        + ((RecyclerView.LayoutParams) currEndView.getLayoutParams()).bottomMargin)
        <= headerHeight) {//修复item高度小于头部，导致碰撞时没有顶出上一个头部的问题
      c.save();
      c.translate(left, bottom - headerHeight);
      drawHeader(parent, c, groups.get(count));
      c.restore();
    } else {
      drawHeader(parent, c, groups.get(count));
    }
  }

  /**
   * 判断是否需要预留头部空间
   *
   * @param parent 当前recyclerView
   * @param view 当前子view
   * @return 返回当前需要头的group位置
   */
  protected boolean needHeaderSpace(RecyclerView parent, View view) {
    int pos = parent.getChildAdapterPosition(view);
    return null != startArr.get(pos, null);
  }

  /**
   * 自定义header需要自己更新布局
   *
   * @param headerView 头部
   */
  public void updateHeaderView(View headerView, GroupInfo info) {
    //empty
  }

  /**
   * 设置分组信息
   *
   * @param list 分组信息
   */
  public void setGroupInfo(List<GroupInfo> list) {
    this.groups.clear();
    this.startArr.clear();
    this.endSArr.clear();
    if (list != null && list.size() > 0) {
      this.groups.addAll(list);
    }
    int count = groups.size();
    int start = 0;
    for (int i = 0; i < count; i++) {
      startArr.append(start, groups.get(i));
      start += groups.get(i).length;
      //不包含每个分组的尾节点，所以减1
      endSArr.append(start - 1, groups.get(i));
    }
  }

  /**
   * 设置头部背景
   *
   * @param color 颜色
   */
  public void setHeaderBackground(@ColorInt int color) {
    this.headerView.setBackgroundColor(color);
  }

  /**
   * 设置头部边距
   *
   * @param left 左边距
   * @param top 顶部边距
   * @param right 有边距
   * @param bottom 底部边距
   */
  public void setHeaderPadding(int left, int top, int right, int bottom) {
    this.headerView.setPadding(left, top, right, bottom);
  }

  /**
   * 设置头部文字颜色，如果头部是默认的话
   *
   * @param color 颜色
   */
  public void setHeaderTextColor(@ColorInt int color) {
    if (this.headerView instanceof TextView) {
      ((TextView) this.headerView).setTextColor(color);
    }
  }

  /**
   * 是否是粘性头部
   *
   * @param isSticky 是否粘性
   */
  public void setSticky(boolean isSticky) {
    this.isSticky = isSticky;
  }

  public static int dip2px(Context context, float dpValue) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
        context.getResources().getDisplayMetrics());
  }
}

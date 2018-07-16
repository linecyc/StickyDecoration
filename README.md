### 截图

<img width="300" height="500" src="https://github.com/linecyc/StickyDecoration/blob/master/screenshots/p1.gif"/>


### 使用

	//	默认头部
    LinearStickyDecoration stickyDecoration = new LinearStickyDecoration(this);
	
	//自定义头部,自定义view
	LinearStickyDecoration stickyDecoration = new LinearStickyDecoration(view) {
        @Override public void updateHeaderView(View headerView, GroupInfo info) {
          ...
        }
      };

    //设置背景、颜色等
	...
    recyclerView.addItemDecoration(stickyDecoration);

	//设置分组信息，GroupInfo(key,length)
	...
    List<GroupInfo> list = new ArrayList<>();
    list.add(new GroupInfo("第一组", 5));

    stickyDecoration.setGroupInfo(list);
package com.linecy.stickydecoration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    findViewById(R.id.linear1).setOnClickListener(this);
    findViewById(R.id.linear2).setOnClickListener(this);
    findViewById(R.id.grid1).setOnClickListener(this);
    findViewById(R.id.grid2).setOnClickListener(this);
    findViewById(R.id.custom).setOnClickListener(this);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.linear1:
        Intent intent = new Intent(this, LinearActivity.class);
        intent.putExtra(LinearActivity.EXTRA_DATA, true);
        startActivity(intent);
        break;
      case R.id.linear2:
        Intent intent2 = new Intent(this, LinearActivity.class);
        intent2.putExtra(LinearActivity.EXTRA_DATA, false);
        startActivity(intent2);
        break;
      case R.id.grid1:
        Intent intent3 = new Intent(this, GridActivity.class);
        intent3.putExtra(GridActivity.EXTRA_DATA, true);
        startActivity(intent3);
        break;
      case R.id.grid2:
        Intent intent4 = new Intent(this, GridActivity.class);
        intent4.putExtra(GridActivity.EXTRA_DATA, false);
        startActivity(intent4);
        break;
      case R.id.custom:
        Intent intent5 = new Intent(this, GridActivity.class);
        intent5.putExtra(GridActivity.EXTRA_CUSTOM, true);
        intent5.putExtra(GridActivity.EXTRA_DATA, true);
        startActivity(intent5);
        break;
    }
  }
}

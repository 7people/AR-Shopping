package com.example.glass.arshopping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.glass.ui.GlassGestureDetector.Gesture;

public class MainActivity extends BaseActivity {

  private static final int REQUEST_CODE = 105;
  private TextView resultLabel;
  private TextView scanResult;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.SignUpButtonm).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this,SignUp.class);
        startActivity(intent);
      }
    });

    findViewById(R.id.loginButtonm).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this,Login.class);
        startActivity(intent);
      }
    });
  }

  @Override
  public boolean onGesture(Gesture gesture) {
    switch (gesture) {
      case SWIPE_DOWN:
        finishAffinity();
        finish();
        return true;
      default:
        return false;
    }
  }
}
package com.example.glass.arshopping;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.example.glass.ui.GlassGestureDetector;
import com.example.glass.ui.GlassGestureDetector.OnGestureListener;

public abstract class BaseActivity extends FragmentActivity implements OnGestureListener {

  private GlassGestureDetector glassGestureDetector;
  private View decorView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    decorView = getWindow().getDecorView();
    decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
      if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
        hideSystemUI();
      }
    });
    glassGestureDetector = new GlassGestureDetector(this, this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    hideSystemUI();
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    return glassGestureDetector.onTouchEvent(ev) || super.dispatchTouchEvent(ev);
  }

  private void hideSystemUI() {
    decorView.setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_IMMERSIVE
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_FULLSCREEN);
  }
}

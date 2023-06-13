package com.example.glass.arshopping;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.TextureView;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraX;
import androidx.core.content.ContextCompat;
import com.example.glass.arshopping.R;
import com.example.glass.arshopping.QRCodeImageAnalysis.QrCodeAnalysisCallback;
import com.example.glass.ui.GlassGestureDetector.Gesture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraActivity extends BaseActivity implements QrCodeAnalysisCallback {

  public static final String QR_SCAN_RESULT = "SCAN_RESULT";
  private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 105;
  private ExecutorService executorService;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);
    executorService = Executors.newSingleThreadExecutor();

    if (ContextCompat.checkSelfPermission(this,
        permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
      startCamera();
    } else {
      requestPermissions(new String[]{permission.CAMERA}, CAMERA_PERMISSIONS_REQUEST_CODE);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    executorService.shutdown();
  }

  @Override
  public boolean onGesture(Gesture gesture) {
    switch (gesture) {
      case SWIPE_DOWN:
        finishNoQR();
        return true;
      default:
        return false;
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (requestCode == CAMERA_PERMISSIONS_REQUEST_CODE) {
      for (int result : grantResults) {
        if (result != PackageManager.PERMISSION_GRANTED) {
          finishNoQR();
        } else {
          startCamera();
        }
      }
    } else {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }

  @Override
  public void onQrCodeDetected(String result) {
    final Intent intent = new Intent();
    intent.putExtra(QR_SCAN_RESULT, result);
    setResult(Activity.RESULT_OK, intent);
    finish();
  }

  private void startCamera() {
    final TextureView textureView = findViewById(R.id.view_finder);
    final QRCodePreview qrCodePreview = new QRCodePreview(
        CameraConfigProvider.getPreviewConfig(getDisplaySize()),
        textureView);
    final QRCodeImageAnalysis qrCodeImageAnalysis = new QRCodeImageAnalysis(
        CameraConfigProvider.getImageAnalysisConfig(), executorService, this);

    CameraX.bindToLifecycle(this, qrCodePreview.getUseCase(), qrCodeImageAnalysis.getUseCase());
  }

  private void finishNoQR() {
    setResult(Activity.RESULT_CANCELED);
    finish();
  }

  private Size getDisplaySize() {
    final DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    return new Size(displayMetrics.widthPixels, displayMetrics.heightPixels);
  }
}

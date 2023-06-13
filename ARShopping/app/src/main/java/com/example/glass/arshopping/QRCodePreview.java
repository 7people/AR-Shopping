package com.example.glass.arshopping;

import android.view.TextureView;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.camera.core.Preview;
import androidx.camera.core.Preview.OnPreviewOutputUpdateListener;
import androidx.camera.core.Preview.PreviewOutput;
import androidx.camera.core.PreviewConfig;
import androidx.camera.core.UseCase;

public class QRCodePreview implements OnPreviewOutputUpdateListener {
  private final PreviewConfig previewConfig;
  private final TextureView textureView;

  public QRCodePreview(PreviewConfig previewConfig, TextureView textureView) {
    this.previewConfig = previewConfig;
    this.textureView = textureView;
  }

  public UseCase getUseCase() {
    final Preview preview = new Preview(previewConfig);
    preview.setOnPreviewOutputUpdateListener(this);
    return preview;
  }

  @Override
  public void onUpdated(@NonNull PreviewOutput output) {
    final ViewGroup viewGroup = (ViewGroup) textureView.getParent();
    viewGroup.removeView(textureView);
    viewGroup.addView(textureView, 0);
    textureView.setSurfaceTexture(output.getSurfaceTexture());
  }
}

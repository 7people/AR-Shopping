package com.example.glass.arshopping;

import android.util.Size;
import androidx.camera.core.ImageAnalysis.ImageReaderMode;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.PreviewConfig;

public class CameraConfigProvider {
  public static PreviewConfig getPreviewConfig(Size displaySize) {
    return new PreviewConfig.Builder()
        .setTargetResolution(displaySize)
        .build();
  }
  public static ImageAnalysisConfig getImageAnalysisConfig() {
    return new ImageAnalysisConfig.Builder()
        .setImageReaderMode(ImageReaderMode.ACQUIRE_LATEST_IMAGE)
        .build();
  }
}

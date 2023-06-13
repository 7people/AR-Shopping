package com.example.glass.arshopping;

import android.util.Log;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysis.Analyzer;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.UseCase;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import java.nio.ByteBuffer;
import java.util.concurrent.Executor;

public class QRCodeImageAnalysis implements Analyzer {

  private static final String TAG = QRCodeImageAnalysis.class.getSimpleName();
  private final ImageAnalysisConfig imageAnalysisConfig;
  private final Executor executor;
  private final QrCodeAnalysisCallback qrCodeAnalysisCallback;

  public QRCodeImageAnalysis(ImageAnalysisConfig imageAnalysisConfig, Executor executor, QrCodeAnalysisCallback qrCodeAnalysisCallback) {
    this.imageAnalysisConfig = imageAnalysisConfig;
    this.executor = executor;
    this.qrCodeAnalysisCallback = qrCodeAnalysisCallback;
  }

  public UseCase getUseCase() {
    final ImageAnalysis imageAnalysis = new ImageAnalysis(imageAnalysisConfig);
    imageAnalysis.setAnalyzer(executor, this);
    return imageAnalysis;
  }

  @Override
  public void analyze(ImageProxy image, int rotationDegrees) {
    final ByteBuffer buffer = image.getPlanes()[0].getBuffer();
    final byte[] imageBytes = new byte[buffer.remaining()];
    buffer.get(imageBytes);
    final int width = image.getWidth();
    final int height = image.getHeight();
    final PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(imageBytes, width, height, 0, 0, width, height, false);
    final BinaryBitmap zxingBinaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
    try {
      final Result decodedBarcode = new QRCodeReader().decode(zxingBinaryBitmap);
      qrCodeAnalysisCallback.onQrCodeDetected(decodedBarcode.getText());
    }
    catch (NotFoundException | ChecksumException | FormatException e) {
      Log.e(TAG, "QR Code decoding error", e);
    }
  }

  interface QrCodeAnalysisCallback {
    void onQrCodeDetected(String result);
  }
}

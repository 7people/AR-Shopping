package com.example.glass.arshopping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.example.glass.ui.GlassGestureDetector.Gesture;

public class QRMainActivity extends BaseActivity {
    private static final int REQUEST_CODE = 105;
    private TextView resultLabel;
    private TextView scanResult;
    String username="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrmain);
        resultLabel = findViewById(R.id.resultLabel);
        scanResult = findViewById(R.id.scanResult);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                final String qrData = data.getStringExtra(CameraActivity.QR_SCAN_RESULT);
                Intent intent = new Intent(getBaseContext(), BestSellerActivity.class);
                intent.putExtra("Product_Id", qrData.toString());
                intent.putExtra("username", username);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onGesture(Gesture gesture) {
        switch (gesture) {
            case TAP:
                startActivityForResult(new Intent(this, CameraActivity.class), REQUEST_CODE);
                resultLabel.setVisibility(View.GONE);
                scanResult.setVisibility(View.GONE);
                return true;
            case SWIPE_DOWN:
                finish();
                return true;
            default:
                return false;
        }
    }
}
package com.example.glass.arshopping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.glass.arshopping.utils.Global;
import org.json.JSONObject;
import java.util.HashMap;

public class otpCheck extends AppCompatActivity {
    String email_ = "";
    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mRequestQueue = Volley.newRequestQueue((Context) this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_check);
        email_ = getIntent().getStringExtra("email");
        EditText cod2e = findViewById(R.id.code);
        Button btn = findViewById(R.id.checkbtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = cod2e.getText().toString();
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("otp", code);
                hm.put("email", email_);
                if (code != null && email_ != null) {
                    JsonObjectRequest mRequest;
                    mRequest = new JsonObjectRequest(Global.url + "/chcotp", new JSONObject(hm), response -> {
                        try {
                            boolean success = response.optBoolean("Success");
                            String message = response.optString("Message");
                            if (success) {
                                Toast.makeText(otpCheck.this, "OTP Code Affirmed", Toast.LENGTH_SHORT).show();


                                Intent intent = new Intent(getBaseContext(), resetPassword.class);
                                intent.putExtra("email", email_);
                                startActivity(intent);
                            } else {
                                Toast.makeText(otpCheck.this, "Error: " + message, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                        }
                    }, error -> {
                    });
                    mRequestQueue.add(mRequest);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void BackButton(View view)
    {
        Intent intent = new Intent(getBaseContext(),Forgotpassword.class);
        startActivity(intent);
    }
}
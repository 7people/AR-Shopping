package com.example.glass.arshopping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.glass.arshopping.utils.Global;
import com.example.glass.ui.GlassGestureDetector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Collections;
import java.util.HashMap;
import papaya.in.sendmail.SendMail;

public class Forgotpassword extends BaseActivity {
    RequestQueue mRequestQueue;
    String OTPCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        TextView email =(TextView) findViewById(R.id.email);
        Button reset= (Button) findViewById(R.id.reset);
        mRequestQueue = Volley.newRequestQueue((Context) this);

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Forgotpassword.this,otpCheck.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),Login.class);
                startActivity(intent);
            }
        });

        EditText emailEditText = (EditText) findViewById(R.id.email);
        Button regbtn = (Button) findViewById(R.id.reset);
        EditText emailField = findViewById(R.id.email);
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email1= emailField.getText().toString();
                final EditText emailValidate = (EditText)findViewById(R.id.email);
                final TextView textView = (TextView)findViewById(R.id.email);
                String email = emailValidate.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("email", email);

                if(email.matches(emailPattern)) {
                    Toast.makeText(Forgotpassword.this, "Password reset code sent", Toast.LENGTH_SHORT).show();
                    JsonObjectRequest mRequest;
                    mRequest = new JsonObjectRequest(Global.url + "/resetpsw", new JSONObject(Collections.singletonMap("email", email)), response -> {
                        try {
                            boolean success = response.optBoolean("Success");
                            String message = response.optString("Message");

                            if (success) {
                                getgeneratedOTP(email, new OTPCallback() {
                                    @Override
                                    public void onOTPReceived(String OTP) {
                                        if (OTP != null) {
                                            SendMail mail = new SendMail("CHANGE HERE WITH YOUR EMAIL ADDRESS", "CHANGE HERE WITH WITH PASSWORD",
                                                    email,
                                                    "One Time Password - AR Shopping App",
                                                    "Your One Time Password Code:" + OTP
                                            );
                                            mail.execute();
                                            Intent intent = new Intent(getBaseContext(), otpCheck.class);
                                            intent.putExtra("email", email);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(Forgotpassword.this, "Error: Unable to generate OTP", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(Forgotpassword.this, "Error: " + message, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                        Toast.makeText(Forgotpassword.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    });
                    mRequestQueue.add(mRequest);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getgeneratedOTP(String userEmail, OTPCallback callback) {
        JsonObjectRequest mRequest = new JsonObjectRequest(Global.url + "/getlatestpasswordreset/" + userEmail, null, response -> {
            try {
                boolean success = response.optBoolean("Success");
                String message = response.optString("Message");

                if (success) {
                    JSONArray data = response.optJSONArray("Data");

                    try {
                        String generatedOTP = data.getJSONObject(0).optString("otp");
                        callback.onOTPReceived(generatedOTP);
                    } catch (JSONException e) {
                        callback.onOTPReceived(null);
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Error: " + message, Toast.LENGTH_LONG).show();
                    callback.onOTPReceived(null);
                }
            } catch (Exception e) {
                callback.onOTPReceived(null);
            }
        }, error -> {
            callback.onOTPReceived(null);
        });
        mRequestQueue.add(mRequest);
    }

    interface OTPCallback {
        void onOTPReceived(String OTP);
    }

    @Override
    public boolean onGesture(GlassGestureDetector.Gesture gesture) {
        switch (gesture) {
            case SWIPE_DOWN:
                finish();
                return true;
            default:
                return false;
        }
    }
}
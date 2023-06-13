package com.example.glass.arshopping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.glass.arshopping.utils.Global;
import com.example.glass.ui.GlassGestureDetector.Gesture;
import org.json.JSONObject;
import java.util.HashMap;

public class SignUp extends BaseActivity {
    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mRequestQueue = Volley.newRequestQueue((Context) this);
        EditText passwordField1 = findViewById(R.id.password);
        EditText passwordField2 = findViewById(R.id.RePassword);
        EditText emailField = findViewById(R.id.email);
        EditText username = findViewById(R.id.username);
        Button regbtn = findViewById(R.id.SignUpButton);

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username1 = username.getText().toString();
                String password1 = passwordField1.getText().toString();
                String password2 = passwordField2.getText().toString();
                String email1= emailField.getText().toString();
                final EditText emailValidate = findViewById(R.id.email);
                final TextView textView = findViewById(R.id.email);
                String email = emailValidate.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (TextUtils.isEmpty(username1) || TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2) || TextUtils.isEmpty(email1)) {
                    Toast.makeText(getApplicationContext(), "Please fill all the blanks", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (password1.equals(password2)) {
                        if (email.matches(emailPattern)) {
                            if (username1.length() >= 5 && password1.length() >= 5) {
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("user_name", username1);
                                hm.put("user_email", email1);
                                hm.put("user_password", password1);

                                JsonObjectRequest mRequest;
                                mRequest = new JsonObjectRequest(Global.url+"/signup", new JSONObject(hm), response -> {
                                    try {
                                        boolean success = response.optBoolean("Success");
                                        String message = response.optString("Message");
                                        if(success){
                                            Toast.makeText(SignUp.this, username1 + " registered", Toast.LENGTH_SHORT).show();
                                            username.setText("");
                                            emailField.setText("");
                                            passwordField1.setText("");
                                            passwordField2.setText("");
                                            String usernameValue = username1;
                                            Intent intent = new Intent(getBaseContext(),Login.class);
                                            intent.putExtra("username", usernameValue);
                                            startActivity(intent);
                                        }
                                        else {
                                            Toast.makeText(SignUp.this, "Error: " + message, Toast.LENGTH_LONG).show();
                                        }
                                    } catch (Exception e) {}
                                }, error -> {
                                });
                                mRequestQueue.add(mRequest);
                            } else {
                                Toast.makeText(getApplicationContext(), "Username and password must have at least 5 digits", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignUp.this, "Your passwords are not matching", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onGesture(Gesture gesture) {
        switch (gesture) {
            case SWIPE_DOWN:
                finish();
                return true;
            default:
                return false;
        }
    }
}
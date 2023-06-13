package com.example.glass.arshopping;

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

public class resetPassword extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        EditText passwordField1 = findViewById(R.id.password);
        EditText passwordField2 = findViewById(R.id.RePassword);
        mEmail = getIntent().getStringExtra("email");
        mRequestQueue = Volley.newRequestQueue(this);
        Button changePasswordBtn = findViewById(R.id.changeps);

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password1 = passwordField1.getText().toString();
                String password2 = passwordField2.getText().toString();

                if(password1.length() > 4 || password2.length() > 4)
                {
                    if (password1.equals(password2)) {
                        HashMap<String, String> requestBody = new HashMap<>();
                        requestBody.put("email", mEmail);
                        requestBody.put("password", password1);

                        JsonObjectRequest request = new JsonObjectRequest(Global.url + "/pswchange", new JSONObject(requestBody),
                                response -> {
                                    try {
                                        boolean success = response.getBoolean("Success");
                                        String message = response.getString("Message");
                                        if (success) {
                                            Toast.makeText(resetPassword.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(resetPassword.this, Login.class);
                                            startActivity(intent);
                                        }else {
                                            Toast.makeText(resetPassword.this, "Error: " + message, Toast.LENGTH_LONG).show();
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(resetPassword.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                },
                                error -> {
                                    Toast.makeText(resetPassword.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                                });

                        mRequestQueue.add(request);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(resetPassword.this, "Password length must be longer than 5", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void BackButton(View view)
    {
        Intent intent = new Intent(getBaseContext(),otpCheck.class);
        intent.putExtra("email", mEmail);
        startActivity(intent);
    }
}

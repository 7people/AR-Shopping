package com.example.glass.arshopping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.glass.arshopping.utils.Global;
import com.example.glass.ui.GlassGestureDetector;

import org.json.JSONObject;

import java.util.HashMap;

public class Login extends BaseActivity implements GlassGestureDetector.OnGestureListener {
    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String username = getIntent().getStringExtra("username");
        mRequestQueue = Volley.newRequestQueue((Context) this);
        TextView user_name =(TextView) findViewById(R.id.loginusername);
        TextView password =(TextView) findViewById(R.id.password);
        Button loginbtn= (Button) findViewById(R.id.loginbtn);

        if(username != null && !username.equals("")){
            user_name.setText(username);
        }

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Forgotpassword.class);
                startActivity(intent);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=user_name.getText().toString();
                String pass=password.getText().toString();

                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("user_name", user);
                hm.put("user_password", pass);

                JsonObjectRequest mRequest;
                mRequest = new JsonObjectRequest(Global.url+"/login", new JSONObject(hm), response -> {
                    try {
                        boolean success = response.optBoolean("Success");
                        String message = response.optString("Message");
                        if(success){
                            user_name.setText("");
                            password.setText("");
                            Toast.makeText(getBaseContext(),"Login Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getBaseContext(),ProductsActivity.class);
                            intent.putExtra("username", user);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getBaseContext(), "Error: " + message, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {}
                }, error -> {
                });
                mRequestQueue.add(mRequest);
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onGesture(GlassGestureDetector.Gesture gesture) {
        switch (gesture) {
            case SWIPE_DOWN:
                Intent intent = new Intent(Login.this,MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }
}
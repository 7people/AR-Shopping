package com.example.glass.arshopping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.glass.arshopping.utils.Global;
import com.example.glass.ui.GlassGestureDetector;
import org.json.JSONArray;

public class AccountActivity extends BaseActivity {

    String username="";
    RequestQueue mRequestQueue;
    String AccountUserId;
    String AccountUserName="";
    String AccountEmail="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        mRequestQueue = Volley.newRequestQueue((Context) this);
        getAccountDetails();
    }

    private void getAccountDetails(){
        JsonObjectRequest mRequest;
        mRequest = new JsonObjectRequest(Global.url+"/getAccountDetails/"+username, response -> {
            try {
                boolean success = response.optBoolean("Success");
                String message = response.optString("Message");
                JSONArray data = response.optJSONArray("Data");
                if(success){
                    AccountUserId = data.getJSONObject(0).optString("user_id");
                    AccountUserName = data.getJSONObject(0).optString("user_name");
                    AccountEmail = data.getJSONObject(0).optString("user_email");
                    TextView accountNameText =(TextView) findViewById(R.id.accountNameValue);
                    TextView accountEmailText =(TextView) findViewById(R.id.accountEmailValue);
                    accountNameText.setText(AccountUserName);
                    accountEmailText.setText(AccountEmail);
                } else {
                    Toast.makeText(getBaseContext(), "Error: " + message, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {}
        }, error -> {
        });
        mRequestQueue.add(mRequest);
    }

    @Override
    public boolean onGesture(GlassGestureDetector.Gesture gesture) {
        switch (gesture) {
            case SWIPE_DOWN:
                Intent intent = new Intent(getBaseContext(),ProductsActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    public void BackToProductList(View view)
    {
        Intent intent = new Intent(getBaseContext(),ProductsActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
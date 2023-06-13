package com.example.glass.arshopping;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.glass.arshopping.adapters.OrderListAdapter;
import com.example.glass.arshopping.models.Orders;
import com.example.glass.arshopping.utils.Global;
import com.example.glass.ui.GlassGestureDetector;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

public class OrderListActivity  extends BaseActivity implements GlassGestureDetector.OnGestureListener {
    ListView orderList;
    String username = "";
    String order_iddd = "";

    RequestQueue mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        setContentView(R.layout.activity_order_list);
        mRequestQueue = Volley.newRequestQueue((Context) this);
        getData(username);
    }

    public void orderClick(View view) {
        Intent intent = new Intent(getBaseContext(), OrdersActivity.class);
        String order_idd = ((TextView)view.findViewById(R.id.order_idd)).getText().toString();
        intent.putExtra("order_id", order_idd);
        intent.putExtra("user_id", username);
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getData(username);
        }
    }

    @Override
    public boolean onGesture(GlassGestureDetector.Gesture gesture) {
        switch (gesture) {
            case SWIPE_BACKWARD:
                finish();
                return true;
            default:
                return false;
        }
    }

    private void getData(String user_Name) {
        List<com.example.glass.arshopping.models.Orders> orders1 = new ArrayList<Orders>();
        Context ctx = this;
        JsonObjectRequest mRequest;
        mRequest = new JsonObjectRequest(Global.url + "/orderl/" + username, response -> {
            try {
                boolean success = response.optBoolean("Success");
                String message = response.optString("Message");
                JSONArray data = response.optJSONArray("Data");
                if (success && data != null) {
                    for (int i = 0; i < data.length(); i++) {
                        try {
                            double total_amount = data.getJSONObject(i).optDouble("total_amount");
                            int order_id = data.getJSONObject(i).optInt("order_id");
                            order_iddd=order_id + "";
                            String order_date = data.getJSONObject(i).optString("order_date");
                            orders1.add(new com.example.glass.arshopping.models.Orders().createOrderWithMinimalData(
                                    total_amount, order_id, order_date));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        orderList = findViewById(R.id.listOrderx);
                        OrderListAdapter aOrder = new OrderListAdapter(ctx, orders1);
                        orderList.setAdapter(aOrder);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Error: " + message, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
        });
        mRequestQueue.add(mRequest);
    }

    public void BackToProductList(View view)
    {
        Intent intent = new Intent(getBaseContext(),ProductsActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
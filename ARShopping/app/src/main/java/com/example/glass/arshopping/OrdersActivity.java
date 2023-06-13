package com.example.glass.arshopping;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.glass.arshopping.adapters.CustomerInfoAdapter;
import com.example.glass.arshopping.adapters.OrdersAdapter;
import com.example.glass.arshopping.utils.Global;
import com.example.glass.ui.GlassGestureDetector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity implements GlassGestureDetector.OnGestureListener {
    String user_id;
    String credit_card_id = "";
    String order_iddd = "";
    ListView myList;
    RequestQueue mRequestQueue;
    ListView orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        user_id = getIntent().getStringExtra("user_id");
        credit_card_id = getIntent().getStringExtra("credit_card_id");
        order_iddd = getIntent().getStringExtra("order_id");
        mRequestQueue = Volley.newRequestQueue(this);
        getData(credit_card_id,order_iddd);
        getData2(credit_card_id,order_iddd);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getData(credit_card_id,order_iddd);
            getData2(credit_card_id,order_iddd);
        }
    }

    private void getData(String credit_card_id, String order_idd) {
        List<com.example.glass.arshopping.models.Orders> datasx = new ArrayList<>();
        Context ctx = this;
        String url = null;

        if (credit_card_id != null) {
            url = Global.url + "/orderlist/" + credit_card_id;
        }    else if(order_idd != null) {
            url = Global.url + "/myorders/" + order_idd;
        }

        JsonObjectRequest mRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dataArray = response.getJSONArray("Data");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject orderObject = dataArray.getJSONObject(i);
                                double total_amount = orderObject.optDouble("total_amount");
                                int cart_id = orderObject.optInt("cart_id");
                                String credit_card_id2 = orderObject.optString("credit_card_id");
                                String user_id = orderObject.optString("user_id");
                                int order_id = orderObject.optInt("order_id");
                                int product_id = orderObject.optInt("product_id");
                                String quantity = orderObject.optString("quantity");
                                String address = orderObject.optString("address");
                                String product_img = orderObject.optString("product_img");
                                String product_name = orderObject.optString("product_name");
                                double product_price = orderObject.optDouble("product_price");
                                String seller_name = orderObject.optString("seller_name");
                                String order_date = orderObject.optString("order_date");
                                datasx.add(new com.example.glass.arshopping.models.Orders().createOrder(total_amount, order_id, cart_id, address,
                                        credit_card_id2, user_id, product_img, product_name, seller_name, quantity, order_date, product_price, product_id));
                            }

                            ListView orderList = findViewById(R.id.listOrders2);
                            OrdersAdapter aOrder = new OrdersAdapter(ctx, datasx);
                            orderList.setAdapter(aOrder);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ctx, "Error parsing JSON data", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(ctx, "Error retrieving data", Toast.LENGTH_LONG).show();
                    }
                });
        mRequestQueue.add(mRequest);
    }

    private void getData2(String credit_card_id, String order_idd) {
        List<com.example.glass.arshopping.models.Orders> orders2 = new ArrayList<>();
        Context ctx = this;
        String url = null;

        if (credit_card_id != null) {
            url = Global.url + "/orderlist/" + credit_card_id;
        }
        else if(order_idd != null) {
            url = Global.url + "/myorders/" + order_idd;
        }

        JsonObjectRequest mRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.optBoolean("Success");
                            String message = response.optString("Message");
                            JSONArray data = response.optJSONArray("Data");

                            if (success) {
                                try {
                                    double total_amount = data.getJSONObject(0).optDouble("total_amount");
                                    String card_number = data.getJSONObject(0).optString("card_number");
                                    String user_id = data.getJSONObject(0).optString("user_id");
                                    int order_id = data.getJSONObject(0).optInt("order_id");
                                    String adress = data.getJSONObject(0).optString("adress");
                                    String order_date = data.getJSONObject(0).optString("order_date");
                                    orders2.add(new com.example.glass.arshopping.models.Orders().createOrderWithCardDetails(total_amount, card_number, adress, user_id, order_date, order_id));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    ListView myll = findViewById(R.id.listOrders1);
                                    CustomerInfoAdapter ordersAdapter = new CustomerInfoAdapter(ctx, orders2);
                                    myll.setAdapter(ordersAdapter);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(getBaseContext(), "Error: " + message, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        mRequestQueue.add(mRequest);
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

    public void BackToOrderList(View view)
    {
        Intent intent = new Intent(getBaseContext(),OrderListActivity.class);
        intent.putExtra("username", user_id);
        startActivity(intent);
    }
}


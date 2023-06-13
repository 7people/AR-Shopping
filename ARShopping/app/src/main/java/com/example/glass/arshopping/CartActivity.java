package com.example.glass.arshopping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.glass.arshopping.adapters.CartAdapter;
import com.example.glass.arshopping.models.Cart;
import com.example.glass.arshopping.utils.Global;
import com.example.glass.ui.GlassGestureDetector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartActivity  extends BaseActivity implements GlassGestureDetector.OnGestureListener {
    ListView cartList;
    String username = "";
    RequestQueue mRequestQueue;
    String cart_id3;
    String idd;
    String sdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        String cart_id2 = intent.getStringExtra("cart_id2");

        Log.d("denemeyapma", "denemeyapma: " + cart_id3);

        setContentView(R.layout.activity_cart);
        mRequestQueue = Volley.newRequestQueue((Context) this);
        getData(username);
        findViewById(R.id.confirmCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart_id3 != null) {
                    Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("cart_id3", cart_id3);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "There is no product in cart!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onRemoveButtonClick(View view) {
        TextView text = findViewById(R.id.cidd);
        if (text != null) {
            String id = text.getText().toString();
            if (!TextUtils.isEmpty(id)) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("id", id);
                JsonObjectRequest mRequest;
                mRequest = new JsonObjectRequest(Global.url + "/removecart", new JSONObject(hm), response -> {
                    try {
                        boolean success = response.optBoolean("Success");
                        String message = response.optString("Message");

                        if (success) {
                            Toast.makeText(getApplicationContext(), "Product Deleted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: " + message, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                        error -> {
                            error.printStackTrace();
                        });

                mRequestQueue.add(mRequest);
            } else {
                Toast.makeText(getApplicationContext(), "Invalid cart ID", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //end
    @Override
    public boolean onGesture(GlassGestureDetector.Gesture gesture) {
        switch (gesture) {
            case SWIPE_BACKWARD:
                Intent intent = new Intent(getBaseContext(),ProductsActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    } private void getData(String username){
        List<Cart> cart = new ArrayList<Cart>();
        Context ctx = this;
        JsonObjectRequest mRequest;
        mRequest = new JsonObjectRequest(Global.url+"/cart/"+username, response -> {
            try {
                boolean success = response.optBoolean("Success");
                String message = response.optString("Message");
                JSONArray data = response.optJSONArray("Data");
                if(success){
                    for (int i=0; i< data.length(); i++) {
                        try {
                            String seller_id= data.getJSONObject(i).optString("seller_id");
                            String product_name = data.getJSONObject(i).optString("product_name");
                            String quantity = data.getJSONObject(i).optString("quantity");
                            String product_img = data.getJSONObject(i).optString("product_img");
                            String date = data.getJSONObject(i).optString("date");
                            String id = data.getJSONObject(i).optString("id");
                            String cart_id2 = data.getJSONObject(i).optString("cart_id");
                            cart_id3 = cart_id2; // Assign the value to cart_id3
                            idd=id;
                            cart.add(new Cart(seller_id,  product_name,  quantity, date,id,product_img,cart_id2));
                        } catch (JSONException e) {}
                    }
                    cartList = (ListView) findViewById(R.id.listCart);
                    CartAdapter aCart = new CartAdapter(ctx, cart);
                    cartList.setAdapter(aCart);
                } else {
                    Toast.makeText(getBaseContext(), "Error: " + message, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {}
        }, error -> {
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
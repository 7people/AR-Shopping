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
import com.example.glass.arshopping.adapters.BestSellerAdapter;
import com.example.glass.arshopping.adapters.ProductAdapter;
import com.example.glass.arshopping.models.Product;
import com.example.glass.arshopping.models.ProductSeller;
import com.example.glass.arshopping.models.Seller;
import com.example.glass.arshopping.utils.Global;
import com.example.glass.ui.GlassGestureDetector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class BestSellerActivity extends BaseActivity implements GlassGestureDetector.OnGestureListener {
    String  username;
    String productId = "";
    ListView myList;
    RequestQueue mRequestQueue;
    Number seller_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_seller);
        username = getIntent().getStringExtra("username");
        productId = getIntent().getStringExtra("Product_Id");
        mRequestQueue = Volley.newRequestQueue((Context) this);
        getData();
        getProduct();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getProduct();
        }
    }

    private void getData(){
        ArrayList<ProductSeller> productSellers = new ArrayList<>();
        Context ctx = this;
        JsonObjectRequest mRequest;
        mRequest = new JsonObjectRequest(Global.url+"/comparison/"+productId, response -> {
            try {
                boolean success = response.optBoolean("Success");
                String message = response.optString("Message");
                JSONArray data = response.optJSONArray("Data");
                if(success){
                    for (int i=0; i< data.length(); i++) {
                        try {
                            int id = data.getJSONObject(i).optJSONObject("seller").optInt("seller_id");
                            String name = data.getJSONObject(i).optJSONObject("seller").optString("seller_name");
                            String image = data.getJSONObject(i).optJSONObject("seller").optString("seller_image");
                            double price = data.getJSONObject(i).optDouble("price");
                            productSellers.add(new ProductSeller(Integer.parseInt(productId), new Seller(id, name, image), price));
                        seller_id=id;
                        } catch (JSONException e) {}
                    }

                    myList = (ListView) findViewById(R.id.listProducts);
                    productSellers.sort(Comparator.comparing(o -> o.getPrice()));

                    BestSellerAdapter aBestSeller = new BestSellerAdapter(this, productSellers);
                    myList.setAdapter(aBestSeller);
                } else {
                    Toast.makeText(getBaseContext(), "Error: " + message, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) { }
        }, error -> {
        });
        mRequestQueue.add(mRequest);
    }

    private void getProduct(){
        List<Product> products = new ArrayList<Product>();
        Context ctx = this;
        JsonObjectRequest mRequest;
        mRequest = new JsonObjectRequest(Global.url+"/product/"+productId, response -> {
            try {
                boolean success = response.optBoolean("Success");
                String message = response.optString("Message");
                JSONArray data = response.optJSONArray("Data");
                if(success){
                    try {
                        int id = data.getJSONObject(0).optInt("product_id");
                        String name = data.getJSONObject(0).optString("product_name");
                        String description = data.getJSONObject(0).optString("product_description");
                        String image = data.getJSONObject(0).optString("product_image");
                        products.add(new Product(id, name,image, description));
                    } catch (JSONException e) {}
                    try {
                        ListView productsList = (ListView) findViewById(R.id.myproduct);
                        ProductAdapter aProducts = new ProductAdapter(ctx, products);
                        productsList.setAdapter(aProducts);
                    } catch (Exception e){}
                    try {
                        ListView productsList = (ListView) findViewById(R.id.my_product2);
                        ProductAdapter aProducts = new ProductAdapter(ctx, products);
                        productsList.setAdapter(aProducts);
                    } catch (Exception e){}
                } else {
                    Toast.makeText(getBaseContext(), "Error: " + message, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {}
        }, error -> {
        });
        mRequestQueue.add(mRequest);
    }

    public void addCartClick(View view) {
        if (true) {
            HashMap<String, String> hm = new HashMap<String, String>();
            TextView sellerTextView = findViewById(R.id.seller_iddd);
            String sellerId = sellerTextView.getText().toString();
            hm.put("user_id", username);
            hm.put("seller_id", String.valueOf(sellerId));
            hm.put("product_id", productId);
            JsonObjectRequest mRequest;
            mRequest = new JsonObjectRequest(Global.url+"/addcart", new JSONObject(hm), response -> {
                try {
                    boolean success = response.optBoolean("Success");
                    String message = response.optString("Message");
                    if(success){
                        Toast.makeText(BestSellerActivity.this, "Product added to cart", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BestSellerActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {}
            }, error -> {
            });
            mRequestQueue.add(mRequest);
        } else {
            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(getBaseContext(), CartActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void onClickk(View view) {
        if (username != null) {
            Intent intent = new Intent(BestSellerActivity.this,ProductsActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
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

    public void productClick(View view)
    {
        // Best Sellers sayfasındaki ürün detayına tıklayınca
        // uyarı vermemesi için bu boş metodu oluşturduk
    }

}
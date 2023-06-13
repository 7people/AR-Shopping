package com.example.glass.arshopping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.glass.arshopping.adapters.ProductAdapter;
import com.example.glass.arshopping.models.Category;
import com.example.glass.arshopping.models.Product;
import com.example.glass.arshopping.utils.Global;
import org.json.JSONArray;
import org.json.JSONException;
import org.tensorflow.lite.examples.detection.DetectorActivity;
import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String username="";
    ListView productsList;
    Spinner spinner;
    RequestQueue mRequestQueue;
    ArrayList<String> categoriess = new ArrayList<>();
    List<Category> categories = new ArrayList<Category>();
    String search = "0";
    Button objDetect;
    DetectorActivity da;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        da = new DetectorActivity();
        try {
            search = getIntent().getStringExtra("search");
        } catch (Exception e){ }

        if(search == null){
            search = "0";
        }

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        setContentView(R.layout.activity_products);
        categoriess.add("|Category");
        spinner = (Spinner)findViewById(R.id.spinner);
        objDetect = (Button)findViewById(R.id.button);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_spinner_item,categoriess);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        mRequestQueue = Volley.newRequestQueue((Context) this);
        getData(0, search);
        getCategories();

        findViewById(R.id.qrBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),QRMainActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnOrders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),OrderListActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        findViewById(R.id.cartBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),CartActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),AccountActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        String text = spinner.getSelectedItem().toString();
        String i = text.substring(0,1);
        getData(i.equals("|")? 0: Integer.parseInt(i), search);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void productClick(View view) {
        Intent intent = new Intent(getBaseContext(), BestSellerActivity.class);
        String product_id = ((TextView)view.findViewById(R.id.productid)).getText().toString();
        intent.putExtra("Product_Id", product_id);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void getData(int category, String search){
        if(!search.equals("0")){
            objDetect.setBackground(ContextCompat.getDrawable(this, R.drawable.search_background));
        } else {
            objDetect.setBackground(ContextCompat.getDrawable(this, R.drawable.black_button_background));
        }
        List<Product> products = new ArrayList<Product>();
        Context ctx = this;
        JsonObjectRequest mRequest;
        mRequest = new JsonObjectRequest(Global.url+"/products/"+category+"/"+search, response -> {
            try {
                boolean success = response.optBoolean("Success");
                String message = response.optString("Message");
                JSONArray data = response.optJSONArray("Data");
                if(success){
                    for (int i=0; i< data.length(); i++) {
                        try {
                            int id = data.getJSONObject(i).optInt("product_id");
                            String name = data.getJSONObject(i).optString("product_name");
                            String description = data.getJSONObject(i).optString("product_description");
                            String image = data.getJSONObject(i).optString("product_image");
                            products.add(new Product(id, name,image, description));
                        } catch (JSONException e) {}
                    }
                   if(products.size() == 0){
                        ((TextView)findViewById(R.id.textView9)).setVisibility(View.VISIBLE);
                    } else {
                        ((TextView)findViewById(R.id.textView9)).setVisibility(View.GONE);
                    }

                    productsList = (ListView) findViewById(R.id.listProducts);
                    ProductAdapter aProducts = new ProductAdapter(ctx, products);
                    productsList.setAdapter(aProducts);
                } else {
                    Toast.makeText(getBaseContext(), "Error: " + message, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {}
        }, error -> {
        });
        mRequestQueue.add(mRequest);
    }

    private void getCategories(){
        Context ctx = this;
        JsonObjectRequest mRequest;
        mRequest = new JsonObjectRequest(Global.url+"/categories", response -> {
            try {
                boolean success = response.optBoolean("Success");
                String message = response.optString("Message");
                JSONArray data = response.optJSONArray("Data");
                categoriess = new ArrayList<>();
                categoriess.add("|Category");
                if(success){
                    for (int i=0; i< data.length(); i++) {
                        try {
                            int id = data.getJSONObject(i).optInt("category_id");
                            String name = data.getJSONObject(i).optString("category_name");
                            String image = data.getJSONObject(i).optString("category_image");
                            categories.add(new Category(id, name,image));
                            categoriess.add(id+"|"+name);
                        } catch (JSONException e) {}
                    }

                    ArrayAdapter<String>adapter = new ArrayAdapter<String>(getBaseContext(),
                            android.R.layout.simple_spinner_item,categoriess);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(this);
                } else {
                    Toast.makeText(getBaseContext(), "Error: " + message, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {}
        }, error -> {
        });
        mRequestQueue.add(mRequest);
    }

    public void LogOut(View view)
    {
        Intent intent = new Intent(getBaseContext(),MainActivity.class);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Logged Out", Toast.LENGTH_LONG).show();
    }

    public void objectDetection(View view) {
        if(objDetect.getBackground().getConstantState() == getResources().getDrawable(R.drawable.search_background).getConstantState()){
            objDetect.setBackground(ContextCompat.getDrawable(this, R.drawable.black_button_background));
            search = "0";
            getData(0, "0");
        } else {
            Intent intent = new Intent(getBaseContext(), da.getClass());
            intent.putExtra("username", username);
            startActivity(intent);
        }
    }
}
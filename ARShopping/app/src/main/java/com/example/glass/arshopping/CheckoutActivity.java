package com.example.glass.arshopping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.glass.arshopping.utils.Global;
import com.example.glass.ui.GlassGestureDetector.Gesture;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CheckoutActivity extends BaseActivity{
    RequestQueue mRequestQueue;
    String username = "";
    String cart_id3 = "";
    String credit_card_id="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Intent intent = getIntent();
        mRequestQueue = Volley.newRequestQueue((Context) this);
        username = intent.getStringExtra("username");
        cart_id3 = intent.getStringExtra("cart_id3"); // Retrieve the cart_id extra
        EditText cardNumber = findViewById(R.id.CardNumber);
        EditText userName = findViewById(R.id.CardUserName);
        EditText expDate = findViewById(R.id.ExperationDate);
        EditText cvv =findViewById(R.id.Cvv);
        EditText phone =findViewById(R.id.phone);
        EditText address=findViewById(R.id.address);
        Button submit1 = (Button) findViewById(R.id.SubmitButton);

        submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cardNu1 = cardNumber.getText().toString();
                String username2 = userName.getText().toString();
                String exprDate = expDate.getText().toString();
                String Cvv= cvv.getText().toString();
                String Phone=phone.getText().toString();
                String Address=address.getText().toString();
                String userr_id = username.toString();
                String cart_idd=cart_id3;

                if((TextUtils.isEmpty(cardNu1) || TextUtils.isEmpty(username2) || TextUtils.isEmpty(exprDate) || TextUtils.isEmpty(Cvv) || TextUtils.isEmpty(Phone) || TextUtils.isEmpty(Address))) {
                    Toast.makeText(getApplicationContext(), "Please fill all the blanks", Toast.LENGTH_SHORT).show();
                } else if (cardNumber.length()!=20) {
                    Toast.makeText(getApplicationContext(), "Card Number has to be 16 digits", Toast.LENGTH_SHORT).show();
                } else if (cvv.length()!=3) {
                    Toast.makeText(getApplicationContext(), "Cvv has to be 3 digits", Toast.LENGTH_SHORT).show();
                } else if (expDate.length()!=5) {
                    Toast.makeText(getApplicationContext(), "Write correct ExpDate", Toast.LENGTH_SHORT).show();
                } else if (phone.length()!=10) {
                    Toast.makeText(getApplicationContext(), "Rewrite phone number(5xx xxx xx xx)", Toast.LENGTH_SHORT).show();
                }
                else {
                    HashMap<String, String> hm = new HashMap<String, String>();
                    hm.put("card_username", username2);
                    hm.put("card_number", cardNu1);
                    hm.put("expiration_date", exprDate);
                    hm.put("cvv", Cvv);
                    hm.put("user_id", userr_id);
                    hm.put("phone_number", Phone);
                    hm.put("cart_id", cart_idd);
                    hm.put("address", Address);
                    JsonObjectRequest mRequest;
                    mRequest = new JsonObjectRequest(Global.url+"/checkout", new JSONObject(hm), response -> {
                        try {
                            boolean success = response.optBoolean("Success");
                            String message = response.optString("Message");
                            if(success){
                                getgeneratedCreditCard(userr_id, new CheckoutActivity.CreditCardCallback() {
                                    @Override
                                    public void onCreditCardReceived(String CreditCard) {
                                        if (CreditCard != null) {
                                            OrderCompleted(userr_id,CreditCard,cart_id3);
                                            Intent intent = new Intent(getBaseContext(), OrdersActivity.class);
                                            intent.putExtra("user_id", userr_id);
                                            intent.putExtra("credit_card_id", CreditCard);
                                            credit_card_id = CreditCard;
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(CheckoutActivity.this, "Error: Unable to generate OTP", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                                cardNumber.setText("");
                                userName.setText("");
                                expDate.setText("");
                                cvv.setText("");
                                phone.setText("");
                                address.setText("");

                            } else {
                                Toast.makeText(CheckoutActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {}
                    }, error -> {
                    });
                    mRequestQueue.add(mRequest);
                }
            }
        });

        EditText cardNumberEditText = findViewById(R.id.CardNumber);
        cardNumberEditText.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting;
            private String previousText;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No-op
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isFormatting) {
                    return;
                }

                isFormatting = true;

                // Remove all spaces from the string
                String digitsOnly = s.toString().replaceAll("\\s", "");
                StringBuilder formatted = new StringBuilder();
                int digitCount = 0;
                for (int i = 0; i < digitsOnly.length(); i++) {
                    formatted.append(digitsOnly.charAt(i));
                    digitCount++;
                    if (digitCount == 4) {
                        formatted.append(" ");
                        digitCount = 0;
                    }
                }

                // Update the text in the EditText
                int selectionIndex = formatted.length();
                cardNumberEditText.setText(formatted.toString());
                cardNumberEditText.setSelection(selectionIndex);

                // Prevent infinite recursion (afterTextChanged is called again)
                isFormatting = false;
            }
        });

        EditText cvvEditText = findViewById(R.id.ExperationDate);
        cvvEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cvvText = s.toString().trim();
                if (cvvText.length() == 2 && before == 0) {
                    cvvEditText.setText(cvvText + "/");
                    cvvEditText.setSelection(cvvEditText.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckoutActivity.this,CartActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }

        });
    }

    private void getgeneratedCreditCard(String userID, CheckoutActivity.CreditCardCallback callback) {
        JsonObjectRequest mRequest = new JsonObjectRequest(Global.url + "/getlatestcreditCardID/" + userID, null, response -> {
            try {
                boolean success = response.optBoolean("Success");
                String message = response.optString("Message");

                if (success) {
                    JSONArray data = response.optJSONArray("Data");

                    try {
                        String generatedCreditCard = data.getJSONObject(0).optString("credit_card_id");
                        callback.onCreditCardReceived(generatedCreditCard);
                    } catch (JSONException e) {
                        callback.onCreditCardReceived(null);
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Error: " + message, Toast.LENGTH_LONG).show();
                    callback.onCreditCardReceived(null);
                }
            } catch (Exception e) {
                callback.onCreditCardReceived(null);
            }
        }, error -> {
            callback.onCreditCardReceived(null);
        });
        mRequestQueue.add(mRequest);
    }

    interface CreditCardCallback {
        void onCreditCardReceived(String CreditCardID);
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

    public void OrderCompleted(String username,String credit_card_id,String cart_id3) {
        if (username != null && credit_card_id != null && cart_id3 != null) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("user_id", username);
            hm.put("credit_card_id", credit_card_id);
            hm.put("cart_id", cart_id3);
            JsonObjectRequest mRequest;
            mRequest = new JsonObjectRequest(Global.url+"/orders", new JSONObject(hm), response -> {
                try {
                    boolean success = response.optBoolean("Success");
                    String message = response.optString("Message");
                    if(success){
                        Toast.makeText(CheckoutActivity.this, "Order Completed!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CheckoutActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {}
            }, error -> {
            });
            mRequestQueue.add(mRequest);
        } else {

        }
    }
}

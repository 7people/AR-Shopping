package com.example.glass.arshopping.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.example.glass.arshopping.R;
import com.example.glass.arshopping.models.Orders;
import java.util.List;

public class OrdersAdapter extends ArrayAdapter<Orders> {

    public OrdersAdapter(@NonNull Context context, @NonNull List<Orders> objects) {
        super(context, R.layout.orders, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.orders, parent, false
            );
        }

        Orders orderItem = getItem(position);

        ImageView productImage = listItemView.findViewById(R.id.orders_product_img);
        Glide.with(getContext()).load(orderItem.getProductImg()).into(productImage);

        TextView productName = listItemView.findViewById(R.id.p_name);
        productName.setText(orderItem.getProductName());

        TextView sellerName = listItemView.findViewById(R.id.seller_name);
        sellerName.setText(orderItem.getSellerName());

        TextView quantity2 = listItemView.findViewById(R.id.p_quantity);
        quantity2.setText(String.valueOf(orderItem.getQuantity()));

        TextView price = listItemView.findViewById(R.id.product_price);
        price.setText(String.valueOf(orderItem.getProductPrice()));

        return listItemView;
    }

}
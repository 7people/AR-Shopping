package com.example.glass.arshopping.adapters;

import android.content.Context;
import android.util.Log;
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
import com.example.glass.arshopping.models.Cart;
import java.util.List;

public class CartAdapter extends ArrayAdapter<Cart> {

    public CartAdapter(@NonNull Context context, @NonNull List<Cart> objects) {
        super(context, R.layout.cart, objects);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.cart, parent, false
            );
        }

        Cart getPos = getItem(position);
        ImageView Image = (ImageView) listItemView.findViewById(R.id.product_img);

        try {
            Glide.with(listItemView.getContext()).load(getPos.getProduct_img()).into(Image);
        } catch (Exception e){
            Log.i("Exception", e.getMessage());
        }

        TextView product_name = (TextView) listItemView.findViewById(R.id.product_name);
        product_name.setText(getPos.getProduct());

        TextView seller_id = (TextView) listItemView.findViewById(R.id.seller_id);
        seller_id.setText(getPos.getSellerID()+"");

        TextView quantity = (TextView) listItemView.findViewById(R.id.quantity);
        quantity.setText(getPos.getQuantity());

        TextView date = (TextView) listItemView.findViewById(R.id.date);
        date.setText(getPos.getDate());

        TextView id = (TextView) listItemView.findViewById(R.id.c_id);
        id.setText(getPos.getId());

        TextView cidd = (TextView) listItemView.findViewById(R.id.cidd);
        cidd.setText(getPos.getCartId());

        return listItemView;
    }

}

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
import com.example.glass.arshopping.R;
import com.example.glass.arshopping.models.ProductSeller;
import com.example.glass.arshopping.utils.SVG;
import java.util.List;

public class BestSellerAdapter extends ArrayAdapter<ProductSeller> {

    public BestSellerAdapter(@NonNull Context context, @NonNull List<ProductSeller> objects) {
        super(context, R.layout.bestseller, objects);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.bestseller, parent, false
            );
        }
        ProductSeller getPos = getItem(position);

        ImageView Image = (ImageView) listItemView.findViewById(R.id.bestsellerimg);

        try {
            SVG.fetchSvg(listItemView.getContext(), getPos.getSeller().getImage(), Image);
        } catch (Exception e){
            Log.i("Exception", e.getMessage());
        }
        TextView seller = (TextView) listItemView.findViewById(R.id.seller);
        seller.setText(getPos.getSeller().getName());

        TextView ss = (TextView) listItemView.findViewById(R.id.seller_iddd);
        ss.setText(getPos.getSeller().getId()+"");

        TextView product = (TextView) listItemView.findViewById(R.id.product);
        product.setText(""+getPos.getProduct_id());

        TextView price = (TextView) listItemView.findViewById(R.id.price);
        price.setText(getPos.getPrice()+" TL");

        return listItemView;
    }
}

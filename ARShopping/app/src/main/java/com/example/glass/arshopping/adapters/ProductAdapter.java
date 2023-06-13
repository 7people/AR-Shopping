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
import com.example.glass.arshopping.models.Product;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    public ProductAdapter(@NonNull Context context, @NonNull List<Product> objects) {
        super(context, R.layout.product, objects);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.product, parent, false
            );
        }
        Product getPos = getItem(position);

        ImageView Image = (ImageView) listItemView.findViewById(R.id.productimg);
        try {
            Glide.with(listItemView.getContext()).load(getPos.getImage()).into(Image);
        } catch (Exception e){
            Log.i("Exception", e.getMessage());
        }

        TextView id = (TextView) listItemView.findViewById(R.id.productid);
        id.setText(getPos.getId()+"");

        TextView name = (TextView) listItemView.findViewById(R.id.productname);
        name.setText(getPos.getName());

        TextView description = (TextView) listItemView.findViewById(R.id.description);
        description.setText(getPos.getDescription());

        return listItemView;
    }
}

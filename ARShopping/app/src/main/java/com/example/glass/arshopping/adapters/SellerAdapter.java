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
import com.example.glass.arshopping.R;
import com.example.glass.arshopping.models.Seller;
import java.util.List;

public class SellerAdapter extends ArrayAdapter<Seller> {

    public SellerAdapter(@NonNull Context context, @NonNull List<Seller> objects) {
        super(context, R.layout.seller, objects);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.seller, parent, false
            );
        }
        Seller getPos = getItem(position);

        int resId = listItemView.getResources().getIdentifier(getPos.getImage() , "drawable", listItemView.getContext().getPackageName());
        ImageView image = (ImageView) listItemView.findViewById(R.id.sellerimg);
        image.setImageResource(resId);

        TextView id = (TextView) listItemView.findViewById(R.id.sellerid);
        id.setText(getPos.getId()+"");

        TextView name = (TextView) listItemView.findViewById(R.id.sellername);
        name.setText(getPos.getName());

        return listItemView;
    }
}

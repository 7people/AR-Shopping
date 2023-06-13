package com.example.glass.arshopping.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.glass.arshopping.R;
import com.example.glass.arshopping.models.Orders;
import java.util.List;

public class CustomerInfoAdapter extends ArrayAdapter<Orders> {

    public CustomerInfoAdapter(@NonNull Context context, @NonNull List<Orders> objects) {
        super(context, R.layout.customer_info, objects);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.customer_info, parent, false
            );
        }
        com.example.glass.arshopping.models.Orders orderItem = getItem(position);

        TextView userID = listItemView.findViewById(R.id.p_user_id);
        userID.setText(orderItem.getUserId());

        TextView order_date = listItemView.findViewById(R.id.p_date);
        order_date.setText(orderItem.getOrderDate());

        TextView address = listItemView.findViewById(R.id.p_address);
        address.setText(orderItem.getAdress());

        TextView total_amount = listItemView.findViewById(R.id.p_total_amount);
        total_amount.setText(String.valueOf(orderItem.getTotalAmount()));

        TextView order_id = listItemView.findViewById(R.id.p_order_id);
        order_id.setText(String.valueOf(orderItem.getOrderId()));

        return listItemView;
    }
}

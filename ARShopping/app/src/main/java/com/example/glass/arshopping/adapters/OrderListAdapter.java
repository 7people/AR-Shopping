package com.example.glass.arshopping.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.glass.arshopping.models.Orders;
import com.example.glass.arshopping.R;
import java.util.List;

public class OrderListAdapter extends ArrayAdapter<com.example.glass.arshopping.models.Orders> {

    public OrderListAdapter(@NonNull Context context, @NonNull List<com.example.glass.arshopping.models.Orders> objects) {
        super(context, R.layout.order_list, objects);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.order_list, parent, false
            );
        }
        com.example.glass.arshopping.models.Orders orderItem = getItem(position);
        TextView order_id = listItemView.findViewById(R.id.order_idd);
        order_id.setText(""+orderItem.getOrderId());
        TextView order_date = listItemView.findViewById(R.id.order_datee);
        order_date.setText(orderItem.getOrderDate());
        TextView total_amount = listItemView.findViewById(R.id.total_amountt);
        total_amount.setText(String.valueOf(orderItem.getTotalAmount() +" TL"));

        return listItemView;
    }
}

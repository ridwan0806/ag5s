package com.hanindya.ag5s.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.R;

public class VHOrderDetail extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView foodName,qty,price,subtotal,numberCount;
    public ImageView menu;

    public VHOrderDetail(@NonNull View itemView) {
        super(itemView);
        foodName = itemView.findViewById(R.id.txtOrderDetailFoodName);
        qty = itemView.findViewById(R.id.txtOrderDetailFoodQty);
        price = itemView.findViewById(R.id.txtOrderDetailfoodPrice);
        subtotal = itemView.findViewById(R.id.txtOrderDetailFoodSubtotal);
        numberCount = itemView.findViewById(R.id.txtOrderDetailNumberCount);
        menu = itemView.findViewById(R.id.imgMenuOrderDetail);
    }

    @Override
    public void onClick(View view) {

    }
}

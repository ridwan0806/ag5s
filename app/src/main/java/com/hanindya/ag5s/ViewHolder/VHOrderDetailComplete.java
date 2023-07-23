package com.hanindya.ag5s.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.R;

public class VHOrderDetailComplete extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView foodName,qty,price,subtotal,numberCount;
    public VHOrderDetailComplete(@NonNull View itemView) {
        super(itemView);
        foodName = itemView.findViewById(R.id.txtOrderDetailCompleteFoodName);
        qty = itemView.findViewById(R.id.txtOrderDetailCompleteFoodQty);
        price = itemView.findViewById(R.id.txtOrderDetailCompletefoodPrice);
        subtotal = itemView.findViewById(R.id.txtOrderDetailCompleteFoodSubtotal);
        numberCount = itemView.findViewById(R.id.txtOrderDetailCompleteNumberCount);
    }

    @Override
    public void onClick(View view) {

    }
}

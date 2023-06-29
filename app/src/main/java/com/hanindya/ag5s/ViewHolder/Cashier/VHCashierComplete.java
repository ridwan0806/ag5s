package com.hanindya.ag5s.ViewHolder.Cashier;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.Interface.ItemClickListener;

public class VHCashierComplete extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView cashierCompleteCustomerName,cashierCompleteCustomerType,cashierCompleteOrderType,cashierCompleteSubtotalPrice;
    private ItemClickListener itemClickListener;

    public VHCashierComplete(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getBindingAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}

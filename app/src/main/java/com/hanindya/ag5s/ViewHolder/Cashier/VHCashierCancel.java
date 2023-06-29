package com.hanindya.ag5s.ViewHolder.Cashier;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.Interface.ItemClickListener;

public class VHCashierCancel extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView cashierCancelCustomerName,cashierCancelCustomerType,cashierCancelOrderType,cashierCancelSubtotalPrice;
    private ItemClickListener itemClickListener;

    public VHCashierCancel(@NonNull View itemView) {
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

package com.hanindya.ag5s.ViewHolder.Cashier;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.R;

public class VHCashierProcess extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView cashierProcessCustomerName,cashierProcessCustomerType,cashierProcessOrderType,cashierProcessSubtotalPrice,cashierProcessStatusOrder;
    private ItemClickListener itemClickListener;

    public VHCashierProcess(@NonNull View itemView) {
        super(itemView);
        cashierProcessCustomerName = itemView.findViewById(R.id.txtRVCashierProcessCustomerName);
        cashierProcessCustomerType = itemView.findViewById(R.id.txtRVCashierProcessCustomerType);
        cashierProcessOrderType = itemView.findViewById(R.id.txtRVCashierProcessOrderType);
        cashierProcessSubtotalPrice = itemView.findViewById(R.id.txtRVCashierProcessSubtotalPrice);
        cashierProcessStatusOrder = itemView.findViewById(R.id.txtRVCashierProcessStatusOrder);
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

package com.hanindya.ag5s.ViewHolder.Cashier;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.R;

public class VHCashierComplete extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView customerName,customerType,orderType,totalBill,status;
    private ItemClickListener itemClickListener;

    public VHCashierComplete(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        customerName = itemView.findViewById(R.id.txtRVCashierCompleteCustomerName);
        customerType = itemView.findViewById(R.id.txtRVCashierCompleteCustomerType);
        orderType = itemView.findViewById(R.id.txtRVCashierCompleteOrderType);
        totalBill = itemView.findViewById(R.id.txtRVCashierCompleteSubtotalPrice);
        status = itemView.findViewById(R.id.txtRVCashierCompleteStatusOrder);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getBindingAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}

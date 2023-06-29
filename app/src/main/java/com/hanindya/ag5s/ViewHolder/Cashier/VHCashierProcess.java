package com.hanindya.ag5s.ViewHolder.Cashier;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.R;

public class VHCashierProcess extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView cashierProcessCustomerName,cashierProcessCustomerType,cashierProcessOrderType,cashierProcessSubtotalPrice,numberCount;
    private ItemClickListener itemClickListener;

    public VHCashierProcess(@NonNull View itemView) {
        super(itemView);
        cashierProcessCustomerName = itemView.findViewById(R.id.txtRVCashierCompleteCustName);
        cashierProcessCustomerType = itemView.findViewById(R.id.txtRVCashierCompleteCatCust);
        cashierProcessOrderType = itemView.findViewById(R.id.txtRVCashierCompleteOrderType);
        cashierProcessSubtotalPrice = itemView.findViewById(R.id.txtRVCashierCompleteSubtotalPrice);
        numberCount = itemView.findViewById(R.id.txtRVCashierCompleteNumber);
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

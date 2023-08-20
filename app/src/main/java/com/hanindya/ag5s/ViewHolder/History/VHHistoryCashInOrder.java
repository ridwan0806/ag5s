package com.hanindya.ag5s.ViewHolder.History;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.R;

public class VHHistoryCashInOrder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView historyCashInOrderName,historyCashInOrderBill,historyCashInOrderNo;
    private ItemClickListener itemClickListener;

    public VHHistoryCashInOrder(@NonNull View itemView) {
        super(itemView);
        historyCashInOrderName = itemView.findViewById(R.id.txtRVHistoryCashInOrderName);
        historyCashInOrderBill = itemView.findViewById(R.id.txtRVHistoryCashInOrderTotalBill);
        historyCashInOrderNo = itemView.findViewById(R.id.txtRVHistoryCashInOrderNo);
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

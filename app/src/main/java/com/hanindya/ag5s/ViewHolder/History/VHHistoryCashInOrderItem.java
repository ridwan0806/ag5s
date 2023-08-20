package com.hanindya.ag5s.ViewHolder.History;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.R;

public class VHHistoryCashInOrderItem extends RecyclerView.ViewHolder {

    public TextView historyCashInOrderItemNumberOfItem,historyCashInOrderItemNumberNameFood,historyCashInOrderItemNumberQty,historyCashInOrderItemNumberTotalBill;

    public VHHistoryCashInOrderItem(@NonNull View itemView) {
        super(itemView);
        historyCashInOrderItemNumberOfItem = itemView.findViewById(R.id.txtRVHistoryCashInOrderItemsNumber);
        historyCashInOrderItemNumberNameFood = itemView.findViewById(R.id.txtRVHistoryCashInOrderItemsNameFood);
        historyCashInOrderItemNumberQty = itemView.findViewById(R.id.txtRVHistoryCashInOrderItemsQty);
        historyCashInOrderItemNumberTotalBill = itemView.findViewById(R.id.txtRVHistoryCashInOrderItemsTotalBill);
    }
}

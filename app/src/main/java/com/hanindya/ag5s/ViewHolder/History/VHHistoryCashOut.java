package com.hanindya.ag5s.ViewHolder.History;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.R;

public class VHHistoryCashOut extends RecyclerView.ViewHolder {
    public TextView historyCashOutNonSuppliesNumber,historyCashOutNonSuppliesOrderDate,historyCashOutNonSuppliesTransaction,historyCashOutNonSuppliesSubtotal;
    public ImageView menuHistoryCashOutNonSuppliesOrderDate;

    public VHHistoryCashOut(@NonNull View itemView) {
        super(itemView);
        historyCashOutNonSuppliesNumber = itemView.findViewById(R.id.txtRVHistoryCashInNumber);;
        historyCashOutNonSuppliesOrderDate = itemView.findViewById(R.id.txtRVHistoryCashInDate);
        historyCashOutNonSuppliesTransaction = itemView.findViewById(R.id.txtRVHistoryCashInTotalTransaction);
        historyCashOutNonSuppliesSubtotal = itemView.findViewById(R.id.txtRVHistoryCashInTotalBill);
        menuHistoryCashOutNonSuppliesOrderDate = itemView.findViewById(R.id.imgRVHistoryCashInOpen);
    }
}

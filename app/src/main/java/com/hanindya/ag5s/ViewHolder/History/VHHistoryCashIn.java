package com.hanindya.ag5s.ViewHolder.History;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.R;

public class VHHistoryCashIn extends RecyclerView.ViewHolder {

    public TextView historyCashInNumber,historyCashInOrderDate,historyCashInTotalTransaction,historyCashInTotalBill;
    public ImageView menuHistoryCashIn;

    public VHHistoryCashIn(@NonNull View itemView) {
        super(itemView);
        historyCashInNumber = itemView.findViewById(R.id.txtRVHistoryCashInNumber);
        historyCashInOrderDate = itemView.findViewById(R.id.txtRVHistoryCashInDate);
        historyCashInTotalTransaction = itemView.findViewById(R.id.txtRVHistoryCashInTotalTransaction);
        historyCashInTotalBill = itemView.findViewById(R.id.txtRVHistoryCashInTotalBill);
        menuHistoryCashIn = itemView.findViewById(R.id.imgRVHistoryCashInOpen);
    }
}

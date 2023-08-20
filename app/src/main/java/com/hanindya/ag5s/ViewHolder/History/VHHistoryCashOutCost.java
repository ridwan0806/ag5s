package com.hanindya.ag5s.ViewHolder.History;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.R;

public class VHHistoryCashOutCost extends RecyclerView.ViewHolder {

    public TextView historyCashOutCostNumber,historyCashOutCostName,historyCashOutCostSubtotal,historyCashOutCostNotes;
    public ImageView historyCashOutCostMenu;

    public VHHistoryCashOutCost(@NonNull View itemView) {
        super(itemView);
        historyCashOutCostNumber = itemView.findViewById(R.id.txtRVHistoryCashOutCostNumber);
        historyCashOutCostName = itemView.findViewById(R.id.txtRVHistoryCashOutCostName);
        historyCashOutCostSubtotal = itemView.findViewById(R.id.txtRVHistoryCashOutCostSubtotal);
        historyCashOutCostNotes = itemView.findViewById(R.id.txtRVHistoryCashOutCostNotes);
        historyCashOutCostMenu = itemView.findViewById(R.id.imgRVHistoryCashOutCostMenu);
    }
}

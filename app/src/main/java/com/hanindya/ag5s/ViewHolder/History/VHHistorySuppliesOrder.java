package com.hanindya.ag5s.ViewHolder.History;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.R;

public class VHHistorySuppliesOrder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView VHHistorySuppliesOrderDate,VHHistorySuppliesOrderSubtotal,VHHistorySuppliesOrderNotes,VHHistorySuppliesOrderCreatedBy,VHHistorySuppliesOrderNumber;
    public ImageView VHHistorySuppliesOrderMenu;
    private ItemClickListener itemClickListener;

    public VHHistorySuppliesOrder(@NonNull View itemView) {
        super(itemView);
        VHHistorySuppliesOrderNumber = itemView.findViewById(R.id.txtRVHistorySuppliesOrderNumber);
        VHHistorySuppliesOrderDate = itemView.findViewById(R.id.txtRVHistorySuppliesOrderDate);
        VHHistorySuppliesOrderSubtotal = itemView.findViewById(R.id.txtRVHistorySuppliesOrderSubtotal);
        VHHistorySuppliesOrderNotes = itemView.findViewById(R.id.txtRVHistorySuppliesOrderNotes);
        VHHistorySuppliesOrderCreatedBy = itemView.findViewById(R.id.txtRVHistorySuppliesOrderCreatedBy);
        VHHistorySuppliesOrderMenu = itemView.findViewById(R.id.imgRVHistorySuppliesOrderMenu);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getBindingAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}

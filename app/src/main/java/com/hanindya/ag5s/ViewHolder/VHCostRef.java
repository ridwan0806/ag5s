package com.hanindya.ag5s.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.R;

public class VHCostRef extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView costRefName,costRefNumber;
    private ItemClickListener itemClickListener;

    public VHCostRef(@NonNull View itemView) {
        super(itemView);
        costRefNumber = itemView.findViewById(R.id.txtRVCostNumber);
        costRefName = itemView.findViewById(R.id.txtRVCostRef);
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

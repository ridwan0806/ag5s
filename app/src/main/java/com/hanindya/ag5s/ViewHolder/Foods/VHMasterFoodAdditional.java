package com.hanindya.ag5s.ViewHolder.Foods;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.R;

public class VHMasterFoodAdditional extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView masterAdditionalTxtName,masterAdditionalTxtPrice,masterAdditionalTxtCategory;
    public ImageView masterAdditionalMenu;
    private ItemClickListener itemClickListener;

    public VHMasterFoodAdditional(@NonNull View itemView) {
        super(itemView);
        masterAdditionalTxtName = itemView.findViewById(R.id.txtRVMasterFoodAdditionalName);
        masterAdditionalTxtPrice = itemView.findViewById(R.id.txtRVMasterFoodAdditionalPrice);
        masterAdditionalTxtCategory = itemView.findViewById(R.id.txtRVMasterFoodAdditionalCategory);
        masterAdditionalMenu = itemView.findViewById(R.id.txtRVMasterFoodAdditionalMenu);
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

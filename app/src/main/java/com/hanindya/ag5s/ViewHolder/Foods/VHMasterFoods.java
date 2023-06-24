package com.hanindya.ag5s.ViewHolder.Foods;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.R;

public class VHMasterFoods extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView masterFoodTxtName,masterFoodTxtPrice,masterFoodTxtCategory;
    public ImageView masterFoodMenu;
    private ItemClickListener itemClickListener;

    public VHMasterFoods(@NonNull View itemView) {
        super(itemView);
        masterFoodTxtName = itemView.findViewById(R.id.txtRVMasterFoodAdditionalName);
        masterFoodTxtPrice = itemView.findViewById(R.id.txtRVMasterFoodAdditionalPrice);
        masterFoodTxtCategory = itemView.findViewById(R.id.txtRVMasterFoodAdditionalCategory);
        masterFoodMenu = itemView.findViewById(R.id.txtRVMasterFoodAdditionalMenu);
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

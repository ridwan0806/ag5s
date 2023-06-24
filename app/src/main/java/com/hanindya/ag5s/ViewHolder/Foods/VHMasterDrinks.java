package com.hanindya.ag5s.ViewHolder.Foods;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.R;

public class VHMasterDrinks extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView masterDrinkTxtName,masterDrinkTxtPrice,masterDrinkTxtCategory;
    public ImageView masterDrinkMenu;
    private ItemClickListener itemClickListener;

    public VHMasterDrinks(@NonNull View itemView) {
        super(itemView);
        masterDrinkTxtName = itemView.findViewById(R.id.txtRVMasterFoodAdditionalName);
        masterDrinkTxtPrice = itemView.findViewById(R.id.txtRVMasterFoodAdditionalPrice);
        masterDrinkTxtCategory = itemView.findViewById(R.id.txtRVMasterFoodAdditionalCategory);
        masterDrinkMenu = itemView.findViewById(R.id.txtRVMasterFoodAdditionalMenu);
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

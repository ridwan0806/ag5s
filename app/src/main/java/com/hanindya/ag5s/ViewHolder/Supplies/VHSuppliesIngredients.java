package com.hanindya.ag5s.ViewHolder.Supplies;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.R;

public class VHSuppliesIngredients extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView suppliesIngredientsName,suppliesIngredientsCategory;
    public ImageView suppliesIngredientsOptions;
    private ItemClickListener itemClickListener;

    public VHSuppliesIngredients(@NonNull View itemView) {
        super(itemView);
        suppliesIngredientsName = itemView.findViewById(R.id.txtRVSuppliesIngredientsName);
        suppliesIngredientsCategory = itemView.findViewById(R.id.txtRVSuppliesIngredientsCategory);
        suppliesIngredientsOptions = itemView.findViewById(R.id.imgRVSuppliesIngredients);
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

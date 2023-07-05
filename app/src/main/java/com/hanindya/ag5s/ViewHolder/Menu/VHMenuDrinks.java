package com.hanindya.ag5s.ViewHolder.Menu;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.R;

public class VHMenuDrinks extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView menuDrinkName,menuDrinkPrice;
    public ImageView menuDrinkOptions;
    private ItemClickListener itemClickListener;

    public VHMenuDrinks(@NonNull View itemView) {
        super(itemView);
        menuDrinkName = itemView.findViewById(R.id.txtRVMenuDrinksName);
        menuDrinkPrice = itemView.findViewById(R.id.txtRVMenuDrinksPrice);
        menuDrinkOptions = itemView.findViewById(R.id.imgRVMenuDrinks);
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

package com.hanindya.ag5s.ViewHolder.Menu;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.R;

public class VHMenuFoods extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView menuFoodName,menuFoodPrice;
    public ImageView menuFoodOptions;
    private ItemClickListener itemClickListener;

    public VHMenuFoods(@NonNull View itemView) {
        super(itemView);
        menuFoodName = itemView.findViewById(R.id.txtRVMenuFoodsName);
        menuFoodPrice = itemView.findViewById(R.id.txtRVMenuFoodsPrice);
        menuFoodOptions = itemView.findViewById(R.id.imgRVMenuFoods);
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

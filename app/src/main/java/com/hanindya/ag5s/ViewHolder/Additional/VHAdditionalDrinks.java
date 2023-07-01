package com.hanindya.ag5s.ViewHolder.Additional;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.R;

public class VHAdditionalDrinks extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtAdditionalFoodName,txtAdditionalFoodPrice,txtAdditionalFoodCategory;
    private ItemClickListener itemClickListener;

    public VHAdditionalDrinks(@NonNull View itemView) {
        super(itemView);
        txtAdditionalFoodName = itemView.findViewById(R.id.txtRVAdditionalDrinksFoodName);
        txtAdditionalFoodPrice = itemView.findViewById(R.id.txtRVAdditionalDrinksFoodPrice);
        txtAdditionalFoodCategory = itemView.findViewById(R.id.txtRVAdditionalDrinksFoodCategory);
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

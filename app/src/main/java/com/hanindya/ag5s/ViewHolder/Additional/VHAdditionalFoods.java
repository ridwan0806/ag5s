package com.hanindya.ag5s.ViewHolder.Additional;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.R;

public class VHAdditionalFoods extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtAdditionalFoodName,txtAdditionalFoodPrice;
    private ItemClickListener itemClickListener;

    public VHAdditionalFoods(@NonNull View itemView) {
        super(itemView);
        txtAdditionalFoodName = itemView.findViewById(R.id.txtRVAdditionalFoodsFoodName);
        txtAdditionalFoodPrice = itemView.findViewById(R.id.txtRVAdditionalFoodsFoodPrice);
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

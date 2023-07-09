package com.hanindya.ag5s.ViewHolder.Supplies;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.R;

public class VHSuppliesCore extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView suppliesCoreName,suppliesCoreCategory;
    public ImageView suppliesCoreOptions;
    private ItemClickListener itemClickListener;

    public VHSuppliesCore(@NonNull View itemView) {
        super(itemView);
        suppliesCoreName = itemView.findViewById(R.id.txtRVSuppliesCoreName);
        suppliesCoreCategory = itemView.findViewById(R.id.txtRVSuppliesCoreCategory);
        suppliesCoreOptions = itemView.findViewById(R.id.imgRVSuppliesCore);
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

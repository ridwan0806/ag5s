package com.hanindya.ag5s.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.Activity.CartSuppliesItem;
import com.hanindya.ag5s.Helper.DatabaseSuppliesOrderItem;
import com.hanindya.ag5s.Model.SuppliesOrderItem;
import com.hanindya.ag5s.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class CartSuppliesItemAdapter extends RecyclerView.Adapter<CartSuppliesItemAdapter.ViewHolder> {

    private Context context;
    private List<SuppliesOrderItem> suppliesItem;

    public CartSuppliesItemAdapter(Context context, List<SuppliesOrderItem>suppliesItem){
        this.context = context;
        this.suppliesItem = suppliesItem;
    }

    @NonNull
    @Override
    public CartSuppliesItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_rv_cart_supplies_item,parent,false);
        return new CartSuppliesItemAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartSuppliesItemAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SuppliesOrderItem list = suppliesItem.get(position);
        int number = position + 1;

        holder.number.setText(String.valueOf(number));
        holder.name.setText(list.getName());
        holder.qty.setText(list.getQty());
        holder.units.setText(list.getUnits());
        holder.notes.setText(list.getNotes());

        NumberFormat formatRp = new DecimalFormat("#,###");
        double price = Math.round(list.getPrice());
        double subtotal = Math.round(list.getSubtotal());

        holder.price.setText(formatRp.format(price));
        holder.subtotal.setText(formatRp.format(subtotal));

        holder.menuOptions.setOnClickListener(view -> {
            AlertDialog.Builder confirmDelete = new AlertDialog.Builder(context);
            confirmDelete.setCancelable(false);
            confirmDelete.setMessage("Hapus "+list.getName()+" ?");

            confirmDelete.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            confirmDelete.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DatabaseSuppliesOrderItem db = new DatabaseSuppliesOrderItem(context);
                    db.deleteItem(Integer.parseInt(list.getId()));
                    suppliesItem.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, ""+list.getName()+" dihapus dari pesanan", Toast.LENGTH_SHORT).show();
                }
            });

            confirmDelete.show();
        });
    }

    @Override
    public int getItemCount() {
        return suppliesItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView number,name,category,notes,qty,units,price,subtotal;
        ImageView menuOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.txtCartSuppliesNoUrut);
            name = itemView.findViewById(R.id.txtCartSuppliesName);
            notes = itemView.findViewById(R.id.txtCartSuppliesNotes);
            qty = itemView.findViewById(R.id.txtCartSuppliesQty);
            units = itemView.findViewById(R.id.txtCartSuppliesUnits);
            price = itemView.findViewById(R.id.txtCartSuppliesPrice);
            subtotal = itemView.findViewById(R.id.txtCartSuppliesSubtotal);
            menuOptions = itemView.findViewById(R.id.imgMenuCartSupplies);
        }
    }
}

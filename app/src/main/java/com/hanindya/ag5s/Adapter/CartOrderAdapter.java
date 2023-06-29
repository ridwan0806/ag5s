package com.hanindya.ag5s.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.hanindya.ag5s.Helper.DatabaseOrderItem;
import com.hanindya.ag5s.Model.OrderItem;
import com.hanindya.ag5s.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class CartOrderAdapter extends RecyclerView.Adapter<CartOrderAdapter.ViewHolder> {
    private Context context;
    private List<OrderItem> orderItem;

    public CartOrderAdapter(Context context, List<OrderItem> orderItem) {
        this.context = context;
        this.orderItem = orderItem;
    }

    @NonNull
    @Override
    public CartOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_rv_cart_order,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartOrderAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        OrderItem list = orderItem.get(position);

        int number = position + 1;
        holder.numberCount.setText(number+".");

        NumberFormat formatRp = new DecimalFormat("#,###");
        double price = Math.round(list.getPrice());
        double subtotal = Math.round(list.getSubtotal());

        holder.foodName.setText(list.getFoodName());
        holder.qty.setText(String.valueOf(list.getQty()));
        holder.price.setText(formatRp.format(price));
        holder.subtotal.setText(formatRp.format(subtotal));
        
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(),holder.menu);
                popupMenu.getMenuInflater().inflate(R.menu.menu_cart_order,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        if (itemId == R.id.cart_order_edit_qty){
                            Toast.makeText(context, "Fitur Ubah Qty Belum Siap", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.cart_order_edit_price){
                            Toast.makeText(context, "Fitur Edit Harga Belum Siap", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.cart_order_delete_food){
                            AlertDialog.Builder confirmDelete = new AlertDialog.Builder(context);
                            confirmDelete.setCancelable(false);
                            confirmDelete.setMessage("Hapus "+list.getFoodName()+" dari daftar pesanan ?");

                            confirmDelete.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            confirmDelete.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DatabaseOrderItem db = new DatabaseOrderItem(context);
                                    db.delete(Integer.parseInt(list.getId()));
                                    orderItem.remove(position);
                                    notifyItemRemoved(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, ""+list.getFoodName()+" dihapus dari pesanan", Toast.LENGTH_SHORT).show();
                                }
                            });
                            confirmDelete.show();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView foodName,qty,price,subtotal,numberCount;
        ImageView menu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.txtCartOrderName);
            qty = itemView.findViewById(R.id.txtCartOrderQty);
            price = itemView.findViewById(R.id.txtCartOrderPrice);
            subtotal = itemView.findViewById(R.id.txtCartOrderSubtotal);
            menu = itemView.findViewById(R.id.imgMenuCartOrder);
            numberCount = itemView.findViewById(R.id.txtCartOrderNoUrut);
        }
    }

}

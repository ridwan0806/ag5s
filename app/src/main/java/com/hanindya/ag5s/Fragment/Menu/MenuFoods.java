package com.hanindya.ag5s.Fragment.Menu;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanindya.ag5s.Helper.DatabaseOrderItem;
import com.hanindya.ag5s.Helper.MoneyTextWatcher;
import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.Model.Foods;
import com.hanindya.ag5s.Model.Menu;
import com.hanindya.ag5s.Model.OrderItem;
import com.hanindya.ag5s.R;
import com.hanindya.ag5s.ViewHolder.Menu.VHMenuFoods;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFoods#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFoods extends Fragment {
    FirebaseUser firebaseUser;
    DatabaseReference root,dbMenu,dbUser;
    String userId,branchName,userName;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progressBar;
    FirebaseRecyclerAdapter<Menu, VHMenuFoods> adapter;

    ImageView plusBtn,minusBtn;
    TextView qty;
    int numberOrder = 1;
    EditText etEditPrice,etEditName;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MenuFoods() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMenuFoods.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFoods newInstance(String param1, String param2) {
        MenuFoods fragment = new MenuFoods();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_menu_foods,container,false);
        recyclerView = layout.findViewById(R.id.rv_menu_foods);
        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = layout.findViewById(R.id.pb_menu_food);

        root = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();
        dbUser = root.child("Users").child(userId);

        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                branchName = snapshot.child("branch").getValue(String.class);
                userName = snapshot.child("username").getValue(String.class);
                dbMenu = root.child("Menu").child(branchName).child("Foods");
                getMenuFoods();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });

        return layout;
    }

    private void getMenuFoods() {
        FirebaseRecyclerOptions<Menu> list =
                new FirebaseRecyclerOptions.Builder<Menu>()
                        .setQuery(dbMenu.orderByChild("name"),Menu.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Menu, VHMenuFoods>(list) {
            @Override
            protected void onBindViewHolder(@NonNull VHMenuFoods holder, int position, @NonNull Menu model) {
                String foodId = adapter.getRef(position).getKey();
                String foodName = adapter.getItem(position).getName();
                String foodPrice = String.valueOf(adapter.getItem(position).getPrice());

                NumberFormat formatRp = new DecimalFormat("#,###");
                double price = model.getPrice();

                holder.menuFoodName.setText(model.getName());
                holder.menuFoodPrice.setText(formatRp.format(price));
                
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        addQty(foodId,foodName,foodPrice);
                    }
                });

                holder.menuFoodOptions.setOnClickListener(view -> {
                    PopupMenu popupMenu = new PopupMenu(getContext(),holder.menuFoodOptions);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_foods,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            int itemId = menuItem.getItemId();
                            if (itemId == R.id.menuFoodEditName){
                                editFoodName(foodId,foodName);
                            } else if (itemId == R.id.menuFoodEditPrice){
                                editFoodPrice(foodId,foodName,foodPrice);
                            } else if (itemId == R.id.menuFoodDeleteItem){
                                deleteFood(foodId,foodName);
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                });
            }

            @NonNull
            @Override
            public VHMenuFoods onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_rv_menu_foods,parent,false);
                return new VHMenuFoods(view);
            }

            @Override
            public void onDataChanged(){
                if (progressBar!=null){
                    progressBar.setVisibility(View.GONE);
                }
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void editFoodName(String foodId, String foodName) {
        AlertDialog.Builder editName = new AlertDialog.Builder(getContext());
        editName.setCancelable(false);
        editName.setMessage("ubah menu "+foodName+" ?");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogEditName = layoutInflater.inflate(R.layout.dialog_edit_foodname,null);
        editName.setView(dialogEditName);

        etEditName = dialogEditName.findViewById(R.id.etFragmentFoodEditName);

        editName.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        editName.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (etEditName.getText().toString().length() == 0){
                    Toast.makeText(getContext(), "Gagal. Nama item belum diisi", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long serverTimeOffset = snapshot.getValue(Long.class);
                            long estimateServerTime = System.currentTimeMillis()+serverTimeOffset;

                            SimpleDateFormat editedDateTime;
                            editedDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date dateNow = new Date(estimateServerTime);

                            String newFoodName = etEditName.getText().toString();

                            //Edit foodName in Firebase
                            dbMenu.child(foodId).child("name").setValue(newFoodName);
                            dbMenu.child(foodId).child("editedBy").setValue(userName);
                            dbMenu.child(foodId).child("editedDateTime").setValue(editedDateTime.format(dateNow));

                            Toast.makeText(getContext(), foodName+" berhasil diubah", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            throw error.toException();
                        }
                    });
                }
            }
        });
        editName.show();
    }

    private void editFoodPrice(String foodId, String foodName, String foodPrice) {
        AlertDialog.Builder editPrice = new AlertDialog.Builder(getContext());
        editPrice.setCancelable(false);
        editPrice.setMessage("Ubah harga "+foodName+" ?");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogEditPrice = layoutInflater.inflate(R.layout.dialog_edit_price,null);
        editPrice.setView(dialogEditPrice);

        etEditPrice = dialogEditPrice.findViewById(R.id.etFragmentFoodEditPrice);
        etEditPrice.addTextChangedListener(new MoneyTextWatcher(etEditPrice));

        editPrice.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        
        editPrice.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                BigDecimal value = MoneyTextWatcher.parseCurrencyValue(etEditPrice.getText().toString());
                String price = String.valueOf(value);

                if (Double.parseDouble(foodPrice) == Double.parseDouble(price)){
                    Toast.makeText(getContext(), "Gagal. Nominal masih sama", Toast.LENGTH_SHORT).show();
                } else {
                    if (etEditPrice.getText().toString().length() == 0 || etEditPrice.getText().toString().equals("Rp 0")){
                        Toast.makeText(getContext(), "Gagal. nominal invalid", Toast.LENGTH_SHORT).show();
                    } else {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                long serverTimeOffset = snapshot.getValue(Long.class);
                                long estimateServerTime = System.currentTimeMillis()+serverTimeOffset;

                                SimpleDateFormat editedDateTime;
                                editedDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date dateNow = new Date(estimateServerTime);

                                //Edit foodName in Firebase
                                dbMenu.child(foodId).child("price").setValue(Double.parseDouble(price));
                                dbMenu.child(foodId).child("editedBy").setValue(userName);
                                dbMenu.child(foodId).child("editedDateTime").setValue(editedDateTime.format(dateNow));

                                Toast.makeText(getContext(), foodName+" berhasil diubah", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                throw error.toException();
                            }
                        });
                    }
                }
            }
        });
        editPrice.show();
    }

    private void deleteFood(String foodId, String foodName) {
        AlertDialog.Builder confirmDelete = new AlertDialog.Builder(getContext());
        confirmDelete.setCancelable(false);
        confirmDelete.setMessage("Hapus "+foodName+" dari Database ?");

        confirmDelete.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        confirmDelete.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Delete Foods from firebase
                dbMenu.child(foodId).removeValue();
                Toast.makeText(getContext(), foodName+" Berhasil dihapus", Toast.LENGTH_SHORT).show();
            }
        });
        confirmDelete.show();
    }

    private void addQty(String foodId, String foodName, String foodPrice) {
        AlertDialog.Builder addQty = new AlertDialog.Builder(getContext());
        addQty.setCancelable(false);
        addQty.setMessage("Tambahkan "+foodName+" ke pesanan ?");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogQty = layoutInflater.inflate(R.layout.dialog_edit_qty,null);
        addQty.setView(dialogQty);

        plusBtn = dialogQty.findViewById(R.id.btnPlus);
        minusBtn = dialogQty.findViewById(R.id.btnMinus);
        qty = dialogQty.findViewById(R.id.txtQty);

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOrder = numberOrder + 1;
                qty.setText(String.valueOf(numberOrder));
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberOrder > 1){
                    int newNumberOrder = Integer.parseInt(qty.getText().toString());
                    numberOrder = newNumberOrder - 1;
                    qty.setText(String.valueOf(numberOrder));
                }
            }
        });

        addQty.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                numberOrder = 1;
            }
        });

        addQty.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseOrderItem dbLocal = new DatabaseOrderItem(getContext());
                int result = dbLocal.checkItemExist(foodId);
                if (result == 0){
                    AlertDialog.Builder failed = new AlertDialog.Builder(getContext());
                    failed.setCancelable(false);
                    failed.setTitle("Error");
                    failed.setMessage(foodName.toUpperCase(Locale.ROOT)+" sudah ada");

                    failed.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            numberOrder = 1;
                        }
                    });
                    failed.show();
                } else {
                    Double subtotal = Double.parseDouble(foodPrice) * numberOrder;
                    dbLocal.addToCart(new OrderItem(
                            "",
                            foodId,
                            foodName,
                            numberOrder,
                            Double.parseDouble(foodPrice),
                            subtotal
                    ));
                    Toast.makeText(getContext(), foodName+" berhasil ditambah ke pesanan", Toast.LENGTH_SHORT).show();
                    numberOrder = 1;
                }
            }
        });
        addQty.show();
    }
}
package com.hanindya.ag5s.Fragment.Supplies;

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
import android.widget.PopupMenu;
import android.widget.ProgressBar;
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
import com.hanindya.ag5s.Interface.ItemClickListener;
import com.hanindya.ag5s.Model.Menu;
import com.hanindya.ag5s.Model.Supplies;
import com.hanindya.ag5s.R;
import com.hanindya.ag5s.ViewHolder.Menu.VHMenuFoods;
import com.hanindya.ag5s.ViewHolder.Supplies.VHSuppliesCore;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuppliesCore#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuppliesCore extends Fragment {
    DatabaseReference dbSupplies;
    String userId,branchName,userName;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progressBar;
    FirebaseRecyclerAdapter<Supplies, VHSuppliesCore> adapter;

    EditText etSuppliesName;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SuppliesCore() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SuppliesCore.
     */
    // TODO: Rename and change types and number of parameters
    public static SuppliesCore newInstance(String param1, String param2) {
        SuppliesCore fragment = new SuppliesCore();
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
        View layout = inflater.inflate(R.layout.fragment_supplies_core,container,false);
        recyclerView = layout.findViewById(R.id.rv_supplies_core);
        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = layout.findViewById(R.id.pb_supplies_core);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbUser = root.child("Users").child(userId);

        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                branchName = snapshot.child("branch").getValue(String.class);
                userName = snapshot.child("username").getValue(String.class);
                dbSupplies = root.child("Supplies").child(branchName).child("Core");
                getListSuppliesCore();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });

        return layout;
    }

    private void getListSuppliesCore() {
        FirebaseRecyclerOptions<Supplies> list =
                new FirebaseRecyclerOptions.Builder<Supplies>()
                        .setQuery(dbSupplies.orderByChild("name"),Supplies.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Supplies, VHSuppliesCore>(list) {
            @Override
            protected void onBindViewHolder(@NonNull VHSuppliesCore holder, int position, @NonNull Supplies model) {
                String suppliesId = adapter.getRef(position).getKey();
                String suppliesName = adapter.getItem(position).getName();

                String categoryValue = model.getCategory();
                String category = "";
                if (categoryValue.equals("Core")){
                    category = "Bahan Utama";
                } else {
                    category = "Unknown";
                }

                holder.suppliesCoreName.setText(model.getName());
                holder.suppliesCoreCategory.setText(category);

                holder.suppliesCoreOptions.setOnClickListener(view -> {
                    PopupMenu popupMenu = new PopupMenu(getContext(),holder.suppliesCoreOptions);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_supplies_options,popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            int itemId = menuItem.getItemId();
                            if (itemId == R.id.supplies_option_edit_name){
                                editSuppliesName(suppliesId,suppliesName);
                            } else if (itemId == R.id.supplies_option_delete){
                                deleteSupplies(suppliesId,suppliesName);
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                });
                
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        addSuppliesCore();
                    }
                });
            }

            @NonNull
            @Override
            public VHSuppliesCore onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_rv_supplies_core,parent,false);
                return new VHSuppliesCore(view);
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

    private void deleteSupplies(String suppliesId, String suppliesName) {
        AlertDialog.Builder confirmDelete = new AlertDialog.Builder(getContext());
        confirmDelete.setCancelable(false);
        confirmDelete.setMessage("Hapus "+suppliesName+" ?");

        confirmDelete.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        confirmDelete.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbSupplies.child(suppliesId).removeValue();
                Toast.makeText(getContext(), suppliesName+" berhasil dihapus", Toast.LENGTH_SHORT).show();
            }
        });

        confirmDelete.show();
    }

    private void editSuppliesName(String suppliesId, String suppliesName) {
        AlertDialog.Builder editNameSupplies = new AlertDialog.Builder(getContext());
        editNameSupplies.setCancelable(false);
        editNameSupplies.setMessage("Edit "+suppliesName);

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.dialog_edit_supplies_name,null);
        editNameSupplies.setView(layout);

        etSuppliesName = layout.findViewById(R.id.etEditSuppliesName);

        editNameSupplies.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        editNameSupplies.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (etSuppliesName.getText().toString().length() == 0){
                    Toast.makeText(getContext(), "GAGAL. invalid input", Toast.LENGTH_SHORT).show();
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

                            String upperText;
                            upperText = etSuppliesName.getText().toString();
                            upperText = upperText.toUpperCase();

                            dbSupplies.child(suppliesId).child("name").setValue(upperText);
                            dbSupplies.child(suppliesId).child("editedBy").setValue(userName);
                            dbSupplies.child(suppliesId).child("editedDateTime").setValue(editedDateTime.format(dateNow));

                            Toast.makeText(getContext(), suppliesName+" berhasil diubah", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            throw error.toException();
                        }
                    });
                }
            }
        });

        editNameSupplies.show();
    }

    private void addSuppliesCore() {
        Toast.makeText(getContext(), "Add Supplies core", Toast.LENGTH_SHORT).show();
    }
}
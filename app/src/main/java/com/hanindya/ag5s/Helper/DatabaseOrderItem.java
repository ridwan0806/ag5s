package com.hanindya.ag5s.Helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hanindya.ag5s.Model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class DatabaseOrderItem extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Order";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "OrderItem";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FOOD_ID = "foodId";
    private static final String COLUMN_FOOD_NAME = "foodName";
    private static final String COLUMN_QUANTITY = "qty";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_SUBTOTAL = "subtotal";

    public DatabaseOrderItem(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FOOD_ID + " TEXT, " +
                COLUMN_FOOD_NAME + " TEXT, " +
                COLUMN_QUANTITY + " TEXT, " +
                COLUMN_PRICE + " TEXT, " +
                COLUMN_SUBTOTAL + " TEXT);";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public int checkItemExist(String row_id)
    {
        int check = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT foodId FROM OrderItem WHERE foodId = ?",new String[] {row_id});
        if (result.moveToFirst()) {
            return check;
        }
        else {
            check = 1;
        }
        return check;
    }

    public void addToCart(OrderItem orderItem)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FOOD_ID, orderItem.getFoodId());
        cv.put(COLUMN_FOOD_NAME, orderItem.getFoodName());
        cv.put(COLUMN_QUANTITY, orderItem.getQty());
        cv.put(COLUMN_PRICE, orderItem.getPrice());
        cv.put(COLUMN_SUBTOTAL, orderItem.getSubtotal());

        db.insert(TABLE_NAME,null,cv);
    }

    @SuppressLint("Range")
    public List<OrderItem> getAllOrderItems() {
        List<OrderItem> itemList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                OrderItem order = new OrderItem();
                order.setId(cursor.getString(cursor.getColumnIndex("id")));
                order.setFoodId(cursor.getString(cursor.getColumnIndex("foodId")));
                order.setFoodName(cursor.getString(cursor.getColumnIndex("foodName")));
                order.setQty(Integer.parseInt(cursor.getString(cursor.getColumnIndex("qty"))));
                order.setPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex("price"))));
                order.setSubtotal(Double.parseDouble(cursor.getString(cursor.getColumnIndex("subtotal"))));
                itemList.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return itemList;
    }

    public void cleanAll(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "DELETE FROM " + TABLE_NAME;
        db.execSQL(query);
    }

    public void updateCart(String row_id, String price, String qty)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PRICE,price);
        cv.put(COLUMN_QUANTITY,qty);

        int result = db.update(TABLE_NAME,cv,"id=?",new String[] {row_id});

        if (result == -1){
            Toast.makeText(context, "Data gagal diubah", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Data berhasil diubah", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteCart(String Row_id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        int result = db.delete(TABLE_NAME,"id=?",new String[] {Row_id});

        if (result == -1){
            Toast.makeText(context, "Data gagal dihapus", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
        }
    }

    public void delete(int id){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "DELETE FROM OrderItem WHERE id="+id;
        database.execSQL(query);
    }

//    public String checkItemInCart(){
//        SQLiteDatabase db = this.getReadableDatabase();
//        String check = "";
//        Cursor cursor = db.rawQuery("SELECT COUNT(productId) productId FROM " + TABLE_NAME, null);
//        if (cursor.moveToFirst()) {
//            String result = cursor.getString('productId');
//            if (result == "0"){
//                check = "0";
//            } else {
//                check = "1";
//            }
//        }
//
//        return check;
//    }
}

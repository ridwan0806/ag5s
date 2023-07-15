package com.hanindya.ag5s.Helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.hanindya.ag5s.Model.OrderItem;
import com.hanindya.ag5s.Model.SuppliesOrderItem;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSuppliesOrderItem extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Supplies";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "SuppliesItem";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_NOTES = "notes";
    private static final String COLUMN_QTY = "qty";
    private static final String COLUMN_UNITS = "units";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_SUBTOTAL = "subtotal";

    public DatabaseSuppliesOrderItem(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_NOTES + " TEXT, " +
                COLUMN_QTY + " TEXT, " +
                COLUMN_UNITS + " TEXT, " +
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
        Cursor result = db.rawQuery("SELECT name FROM SuppliesItem WHERE name = ?",new String[] {row_id});
        if (result.moveToFirst()) {
            return check;
        }
        else {
            check = 1;
        }
        return check;
    }

    public void addToCart(SuppliesOrderItem suppliesOrderItem)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, suppliesOrderItem.getName());
        cv.put(COLUMN_CATEGORY, suppliesOrderItem.getCategory());
        cv.put(COLUMN_NOTES, suppliesOrderItem.getNotes());
        cv.put(COLUMN_QTY, suppliesOrderItem.getQty());
        cv.put(COLUMN_UNITS, suppliesOrderItem.getUnits());
        cv.put(COLUMN_PRICE, suppliesOrderItem.getPrice());
        cv.put(COLUMN_SUBTOTAL, suppliesOrderItem.getSubtotal());

        db.insert(TABLE_NAME,null,cv);
    }

    @SuppressLint("Range")
    public List<SuppliesOrderItem> getAllSuppliesOrderItems() {
        List<SuppliesOrderItem> itemList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                SuppliesOrderItem suppliesOrderItem = new SuppliesOrderItem();
                suppliesOrderItem.setId(cursor.getString(cursor.getColumnIndex("id")));
                suppliesOrderItem.setName(cursor.getString(cursor.getColumnIndex("name")));
                suppliesOrderItem.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                suppliesOrderItem.setNotes(cursor.getString(cursor.getColumnIndex("notes")));
                suppliesOrderItem.setQty(cursor.getString(cursor.getColumnIndex("qty")));
                suppliesOrderItem.setUnits(cursor.getString(cursor.getColumnIndex("units")));
                suppliesOrderItem.setPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex("price"))));
                suppliesOrderItem.setSubtotal(Double.parseDouble(cursor.getString(cursor.getColumnIndex("subtotal"))));
                itemList.add(suppliesOrderItem);
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
}

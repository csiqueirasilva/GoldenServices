package br.uva.goldenservices.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.uva.goldenservices.database.recibos.Recibo;

/**
 * Created by csiqueira on 18/06/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "GOLDEN_SERVICES_RECIBOS";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table recibos (desc text, tipo text, id integer primary key)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists recibos");
        onCreate(db);
    }

    public void inserirRecibo(String data, Recibo.Tipo tipo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("desc", data);
        contentValues.put("tipo", tipo.toString());
        db.insert("recibos", null, contentValues);
    }

    private List<Recibo> obterRecibos(Recibo.Tipo tipo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from recibos where tipo = " + tipo.toString(), null);
        res.moveToFirst();

        List<Recibo> ret = new ArrayList<Recibo>();

        while(res.isAfterLast() == false) {
            Recibo r = new Recibo();
            r.setDesc(res.getString(res.getColumnIndex("desc")));
            r.setTipo(Recibo.Tipo.valueOf(res.getString(res.getColumnIndex("tipo"))));
            ret.add(r);
            res.moveToNext();
        }

        return ret;
    }

    public List<Recibo> obterRecibosCliente() {
        return obterRecibos(Recibo.Tipo.CLIENTE);
    }

    public List<Recibo> obterRecibosPrestador() {
        return obterRecibos(Recibo.Tipo.PRESTADOR);
    }
}

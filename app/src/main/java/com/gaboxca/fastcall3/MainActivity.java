package com.gaboxca.fastcall3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_marca1;
    Button btn_marca2;
    Button btn_marca3;
    Button btn_marca4;
    ImageButton imb_edita;
    private SQLiteDatabase db;
    private String tel;
    private Cursor cursor;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_marca1 = findViewById(R.id.btn_marca1);
        btn_marca2 = findViewById(R.id.btn_marca2);
        btn_marca3 = findViewById(R.id.btn_marca3);
        btn_marca4 = findViewById(R.id.btn_marca4);
        imb_edita = findViewById(R.id.imb_editar);

        String font_path = "font/teenbd.ttf";
        Typeface TF = Typeface.createFromAsset(getAssets(), font_path);
        btn_marca1.setTypeface(TF);
        btn_marca2.setTypeface(TF);
        btn_marca3.setTypeface(TF);
        btn_marca4.setTypeface(TF);

        intent = new Intent(MainActivity.this,edit.class);


        ApoyoDB miDB = new ApoyoDB(this, "DBAgenda.dbf",null,1);
        db = miDB.getWritableDatabase();

        arranca();

        consulta();
        btn_marca1.setOnClickListener(this);
        btn_marca2.setOnClickListener(this);
        btn_marca3.setOnClickListener(this);
        btn_marca4.setOnClickListener(this);
        imb_edita.setOnClickListener(this);

    }

    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override protected void onRestart() {
        super.onRestart();
        arranca();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_marca1:
                tel = consultaDB(1);
                if (tel == "vacio") {
                    Toast.makeText(getApplicationContext(),"No hay contacto asignado",Toast.LENGTH_LONG).show();
                } else {
                    llamar("tel:" + tel);
                }
                break;
            case R.id.btn_marca2:
                tel = consultaDB(2);
                if (tel == "vacio") {
                    Toast.makeText(getApplicationContext(),"No hay contacto asignado",Toast.LENGTH_LONG).show();
                } else {
                    llamar("tel:" + tel);
                }
                break;
            case R.id.btn_marca3:
                tel = consultaDB(3);
                if (tel == "vacio") {
                    Toast.makeText(getApplicationContext(),"No hay contacto asignado",Toast.LENGTH_LONG).show();
                } else {
                    llamar("tel:" + tel);
                }
                break;
            case R.id.btn_marca4:
                tel = consultaDB(4);
                if (tel == "vacio") {
                    Toast.makeText(getApplicationContext(),"No hay contacto asignado",Toast.LENGTH_LONG).show();
                } else {
                    llamar("tel:" + tel);
                }
                break;
            case R.id.imb_editar:
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    public void arranca(){

        cursor = db.rawQuery("Select id, nombre from agenda",null);
        String  nombre_tmp;
        Integer id_tmp;
        btn_marca1.setText("VACÍO");
        btn_marca2.setText("VACÍO");
        btn_marca3.setText("VACÍO");
        btn_marca4.setText("VACÍO");
        if (cursor.moveToFirst()){
            do{
                id_tmp = cursor.getInt(0);
                nombre_tmp = cursor.getString(1);
                if(id_tmp == 1){
                    btn_marca1.setText(nombre_tmp);
                }
                if(id_tmp == 2){
                    btn_marca2.setText(nombre_tmp);
                }
                if(id_tmp == 3){
                    btn_marca3.setText(nombre_tmp);
                }
                if(id_tmp == 4){
                    btn_marca4.setText(nombre_tmp);
                }
            }while ( cursor.moveToNext());
        }
    }

    public String consultaDB(int id){
        cursor =  db.rawQuery("Select  tel from agenda where id = " + id,null);
        if(cursor.moveToFirst())
        {
            return cursor.getString(0);

        } else {
            return "vacio";
        }
    }

    public void llamar(String numero){
        Intent i;
        i = new Intent(Intent.ACTION_CALL, Uri.parse(numero));

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para realizar llamadas telefónicas.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso!");
        }

        startActivity(i);
    }

    public void consulta(){
        cursor = db.rawQuery("SELECT id, nombre, tel FROM agenda",null);
        if(cursor.moveToFirst())
        {
            do{
                String id = cursor.getString(0);
                String cla = cursor.getString(1);
                String nom = cursor.getString(2);
                Log.d("Registro: "," " + id + " -  " + cla + " - " + nom + "\n");
            } while (cursor.moveToNext());
        }
    }



}

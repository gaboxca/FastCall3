package com.gaboxca.fastcall3;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class edit extends AppCompatActivity implements View.OnClickListener {

    Button btn_marca1;
    Button btn_marca2;
    Button btn_marca3;
    Button btn_marca4;
    Button btn_salvar;
    Button btn_borrar;
    Button btn_agenda;
    EditText et_nombre;
    EditText et_tel;
    int id;


    // Variables BASE DE DATOS
    private Cursor c_update;
    private SQLiteDatabase db_edit;
    ContentValues registro = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        btn_marca1 = findViewById(R.id.btn_marca1);
        btn_marca2 = findViewById(R.id.btn_marca2);
        btn_marca3 = findViewById(R.id.btn_marca3);
        btn_marca4 = findViewById(R.id.btn_marca4);

        String font_path = "font/teenbd.ttf";
        Typeface TF = Typeface.createFromAsset(getAssets(), font_path);
        btn_marca1.setTypeface(TF);
        btn_marca2.setTypeface(TF);
        btn_marca3.setTypeface(TF);
        btn_marca4.setTypeface(TF);

        et_nombre = findViewById(R.id.et_nombre);
        et_tel = findViewById(R.id.et_tel);
        btn_salvar = findViewById(R.id.btn_salvar);
        btn_borrar = findViewById(R.id.btn_borrar);
        btn_agenda = findViewById(R.id.btn_agenda);

        btn_marca1.setOnClickListener(this);
        btn_marca2.setOnClickListener(this);
        btn_marca3.setOnClickListener(this);
        btn_marca4.setOnClickListener(this);
        btn_salvar.setOnClickListener(this);
        btn_borrar.setOnClickListener(this);
        btn_agenda.setOnClickListener(this);

        //  VARIABLES CONEXION Y MANEJO BASE DE DATOS
        ApoyoDB conexion_db = new ApoyoDB(this,"DBAgenda.dbf",null,1);
        db_edit = conexion_db.getWritableDatabase();

        arranca();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db_edit.close();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_marca1:
                visualiza(v);
                id = 1;
                inicia_boton(id, btn_marca1);
                break;
            case R.id.btn_marca2:
                visualiza(v);
                id = 2;
                inicia_boton(id, btn_marca2);
                break;
            case R.id.btn_marca3:
                visualiza(v);
                id = 3;
                inicia_boton(id, btn_marca3);
                break;
            case R.id.btn_marca4:
                visualiza(v);
                id = 4;
                inicia_boton(id, btn_marca4);
                break;
            case R.id.btn_salvar:
                if (et_nombre.getText().toString().equals("") || et_tel.getText().toString().equals("")){
                    Toast.makeText(this,"Favor de agregar Nombre y Teléfono",Toast.LENGTH_SHORT).show();
                } else {
                    borrar(id);
                    insertar(id,et_nombre.getText().toString(),et_tel.getText().toString());
                    oculta(v);
                    arranca();
                    Toast.makeText(this,"El contacto ha sido SALVADO",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case R.id.btn_borrar:
                borrar(id);
                oculta(v);
                arranca();
                Toast.makeText(this,"El contacto ha sido ELIMINADO",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btn_agenda:


                break;
            default:
                break;
        }

    }

    public void inicia_boton(int id, Button btn){
        if ( btn.getText().toString().equals("VACÍO") ){
            et_nombre.setText("");
            et_tel.setText("");
        } else {
            et_nombre.setText(btn.getText().toString());
            et_tel.setText(consulta_tel(id));
        }
        et_nombre.selectAll();
    }

    public void insertar(int id, String nombre, String tel){
        registro.put("id",id);
        registro.put("nombre",nombre);
        registro.put("tel",tel);
        db_edit.insert("agenda",null,registro);
    }

    public void borrar(int id){
        db_edit.delete("agenda","ID = " + id, null );
    }

    public void update(int id, String nombre, String tel){
        Log.d("Registro", "UPDATE agenda SET nombre = '" + nombre + "' SET tel = '" + tel +"' WHERE id = " + id );
        registro.put("nombre",nombre);
        registro.put("tel",tel);
        db_edit.update("agenda",registro,"ID = " + id,null);

    }

    public String consulta_tel (int id){
        c_update = db_edit.rawQuery("SELECT tel from agenda where id = " + id , null);
        if (c_update.moveToFirst()) {
            //c_update.moveToFirst();
            Log.d("Registro: ", "" + c_update.getString(0));
            return c_update.getString(0);
        } else {
            return "";
        }

    }

    public void visualiza(View v) {
        et_nombre.setVisibility(v.getVisibility());
        et_tel.setVisibility(v.getVisibility());
        btn_salvar.setVisibility(v.getVisibility());
        btn_borrar.setVisibility(v.getVisibility());
        btn_agenda.setVisibility(v.getVisibility());
    }

    public void oculta(View v) {
        et_nombre.setVisibility(v.GONE);
        et_tel.setVisibility(v.GONE);
        btn_salvar.setVisibility(v.GONE);
        btn_borrar.setVisibility(v.GONE);
    }

    public void arranca(){

        c_update = db_edit.rawQuery("Select id, nombre from agenda",null);
        btn_marca1.setText("VACÍO");
        btn_marca2.setText("VACÍO");
        btn_marca3.setText("VACÍO");
        btn_marca4.setText("VACÍO");
        String  nombre_tmp;
        Integer id_tmp;
        if (c_update.moveToFirst()){
            do{
                id_tmp = c_update.getInt(0);
                nombre_tmp = c_update.getString(1);
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
            }while ( c_update.moveToNext());
        }
    }


}

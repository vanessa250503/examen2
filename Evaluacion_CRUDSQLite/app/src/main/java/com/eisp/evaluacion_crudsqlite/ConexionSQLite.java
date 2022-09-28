package com.eisp.evaluacion_crudsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ConexionSQLite extends SQLiteOpenHelper {

    boolean estadoDelete = true;

    ArrayList<String> listaArticulos;
    ArrayList<String> ListaCategorias;
    ArrayList<String> listaPro;

    ArrayList<Dto> articulosList;
    ArrayList<Dto> articulosLista;
    ArrayList<Dto> CatList;

    public ConexionSQLite(Context context) {
        super(context, "evaluacion.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tb_categorias(idcategoria integer not null primary key, nombrecategoria text, estadocategoria integer, fecharegistro date)");
        db.execSQL("create table tb_productos(codigo integer not null primary key, descripcion text, precio real, idcategoria integer, FOREIGN KEY(idcategoria) REFERENCES tb_categorias(idcategoria))");

        db.execSQL("insert into tb_categorias values('1', 'Teclado', '1', '24/09/2022)')");
        db.execSQL("insert into tb_categorias values('2', 'Celular', '1', '24/09/2022)')");
        db.execSQL("insert into tb_categorias values('3', 'TV', '1', '25/09/2022)')");
        db.execSQL("insert into tb_categorias values('4', 'Audifono', '1', '25/09/2022)')");
        db.execSQL("insert into tb_categorias values('5', 'Impresora', '1', '25/09/2022)')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists tb_categorias");
        db.execSQL("drop table if exists tb_productos");
        onCreate(db);
    }

    public SQLiteDatabase bd(){
        SQLiteDatabase bd = this.getWritableDatabase();
        return bd;
    }

    public boolean InsertarTradicional(Context context, Dto datos){
        boolean estado = true;
        int resultado;

        try{
            int codigo = datos.getCodigo();
            String descripcion = datos.getDescripcion();
            double precio = datos.getPrecio();
            int idcategoria = datos.getIdcategoria();


            Cursor fila = bd().rawQuery("select codigo from tb_productos where codigo='"+codigo+"'", null);
            if (fila.moveToFirst()==true){
                Toast.makeText(context, "false", Toast.LENGTH_SHORT).show();
                estado = false;
            }else{
                String SQL="INSERT INTO tb_productos \n"+
                        "(codigo,descripcion,precio,idcategoria)\n"+
                        "VALUES \n"+
                        "('"+String.valueOf(codigo)+"','"+descripcion+"','"+String.valueOf(precio)+"','"+String.valueOf(idcategoria)+"')";

                bd().execSQL(SQL);
                bd().close();
                Toast.makeText(context, "true", Toast.LENGTH_SHORT).show();
                estado = true;
            }
        }catch (Exception e){
            estado = false;
            Log.e("error.",e.toString());
            Toast.makeText(context, "catch", Toast.LENGTH_SHORT).show();
        }
        return estado;

    }

    public boolean insertardatos(Dto datos){
        boolean estado=true;
        int resultado;
        ContentValues registro=new ContentValues();
        try{
            registro.put("codigo", datos.getCodigo());
            registro.put("descripcion", datos.getDescripcion());
            registro.put("precio", datos.getPrecio());
            Cursor fila= bd().rawQuery("select codigo from articulos where codigo='"+datos.getCodigo()+"'", null);
            if (fila.moveToFirst()==true){
                estado = false;
            }else{
                resultado=(int) bd().insert("articulos", null, registro);
                if (resultado > 0) estado = true;
                else estado = false;
            }
        }catch (Exception e){
            estado = false;
            Log.e("error.",e.toString());
        }
        return estado;
    }

    public boolean InsertRegister(Dto datos){
        boolean estado=true;
        int resultado;
        try{
            int codigo=datos.getCodigo();
            String descripcion=datos.getDescripcion();
            double precio= datos.getPrecio();

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fecha1 = sdf.format(cal.getTime());

            Cursor fila = bd().rawQuery("select codigo from articulos where codigo='"+datos.getCodigo()+"'", null);
            if (fila.moveToFirst()==true){
                estado = false;
            }else {
                String SQL = "INSERT INTO articulos \n"+
                        "(codigo, descripcion, precio) \n"+
                        "VALUES \n"+
                        "(?,?,?,?);";
                bd().execSQL(SQL, new String[]{String.valueOf(codigo),
                        descripcion, String.valueOf(precio)});
                estado = true;
            }
        }catch (Exception e){
            estado = false;
            Log.e("error.",e.toString());
        }
        return estado;
    }

    public boolean consultaCodigo(Dto datos){
        boolean estado = true;
        int resultado;
        SQLiteDatabase bd = this.getWritableDatabase();
        try {
            int codigo = datos.getCodigo();
            Cursor fila = bd.rawQuery("select codigo, descripcion, precio, idcategoria from tb_productos where codigo="+codigo,null);
            if (fila.moveToFirst()){
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                datos.setIdcategoria(Integer.parseInt(fila.getString(3)));
                estado = true;
            }else {
                estado = false;
            }
            bd.close();
        }catch (Exception e){
            estado = false;
            Log.e("error.", e.toString());
        }
        return estado;
    }

    //metodo opcional
    public boolean consultaArticulos(Dto datos){
        boolean estado = true;
        int resultado;

        SQLiteDatabase bd=this.getWritableDatabase();
        try {
            String[] parametros={String.valueOf(datos.getCodigo())};
            String[] campos={"codigo", "descripcion", "precio", "idcategoria"};
            Cursor fila= bd().query("tb_productos", campos, "codigo=?", parametros,null,null,null);
            if (fila.moveToFirst()){
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                datos.setIdcategoria(Integer.parseInt(fila.getString(3)));
                estado = true;
            }else {
                estado=false;
            }
            fila.close();
            bd.close();
        }catch (Exception e){
            estado = false;
            Log.e("error.",e.toString());
        }
        return estado;
    }//fin del metodo opcional

    public boolean consultarDescripcion(Dto datos){
        boolean estado = true;
        int resultado;
        SQLiteDatabase bd = this.getWritableDatabase();
        try {
            String descripcion=datos.getDescripcion();
            Cursor fila= bd.rawQuery("select codigo, descripcion, precio, idcategoria from tb_productos where descripcion='"+descripcion+"'", null);
            if (fila.moveToFirst()){
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                datos.setIdcategoria(Integer.parseInt(fila.getString(3)));
                estado = true;
            }else{
                estado=false;
            }
            bd.close();
        }catch (Exception e){
            estado=false;
            Log.e("error.",e.toString());
        }
        return estado;
    }

    public boolean bajaCodigo(final Context context, final Dto datos){
        estadoDelete=true;
        try{
            int codigo= datos.getCodigo();
            Cursor fila = bd().rawQuery("select * from tb_productos where codigo="+codigo, null);
            if (fila.moveToFirst()) {
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                datos.setIdcategoria(Integer.parseInt(fila.getString(3)));

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.ic_delete);
                builder.setTitle("Warning");
                builder.setMessage("¿Estas seguro de borrar el registro?\nCodigo:"+
                        datos.getCodigo()+"\nDescripcion: "+datos.getDescripcion());
                builder.setCancelable(false);
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int codigo=datos.getCodigo();
                        int cant=bd().delete("tb_productos", "codigo="+codigo, null);
                        if(cant > 0){
                            estadoDelete=true;
                            Toast.makeText(context, "Registro Eliminado Correctamente.", Toast.LENGTH_SHORT).show();
                        }else {
                            estadoDelete=false;
                        }
                        bd().close();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else {
                Toast.makeText(context, "No hay resultados encontrados para la busqueda especificada", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            estadoDelete=false;
            Log.e("Error.", e.toString());
        }
        return estadoDelete;
    }

    public boolean modificar(Dto datos){
        boolean estado=true;
        int resultado;
        SQLiteDatabase bd=this.getWritableDatabase();
        try {
            int codigo= datos.getCodigo();
            String descripcion=datos.getDescripcion();
            double precio= datos.getPrecio();
            int idcategoria = datos.getIdcategoria();

            ContentValues registro=new ContentValues();
            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);
            registro.put("idcategoria", idcategoria);

            int cant = (int) bd.update("tb_productos", registro, "codigo="+codigo, null);
            bd.close();
            if (cant > 0) estado=true;
            else estado=false;
        }catch (Exception e){
            Log.e("error.",e.toString());
        }
        return estado;
    }

    //metodo para crear lista de datos de la BD en el spinner
    public ArrayList<Dto> consultaListaArticulos(){
        boolean estado=false;
        SQLiteDatabase bd=this.getWritableDatabase();

        Dto articulos=null;
        articulosList=new ArrayList<Dto>();
        try {
            Cursor fila = bd.rawQuery("select * from tb_productos", null);
            while (fila.moveToNext()){
                articulos=new Dto();
                articulos.setCodigo(fila.getInt(0));
                articulos.setDescripcion(fila.getString(1));
                articulos.setPrecio(fila.getDouble(2));
                articulos.setIdcategoria(fila.getInt(3));

                articulosList.add(articulos);

                Log.i("codigo", String.valueOf(articulos.getCodigo()));
                Log.i("descripcion", articulos.getDescripcion().toString());
                Log.i("precio", String.valueOf(articulos.getPrecio()));
                Log.i("idcategoria", String.valueOf(articulos.getIdcategoria()));
            }
            obtenerListaArticulos();
        }catch (Exception e){

        }
        return articulosList;
    }

    public ArrayList<String> obtenerListaArticulos(){
        listaArticulos=new ArrayList<String>();
        listaArticulos.add("Seleccione");

        for (int i=0; i<articulosList.size(); i++){
            listaArticulos.add(articulosList.get(i).getCodigo()+"~"+articulosList.get(i).getDescripcion()+"~"+articulosList.get(i).getIdcategoria());
        }
        return listaArticulos;
    }//final del spinner

    //inicio del metodo para crear lista de datos de la BD en el ListView
    public ArrayList<String> consultaListaArticulos1(){
        boolean estado = false;
        SQLiteDatabase bd=this.getReadableDatabase();

        Dto articulos=null;
        articulosList=new ArrayList<Dto>();

        try {
            Cursor fila = bd.rawQuery("select * from tb_productos", null);
            while (fila.moveToNext()){
                articulos=new Dto();
                articulos.setCodigo(fila.getInt(0));
                articulos.setDescripcion(fila.getString(1));
                articulos.setPrecio(fila.getDouble(2));
                articulos.setIdcategoria(fila.getInt(3));

                articulosList.add(articulos);
            }

            listaArticulos=new ArrayList<String>();
            for (int i=0; i<articulosList.size(); i++){
                listaArticulos.add(articulosList.get(i).getCodigo()+"~"+articulosList.get(i).getDescripcion()+"~"+articulosList.get(i).getIdcategoria());
            }
        }catch (Exception e){

        }
        return listaArticulos;
    }



    //inicio del metodo para crear lista de datos de la BD en el ListView INNERJOIN
    public ArrayList<String> consultaListaArticulos1IJ(){
        boolean estado = false;
        SQLiteDatabase bd=this.getReadableDatabase();

        Dto articulos=null;
        articulosLista=new ArrayList<Dto>();

        try {
            Cursor fila = bd.rawQuery("select tb_productos.codigo, tb_productos.descripcion, tb_productos.precio, tb_categorias.nombrecategoria from tb_productos INNER JOIN tb_categorias ON tb_productos.idcategoria = tb_categorias.idcategoria", null);
            while (fila.moveToNext()){
                articulos=new Dto();
                articulos.setCodigo(fila.getInt(0));
                articulos.setDescripcion(fila.getString(1));
                articulos.setPrecio(fila.getDouble(2));
                articulos.setNomCate(fila.getString(3));

                articulosLista.add(articulos);
            }

            listaPro=new ArrayList<String>();
            for (int i=0; i<articulosLista.size(); i++){
                listaPro.add(articulosLista.get(i).getCodigo()+"~"+articulosLista.get(i).getDescripcion()+"~"+articulosLista.get(i).getNomCate());
            }
        }catch (Exception e){

        }
        return listaPro;
    }


    public ArrayList<Dto> consultaListaArticulosIJ(){
        boolean estado=false;
        SQLiteDatabase bd=this.getWritableDatabase();

        Dto articulos=null;
        articulosLista=new ArrayList<Dto>();
        try {
            Cursor fila = bd.rawQuery("select tb_productos.codigo, tb_productos.descripcion, tb_productos.precio, tb_categorias.nombrecategoria from tb_productos INNER JOIN tb_categorias ON tb_productos.idcategoria = tb_categorias.idcategoria", null);
            while (fila.moveToNext()){
                articulos=new Dto();
                articulos.setCodigo(fila.getInt(0));
                articulos.setDescripcion(fila.getString(1));
                articulos.setPrecio(fila.getDouble(2));
                articulos.setNomCate(fila.getString(3));

                articulosLista.add(articulos);

                Log.i("codigo", String.valueOf(articulos.getCodigo()));
                Log.i("descripcion", articulos.getDescripcion().toString());
                Log.i("precio", String.valueOf(articulos.getPrecio()));
                Log.i("nombrecategoria", articulos.getNomCate());
            }
            obtenerListaArticulos();
        }catch (Exception e){

        }
        return articulosLista;
    }


}

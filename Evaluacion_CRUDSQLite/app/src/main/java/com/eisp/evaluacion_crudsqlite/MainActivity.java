package com.eisp.evaluacion_crudsqlite;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.eisp.evaluacion_crudsqlite.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Spinner SpinnerCate;
    private EditText et_codigo, et_descripcion, et_precio;
    private Button btn_guardar, btn_consultar1, btn_consultar2, btn_eliminar, btn_actualizar;
    private TextView tv_resultado;

    boolean inputEt=false;
    boolean inputEd=false;
    boolean inputEx=false;
    boolean SpCat=false;
    int resultadoInsert=0;


    Modal ventanas = new Modal();
    ConexionSQLite conexion = new ConexionSQLite(this);
    Dto datos = new Dto();
    AlertDialog.Builder dialogo;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            new android.app.AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_close)
                    .setTitle("Warning")
                    .setMessage("¿Realmente desea salir?")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                        }
                    })
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //para el spinner de las categorias
        SpinnerCate=(Spinner) findViewById(R.id.sp1);
        String []opciones={"Seleccione una opción:","Teclado", "Celular", "TV", "Audifonos", "Impresora"};
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        SpinnerCate.setPrompt("Selecciones una Opción");
        SpinnerCate.setAdapter(adapter);


        conexion.consultaListaArticulos();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_ir));
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        toolbar.setTitleMargin(0,0,0,0);
        toolbar.setSubtitle("CRUD SQLite - 2022");
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.black));
        toolbar.setSubtitle("Ismael Panameño");
        setSupportActionBar(toolbar);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmacion();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ventanas.Search(MainActivity.this);
            }
        });

        et_codigo=(EditText) findViewById(R.id.et_codigo);
        et_descripcion=(EditText) findViewById(R.id.et_descripcion);
        et_precio=(EditText) findViewById(R.id.et_precio);
        btn_guardar=(Button) findViewById(R.id.btn_guardar);
        btn_consultar1=(Button) findViewById(R.id.btn_consultar1);
        btn_consultar2=(Button) findViewById(R.id.btn_consultar2);
        btn_eliminar=(Button) findViewById(R.id.btn_eliminar);
        btn_actualizar=(Button) findViewById(R.id.btn_actualizarRegistro);

        String senal ="";
        String codigo ="";
        String descripcion ="";
        String precio = "";
        String idcategoria = "";

        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (bundle != null){
                codigo = bundle.getString("codigo");
                senal = bundle.getString("senal");
                descripcion = bundle.getString("descripcion");
                precio = bundle.getString("precio");

                //CATEGORIA

                idcategoria = bundle.getString("idcategoria");
                if (senal.equals("1")){
                    et_codigo.setText(codigo);
                    et_descripcion.setText(descripcion);
                    et_precio.setText(precio);
                }
            }
        }catch (Exception e){

        }

    }

    private void confirmacion(){
        String mensaje = "¿Realmente desea salir?";
        dialogo=new AlertDialog.Builder(MainActivity.this);
        dialogo.setIcon(R.drawable.ic_close);
        dialogo.setTitle("Warning");
        dialogo.setMessage(mensaje);
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.finish();
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogo.show();
    }

    //MENUUUUUU

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_limpiar) {
            et_codigo.setText(null);
            et_descripcion.setText(null);
            et_precio.setText(null);
            SpinnerCate.setSelection(0);

            return true;
        }else if (id == R.id.action_listaArticulos){
            Intent spinnerActivity = new Intent(MainActivity.this, ConsultaSpinner.class);
            startActivity(spinnerActivity);
            return true;
        }else if (id == R.id.action_listaArticulos1) {
            Intent listViewActivity = new Intent(MainActivity.this, ListViewArticulos.class);
            startActivity(listViewActivity);
            return true;
        }else if (id == R.id.action_listaArticulos2) {
            Intent CAT_PRO = new Intent(MainActivity.this, ListViewCat_Pro.class);
            startActivity(CAT_PRO);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void alta(View v){
        SpinnerOP();
        //Toast.makeText(this, "holaa", Toast.LENGTH_SHORT).show();

        if (et_codigo.getText().toString().length()==0){
            et_codigo.setError("Campo obligatorio");
            inputEt=false;
        }else {
            inputEt=true;
        }

        if (et_descripcion.getText().toString().length()==0){
            et_descripcion.setError("Campo obligatorio");
            inputEd=false;
        }else {
            inputEd=true;
        }

        if (et_precio.getText().toString().length()==0){
            et_precio.setError("Campo obligatorio");
            inputEx=false;
        }else {
            inputEx=true;
        }
        if (inputEt && inputEd && inputEx && SpCat){
            //Toast.makeText(this, "12345", Toast.LENGTH_SHORT).show();
            try {
                datos.setCodigo(Integer.parseInt(et_codigo.getText().toString()));
                datos.setDescripcion(et_descripcion.getText().toString());
                datos.setPrecio(Double.parseDouble(et_precio.getText().toString()));

                //Toast.makeText(this, ""+conexion.InsertarTradicional(this, datos), Toast.LENGTH_SHORT).show();


                if (conexion.InsertarTradicional(this,datos)==true){
                    Toast.makeText(this, "Registro agregado satisfactoriamente!", Toast.LENGTH_SHORT).show();
                    limpiarDatos();
                }else {
                    Toast.makeText(getApplicationContext(), "Error. Ya existe un registro\n"+"Codigo:"+et_codigo.getText().toString(), Toast.LENGTH_SHORT).show();
                    limpiarDatos();
                }


            }catch (Exception e){
                Toast.makeText(this, "ERROR. Ya existe", Toast.LENGTH_SHORT).show();
            }

        }
    }


    public void mensaje (String mensaje){
        Toast.makeText(this, ""+mensaje, Toast.LENGTH_SHORT).show();
    }

    public void limpiarDatos(){
        et_codigo.setText(null);
        et_descripcion.setText(null);
        et_precio.setText(null);
        et_codigo.requestFocus();
        SpinnerCate.setSelection(0);
    }

    public void consultaporcodigo(View v){
        if (et_codigo.getText().toString().length()==0){
            et_codigo.setError("Campo obligatorio");
            inputEt=false;
        }else{
            inputEt=true;
        }
        if (inputEt){
            String codigo = et_codigo.getText().toString();
            datos.setCodigo(Integer.parseInt(codigo));

            if (conexion.consultaArticulos(datos)){
                et_descripcion.setText(datos.getDescripcion());
                et_precio.setText(""+datos.getPrecio());
                SpinnerCate.setSelection(datos.getIdcategoria());
            }else{
                Toast.makeText(this, "No existe un articulo con dicho codigo", Toast.LENGTH_SHORT).show();
                limpiarDatos();
            }
        }else{
            Toast.makeText(this, "Ingrese el codigo de articulo a buscar.", Toast.LENGTH_SHORT).show();
        }
    }

    public void consultarpordescripcion(View v){
        if (et_descripcion.getText().toString().length()==0){
            et_descripcion.setError("Campo obligatorio");
            inputEd=false;
        }else{
            inputEd=true;
        }
        if (inputEd){
            String descripcion = et_descripcion.getText().toString();
            datos.setDescripcion(descripcion);
            if (conexion.consultarDescripcion(datos)){
                et_codigo.setText(""+datos.getCodigo());
                et_descripcion.setText(datos.getDescripcion());
                et_precio.setText(""+datos.getPrecio());
                SpinnerCate.setSelection(datos.getIdcategoria());
            }else{
                Toast.makeText(this, "No existe un articulo con dicha descripcion", Toast.LENGTH_SHORT).show();
                limpiarDatos();
            }
        }else{
            Toast.makeText(this, "Ingrese la descripcion del articulo a buscar.", Toast.LENGTH_SHORT).show();
        }
    }

    public void bajaporcodigo(View v){
        if (et_codigo.getText().toString().length()==0){
            et_codigo.setError("Campo obligatorio");
            inputEt=false;
        }else{
            inputEt=true;
        }
        if (inputEt){
            String cod=et_codigo.getText().toString();
            datos.setCodigo(Integer.parseInt(cod));
            if (conexion.bajaCodigo(MainActivity.this,datos)){
                limpiarDatos();
            }else{
                Toast.makeText(this, "No existe un articulo con dicho codigo.", Toast.LENGTH_SHORT).show();
                limpiarDatos();
            }
        }
    }

    public void modificacion(View v){
        if (et_codigo.getText().toString().length()==0){
            et_codigo.setError("Campo obligatorio");
            inputEt=false;
        }else{
            inputEt=true;
        }
        if (inputEt){
            String cod = et_codigo.getText().toString();
            String descripcion=et_descripcion.getText().toString();
            double precio=Double.parseDouble(et_precio.getText().toString());
            int idcategoria = SpinnerOP();

            datos.setCodigo(Integer.parseInt(cod));
            datos.setDescripcion(descripcion);
            datos.setPrecio(precio);
            datos.setIdcategoria(idcategoria);

            if (conexion.modificar(datos)){
                Toast.makeText(this, "Registro Modificado Correctamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "No se ha encontrado resultados para la busqueda especificada.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public int SpinnerOP(){
        String op=SpinnerCate.getSelectedItem().toString();
        if (op.equals("Seleccione una opción:")){
            SpCat=false;
            Toast.makeText(this, "Error: Seleccione una Opción válida! ", Toast.LENGTH_SHORT).show();
        }else{
            SpCat=true;
        }
        int idCat=0;
        if (op.equals("Monitor")){
            idCat=1;
            datos.setIdcategoria(idCat);
            //Double suma=n1+n2;
            //String resu=String.valueOf(suma);
            //r.setText(resu);
        }else if (op.equals("Celular")){
            idCat=2;
            datos.setIdcategoria(idCat);
            //Toast.makeText(this, "Celular", Toast.LENGTH_SHORT).show();
        }else if (op.equals("TV")){
            idCat=3;
            datos.setIdcategoria(idCat);
        }else if (op.equals("Laptop")) {
            idCat=4;
            datos.setIdcategoria(idCat);
        }else if (op.equals("Computadora")) {
            idCat=5;
            datos.setIdcategoria(idCat);
        }else if (op.equals("")) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        return idCat;
    }
}
package com.eisp.evaluacion_crudsqlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListViewArticulos extends AppCompatActivity {

    ListView listViewPersonas;
    ArrayAdapter adaptador;
    SearchView searchView;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter adapter;

    String[] version =
            {"Aestro", "Blender", "CupCake", "Donut", "Eclair", "Froyo", "GingerBread", "HoneyComb",
                    "IceCream Sandwich", "Jelly Bean", "Kitkat", "Lolipop", "Marshmallow", "Nought", "Oreo"};

    ConexionSQLite conexion = new ConexionSQLite(this);
    Dto datos = new Dto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_articulos);

        listViewPersonas=(ListView) findViewById(R.id.ViewPersonas);
        searchView=(SearchView)findViewById(R.id.SearchView);

        adaptador=new ArrayAdapter(this, android.R.layout.simple_list_item_1, conexion.consultaListaArticulos1());
        listViewPersonas.setAdapter(adaptador);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String text=s;
                adaptador.getFilter().filter(text);
                return false;
            }
        });

        listViewPersonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int post, long l) {
                String informacion="Codigo:" +conexion.consultaListaArticulos().get(post).getDescripcion()+"\n";
                informacion+="Descripcion:" +conexion.consultaListaArticulos().get(post).getDescripcion()+"\n";
                informacion+="Precio:"+conexion.consultaListaArticulos().get(post).getPrecio();
                informacion+="Categoria:"+conexion.consultaListaArticulos().get(post).getIdcategoria();

                Dto articulos = conexion.consultaListaArticulos().get(post);
                Intent intent = new Intent(ListViewArticulos.this, DetallesArticulos.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("tb_productos", articulos);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}

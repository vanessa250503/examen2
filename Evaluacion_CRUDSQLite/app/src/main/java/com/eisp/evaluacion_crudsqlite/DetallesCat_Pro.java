package com.eisp.evaluacion_crudsqlite;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetallesCat_Pro extends AppCompatActivity {

    private TextView tv_codigo, tv_descripcion, tv_precio, tv_idcegoria;
    private TextView tv_codigo1, tv_descripcion1, tv_precio1, tv_fecha, tv_idcegoria1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_cat_pro);

        tv_codigo = (TextView) findViewById(R.id.codigo);
        tv_descripcion = (TextView) findViewById(R.id.descripcion);
        tv_precio = (TextView) findViewById(R.id.precio);
        tv_idcegoria = (TextView) findViewById(R.id.idCat);

        tv_codigo1 = (TextView) findViewById(R.id.codigo1);
        tv_descripcion1 = (TextView) findViewById(R.id.descripcion1);
        tv_precio1 = (TextView) findViewById(R.id.precio1);
        tv_fecha = (TextView) findViewById(R.id.tv_f);
        tv_idcegoria1 = (TextView) findViewById(R.id.idcat3);


        Bundle objeto = getIntent().getExtras();
        Dto dto = null;
        if (objeto != null) {
            dto = (Dto) objeto.getSerializable("tb_productos");
            tv_codigo.setText("" + dto.getCodigo());
            tv_descripcion.setText(dto.getDescripcion());
            tv_precio.setText(String.valueOf(dto.getPrecio()));
            tv_idcegoria.setText(""+dto.getNomCate());

            tv_codigo1.setText("" + dto.getCodigo());
            tv_descripcion1.setText(dto.getDescripcion());
            tv_precio1.setText(String.valueOf(dto.getPrecio()));
            tv_idcegoria1.setText(""+dto.getNomCate());
            tv_fecha.setText("Fecha de creacion" + getDateTime());
        }
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss a", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}

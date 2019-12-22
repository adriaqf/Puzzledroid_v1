package com.sds.puzzledroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class IndividualActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual);

        // Crear el gridview
        GridView gridview = (GridView) findViewById(R.id.gridview);

        gridview.setAdapter(new ImageAdapter(this));// con setAdapter se llena
        // el gridview con datos. en
        // este caso un nuevo objeto de la clase com.sds.puzzledroid.ImageAdapter,
        // que está definida en otro archivo
        // para que detecte la pulsación se le añade un listener de itemClick
        // que recibe un OniTemClickListener creado con new

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // dentro de este listener difinimos la función que se ejecuta al
            // hacer click en un item
            // el metodo pertenece a AdapterView, es decir, es
            // AdapterView.OnItemClickListener
            // dentro de este, tenemos el método onItemClick, que es el que se
            // invoca al pulsar un item del AdapterView
            // esa función recibe el objeto padre, que es un adapterview en el
            // que se ha pulasdo, una vista, que es el elemento sobre el que se
            // ha pulsado,
            // una posicion, que es la posicion del elemento dentro del adapter,
            // y un id, que es el id de fila del item que se ha pulsado
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // Toast para mostrar un mensaje. Escribe el nombre de tu clase
                // si no la llamaste MainActivity.
                // Al hacer click, esta mensaje muestra la posición
                Toast.makeText(IndividualActivity.this, "" + position, Toast.LENGTH_SHORT)
                        .show();

            }
        });

    }
}

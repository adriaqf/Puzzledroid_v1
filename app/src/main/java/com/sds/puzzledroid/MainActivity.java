package com.sds.puzzledroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Método boton Individual
    public void individual(View view){
        Intent individual = new Intent(this,Individual.class);
        startActivity(individual);
    }
    //Método boton Multijugador
    public void multijugador(View view){
        Intent multijugador = new Intent(this,Multijugador.class);
        startActivity(multijugador);
    }

    //Shows Action Bar Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    //Adds Action Bar Buttons' actions
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.config_item:
                Toast.makeText(this, "Configuración", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.help_item:
                Toast.makeText(this, "Ayuda", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

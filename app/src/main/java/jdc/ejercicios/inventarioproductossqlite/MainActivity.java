package jdc.ejercicios.inventarioproductossqlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Se usan LinearLayouts como botones en la interfaz para simplificar el diseño
    LinearLayout btnRegistrar, btnListar, btnConsultar, btnStockBajo;

    // Objeto Intent utilizado para la navegación entre actividades
    Intent miIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inicialización de la Base de Datos
        ConexionSQLiteHelper conexionbd = new ConexionSQLiteHelper(
                this, "bd_inventario", null, 1);

        // Botón para registrar productos
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ir a la actividad de registro
                miIntent = new Intent(MainActivity.this, RegistroProductoActivity.class);
                startActivity(miIntent);
            }
        });

        // Botón para listar productos
        btnListar = findViewById(R.id.btnListar);
        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ir a la actividad de listado
                miIntent = new Intent(MainActivity.this, ListarProductosActivity.class);
                startActivity(miIntent);
            }
        });

        // Botón para consultar productos
        btnConsultar = findViewById(R.id.btnConsultar);
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ir a la actvidad de consulta
                miIntent = new Intent(MainActivity.this, ConsultarProductoActivity.class);
                startActivity(miIntent);
            }
        });

        // Botón para ver productos con stock bajo
        btnStockBajo = findViewById(R.id.btnStockBajo);
        btnStockBajo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ir a la actividad stock bajo
                miIntent = new Intent(MainActivity.this, StockBajoActivity.class);
                startActivity(miIntent);
            }
        });
    }
}
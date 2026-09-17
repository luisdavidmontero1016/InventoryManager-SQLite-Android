package jdc.ejercicios.inventarioproductossqlite;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import jdc.ejercicios.inventarioproductossqlite.utilidades.Utilidades;

public class StockBajoActivity extends AppCompatActivity {

    // Componentes de la interfaz
    ListView listaStockBajo;
    TextView txtTitulo;

    // Objetos para la gestión de datos y conexión
    ConexionSQLiteHelper conexionbd;
    ArrayList<String> listaInformacion;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stock_bajo);

        // Inicializa la conexión a la base de datos
        conexionbd = new ConexionSQLiteHelper(getApplicationContext(), "bd_inventario", null, 1);

        // Asociación de componentes del layout
        listaStockBajo = findViewById(R.id.listaStockBajo);
        txtTitulo = findViewById(R.id.txtTitulo);

        // Ejecuta la consulta para mostrar los productos críticos
        consultarProductosStockBajo();
    }

    private void consultarProductosStockBajo() {
        // Obtiene la BD en modo lectura
        SQLiteDatabase db = conexionbd.getReadableDatabase();
        listaInformacion = new ArrayList<>(); // Inicializa la lista que contendrá los resultados

        try {
            // Ejecución de la consulta SELECT filtrada
            // Consultar productos con stock menor a 5
            Cursor cursor = db.query(
                    Utilidades.TABLA_PRODUCTOS,
                    null,
                    Utilidades.CAMPO_STOCK + " < ?",
                    new String[] { "5" },
                    null,
                    null,
                    Utilidades.CAMPO_STOCK + " ASC"
            );

            // Procesamiento de los resultados
            if (cursor.moveToFirst()) {
                int contador = 0; // Contador para saber cuántos productos tienen stock bajo
                do {
                    contador++;
                    int stock = cursor.getInt(4);
                    String alerta = "";

                    // Asigna el tipo de alerta según el nivel de stock
                    if (stock == 0) {
                        alerta = " ⚠️ SIN STOCK";
                    } else if (stock < 3) {
                        alerta = " ⚠️ CRÍTICO";
                    } else {
                        alerta = " ⚠️ BAJO";
                    }
                    // Formatea la información del producto
                    String linea = "ID: " + cursor.getInt(0) + "\n" +
                            "Nombre: " + cursor.getString(1) + alerta + "\n" +
                            "Descripción: " + cursor.getString(2) + "\n" +
                            "Precio: $" + String.format("%.2f", cursor.getDouble(3)) + "\n" +
                            "Stock: " + stock + " unidades\n" +
                            "──────────────────────────";
                    listaInformacion.add(linea);
                } while (cursor.moveToNext()); // Continúa con el siguiente registro

                // Adaptación y visualización
                adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        listaInformacion
                );
                listaStockBajo.setAdapter(adapter);

                // Actualiza el título con el total de productos encontrados
                txtTitulo.setText("Productos con Stock Bajo \nTotal: " + contador + " productos");

                // Muestra un Toast informativo
                Toast.makeText(getApplicationContext(),
                        "⚠️ " + contador + " productos requieren atención",
                        Toast.LENGTH_LONG).show();
            } else {
                // Si no hay productos con stock bajo
                txtTitulo.setText("Productos con Stock Bajo (< 5 unidades)\nTotal: 0 productos");
                Toast.makeText(getApplicationContext(),
                        "✓ No hay productos con stock bajo",
                        Toast.LENGTH_SHORT).show();
                listaStockBajo.setAdapter(null); // Limpia el ListView
            }
            cursor.close();

        } catch (Exception e) {
            // Manejo de errores durante la consulta
            Toast.makeText(getApplicationContext(),
                    "Error al consultar stock: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar la lista para reflejar cualquier cambio en el inventario
        consultarProductosStockBajo();
    }
}
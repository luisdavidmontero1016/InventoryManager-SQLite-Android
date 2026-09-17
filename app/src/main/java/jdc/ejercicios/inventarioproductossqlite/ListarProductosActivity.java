package jdc.ejercicios.inventarioproductossqlite;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import jdc.ejercicios.inventarioproductossqlite.utilidades.Utilidades;

public class ListarProductosActivity extends AppCompatActivity {


    // Componentes de la interfaz de usuario
    ListView listaProductos;
    Button btnOrdenarNombre, btnOrdenarPrecio;

    // Objeto de conexión a la base de datos
    ConexionSQLiteHelper conexionbd;
    // Lista temporal para almacenar los datos formateados antes de pasarlos al adaptador
    ArrayList<String> listaInformacion;
    // Adaptador para enlazar la lista de datos al ListView
    ArrayAdapter<String> adapter;

    //Componente de la interfaz de usuario utilizado como contador
    TextView tvContadorProductos;


    // Variables de estado para controlar el orden actual de cada columna
    private boolean ordenNombreAsc = true;
    private boolean ordenPrecioAsc = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar_productos);

        // Inicializa la conexión a la base de datos
        conexionbd = new ConexionSQLiteHelper(getApplicationContext(), "bd_inventario", null, 1);

        // Asociación de componentes del layout
        listaProductos = findViewById(R.id.listaProductos);
        btnOrdenarNombre = findViewById(R.id.btnOrdenarNombre);
        btnOrdenarPrecio = findViewById(R.id.btnOrdenarPrecio);
        tvContadorProductos = findViewById(R.id.tvContadorProductos);



        // Carga inicial de datos
        // Carga productos al iniciar por nombre ascendente por defecto
        consultarListaProductos("nombre", "ASC");
        actualizarTextoBoton(btnOrdenarNombre, "Nombre", ordenNombreAsc);

        // Configuración del Listener para el botón Ordenar por Nombre
        btnOrdenarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Alterna entre "ASC" Ascendente y "DESC" Descendente
                String orden = ordenNombreAsc ? "ASC" : "DESC";
                // Llama a la consulta con el nuevo orden
                consultarListaProductos("nombre", orden);
                // Invierte el estado para el próximo clic
                ordenNombreAsc = !ordenNombreAsc; // Alternar orden
                // Actualiza el texto del botón para indicar el próximo orden
                actualizarTextoBoton(btnOrdenarNombre, "Nombre", ordenNombreAsc);
            }
        });

        // Configuración del Listener para el botón Ordenar por Precio
        btnOrdenarPrecio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Alterna entre "ASC" Ascendente y "DESC" Descendente
                String orden = ordenPrecioAsc ? "ASC" : "DESC";
                // Llama a la consulta con el nuevo orden
                consultarListaProductos("precio_unitario", orden);
                // Invierte el estado para el próximo clic
                ordenPrecioAsc = !ordenPrecioAsc;
                // Actualiza el texto del botón para indicar el próximo orden
                actualizarTextoBoton(btnOrdenarPrecio, "Precio", ordenPrecioAsc);
            }
        });
    }

    private void consultarListaProductos(String campoOrden, String tipoOrden) {
        // Obtiene la BD en modo lectura
        SQLiteDatabase db = conexionbd.getReadableDatabase();
        // Inicializa la lista que contendrá la información formateada para el ListView
        listaInformacion = new ArrayList<>();

        try {
            // Ejecución de la consulta SELECT
            Cursor cursor = db.query(
                    Utilidades.TABLA_PRODUCTOS,
                    null,
                    null,
                    null,
                    null,
                    null,
                    campoOrden + " " + tipoOrden
            );

            // Procesamiento de los resultados
            if (cursor.moveToFirst()) {
                // Itera sobre todos los registros devueltos por la consulta
                do {
                    // Formatea la información del producto en una cadena de texto multilinea
                    String linea = "ID: " + cursor.getInt(0) + "\n" +
                            "Nombre: " + cursor.getString(1) + "\n" +
                            "Descripción: " + cursor.getString(2) + "\n" +
                            // Formato de precio con dos decimales
                            "Precio: $" + String.format("%.2f", cursor.getDouble(3)) + "\n" +
                            "Stock: " + cursor.getInt(4) + " unidades\n" +
                            "──────────────────────────";
                    // Añade la cadena formateada a la lista de información
                    listaInformacion.add(linea);
                } while (cursor.moveToNext()); // Mueve el cursor al siguiente registro


                // Adaptación y visualización
                adapter = new ArrayAdapter<>(
                        this,
                        // Layout estándar para elementos de lista simples
                        android.R.layout.simple_list_item_1,
                        listaInformacion
                );
                // Asigna el adaptador al ListView para mostrar los datos
                listaProductos.setAdapter(adapter);

                // Actualiza el contador de productos mostrados en la interfaz
                tvContadorProductos.setText(cursor.getCount() + " items");

                // Muestra un Toast con el total de productos
                Toast.makeText(getApplicationContext(),
                        "Total de productos: " + cursor.getCount(),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Si no hay resultados
                Toast.makeText(getApplicationContext(),
                        "No hay productos registrados",
                        Toast.LENGTH_SHORT).show();
                // Limpia el ListView
                listaProductos.setAdapter(null);
                // Pone el contador en 0
                tvContadorProductos.setText("0 items");
            }
            cursor.close();

        } catch (Exception e) {
            // Manejo de errores durante la consulta
            Toast.makeText(getApplicationContext(),
                    "Error al cargar productos: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        } finally {
            db.close(); //Se cierra la bd
        }

    }

    private void actualizarTextoBoton(Button boton, String campo, boolean esAscendente) {
        // Si el orden actual es Ascendente, el próximo será Descendente
        String proximoOrden = esAscendente ? "↓" : "↑";
        boton.setText("Ordenar por " + campo + " " + proximoOrden);
    }

    // Se llama cuando la actividad vuelve a estar visible.
    // Se usa para refrescar la lista en caso de que un producto haya sido modificado o eliminado en otra actividad.
    @Override
    protected void onResume() {
        super.onResume();
        // Recargar la lista cuando se vuelve a la actividad
        consultarListaProductos("nombre", "ASC");
        ordenNombreAsc = true;
        actualizarTextoBoton(btnOrdenarNombre, "Nombre", ordenNombreAsc);
    }
}
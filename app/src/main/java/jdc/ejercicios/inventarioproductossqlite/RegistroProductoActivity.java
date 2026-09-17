package jdc.ejercicios.inventarioproductossqlite;



import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import jdc.ejercicios.inventarioproductossqlite.utilidades.Utilidades;


public class RegistroProductoActivity extends AppCompatActivity {

    // Declaración de los campos de texto y el botón de la interfaz
    EditText campoNombre, campoDescripcion, campoPrecio, campoStock;
    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_producto);

        // Asociación de las referencias del código con los elementos del layout
        campoNombre = findViewById(R.id.editNombre);
        campoDescripcion = findViewById(R.id.editDescripcion);
        campoPrecio = findViewById(R.id.editPrecio);
        campoStock = findViewById(R.id.editStock);

        // Configuración del Listener para el botón Registrar
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarProducto();
            }
        });
    }

    private void registrarProducto() {
        // Validación de campos obligatorios vacíos
        if (campoNombre.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        if (campoPrecio.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "El precio es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        if (campoStock.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "La cantidad en stock es obligatoria", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Validación de que precio y stock sean números válidos
            // Intenta convertir los valores de texto a sus tipos numéricos correspondientes
            double precio = Double.parseDouble(campoPrecio.getText().toString());
            int stock = Integer.parseInt(campoStock.getText().toString());

            if (precio <= 0) {
                Toast.makeText(getApplicationContext(), "El precio debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                return;
            }

            if (stock < 0) {
                Toast.makeText(getApplicationContext(), "La cantidad no puede ser negativa", Toast.LENGTH_SHORT).show();
                return;
            }

            // Conexión a la base de datos

            // Instancia el Helper para la conexión
            ConexionSQLiteHelper conexionbd = new ConexionSQLiteHelper(
                    this, "bd_inventario", null, 1);
            // Obtiene una referencia a la BD en modo escritura
            SQLiteDatabase db = conexionbd.getWritableDatabase();

            // Preparación de valores para insertar

            // ContentValues se usa para guardar los pares clave-valor a insertar
            ContentValues valores = new ContentValues();
            valores.put(Utilidades.CAMPO_NOMBRE, campoNombre.getText().toString().trim());
            // La descripción es opcional, pero se toma su valor si existe.
            valores.put(Utilidades.CAMPO_DESCRIPCION, campoDescripcion.getText().toString().trim());
            valores.put(Utilidades.CAMPO_PRECIO, precio);
            valores.put(Utilidades.CAMPO_STOCK, stock);

            // Ejecutar la inserción
            Long idResultado = db.insert(Utilidades.TABLA_PRODUCTOS, null, valores);

            // Mostrar resultado y limpiar
            if (idResultado != -1) {
                Toast.makeText(getApplicationContext(),
                        "Producto registrado con ID: " + idResultado,
                        Toast.LENGTH_SHORT).show();
                limpiarCampos();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Error al registrar el producto",
                        Toast.LENGTH_SHORT).show();
            }

            db.close(); //Cierre de conexión de la base de datos

        } catch (NumberFormatException e) {
            // Maneja el error si el usuario ingresa texto en los campos numéricos
            Toast.makeText(getApplicationContext(),
                    "Precio y cantidad deben ser números válidos",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // Captura cualquier otro error durante la conexión o inserción
            Toast.makeText(getApplicationContext(),
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    // Método auxiliar para limpiar los campos de texto después de un registro exitoso.
    private void limpiarCampos() {
        campoNombre.setText("");
        campoDescripcion.setText("");
        campoPrecio.setText("");
        campoStock.setText("");
        campoNombre.requestFocus();
    }
}
package jdc.ejercicios.inventarioproductossqlite;



import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import jdc.ejercicios.inventarioproductossqlite.utilidades.Utilidades;


public class ConsultarProductoActivity extends AppCompatActivity {

    // Declaración de las cajas de texto y botones de la interfaz
    EditText campoId, campoNombre, campoDescripcion, campoPrecio, campoStock;
    Button btnBuscar, btnBuscarNombre, btnActualizar, btnEliminar;

    // Objeto para manejar la conexión con SQLite
    private ConexionSQLiteHelper conexionbd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consultar_producto);

        // Crear conexión a la base de datos
        conexionbd = new ConexionSQLiteHelper(getApplicationContext(), "bd_inventario", null, 1);

        // Se asocian los campos de texto con el XML respectivo
        campoId = findViewById(R.id.edtId);
        campoNombre = findViewById(R.id.edtNombre);
        campoDescripcion = findViewById(R.id.edtDescripcion);
        campoPrecio = findViewById(R.id.edtPrecio);
        campoStock = findViewById(R.id.edtStock);

        // Botón que sirve para buscar por ID
        btnBuscar = findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarProductoPorId();
            }
        });

        // Botón que sirve para buscar por nombre
        btnBuscarNombre = findViewById(R.id.btnBuscarNombre);
        btnBuscarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarProductoPorNombre();
            }
        });

        // Botón que sirve para actualizar stock
        btnActualizar = findViewById(R.id.btnActualizar);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarProducto();
            }
        });

        // Botón que sirve para eliminar un producto
        btnEliminar = findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarProducto();
            }
        });
    }


    private void consultarProductoPorId() {
        // Validación que el ID no esté vacío
        if (campoId.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingrese un ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener base de datos en modo lectura
        SQLiteDatabase db = conexionbd.getReadableDatabase();
        // Parámetros de búsqueda
        String[] parametros = { campoId.getText().toString() };
        // Columnas que queremos consultar
        String[] campos = { Utilidades.CAMPO_NOMBRE, Utilidades.CAMPO_DESCRIPCION,
                Utilidades.CAMPO_PRECIO, Utilidades.CAMPO_STOCK };

        try {
            // Realizar consulta SELECT
            Cursor cursor = db.query(Utilidades.TABLA_PRODUCTOS, campos,
                    Utilidades.CAMPO_ID + "=?", parametros, null, null, null);

            // Si encontró el producto
            if (cursor.moveToFirst()) {
                campoNombre.setText(cursor.getString(0));
                campoDescripcion.setText(cursor.getString(1));
                campoPrecio.setText(String.valueOf(cursor.getDouble(2)));
                campoStock.setText(String.valueOf(cursor.getInt(3)));
                Toast.makeText(getApplicationContext(), "Producto encontrado", Toast.LENGTH_SHORT).show();
            } else {
                // Si no encontró coincidencias
                Toast.makeText(getApplicationContext(), "Producto no encontrado", Toast.LENGTH_SHORT).show();
                limpiarCampos();
            }
            cursor.close();
        } catch (Exception e) {
            // Captura de errores

            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            limpiarCampos();
        } finally {
            db.close();
        }
    }

    private void consultarProductoPorNombre() {
        // Valida que el campo Nombre no esté vacío.
        if (campoNombre.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingrese un nombre para buscar", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtiene una referencia a la base de datos en modo solo lectura.
        SQLiteDatabase db = conexionbd.getReadableDatabase();
        // % para búsqueda parcial
        String nombreBusqueda = "%" + campoNombre.getText().toString() + "%";

        try {
            // Ejecuta una consulta genérica. Se usa 'null' en los campos para obtener todas las columnas.
            // La cláusula WHERE usa LIKE para búsquedas flexibles.
            Cursor cursor = db.query(Utilidades.TABLA_PRODUCTOS, null,
                    Utilidades.CAMPO_NOMBRE + " LIKE ?",
                    new String[] { nombreBusqueda }, null, null, null);

            // Verifica si se encontró al menos un producto.
            if (cursor.moveToFirst()) {
                // Si hay resultados, carga los datos del PRIMER producto encontrado en los EditText.
                // Los índices corresponden al orden de las columnas en la tabla.
                campoId.setText(String.valueOf(cursor.getInt(0)));
                campoNombre.setText(cursor.getString(1));
                campoDescripcion.setText(cursor.getString(2));
                campoPrecio.setText(String.valueOf(cursor.getDouble(3)));
                campoStock.setText(String.valueOf(cursor.getInt(4)));

                // Muestra un mensaje si hay más de un producto con ese nombre.
                if (cursor.getCount() > 1) {
                    Toast.makeText(getApplicationContext(),
                            "Se encontraron " + cursor.getCount() + " productos. Mostrando el primero.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Producto encontrado", Toast.LENGTH_SHORT).show();
                }
            } else {
                // No hay coincidencias con el patrón de búsqueda.
                Toast.makeText(getApplicationContext(),
                        "No se encontraron productos con ese nombre",
                        Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
    }

    private void actualizarProducto() {
        // Validación inicial en el que se asegura que haya un ID cargado.
        if (campoId.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Primero busque un producto", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validación de campos obligatorios.
        if (campoNombre.getText().toString().trim().isEmpty() ||
                campoPrecio.getText().toString().trim().isEmpty() ||
                campoStock.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Complete todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Conversión y validación numérica
            double precio = Double.parseDouble(campoPrecio.getText().toString());
            int stock = Integer.parseInt(campoStock.getText().toString());

            if (precio <= 0 || stock < 0) {
                Toast.makeText(getApplicationContext(), "Valores inválidos", Toast.LENGTH_SHORT).show();
                return;
            }
            //Inicia la operación de escritura.
            SQLiteDatabase db = conexionbd.getWritableDatabase();
            String[] parametros = { campoId.getText().toString() };

            // Crea un objeto ContentValues con los nuevos valores.
            ContentValues valores = new ContentValues();
            valores.put(Utilidades.CAMPO_NOMBRE, campoNombre.getText().toString().trim());
            valores.put(Utilidades.CAMPO_DESCRIPCION, campoDescripcion.getText().toString().trim());
            valores.put(Utilidades.CAMPO_PRECIO, precio);
            valores.put(Utilidades.CAMPO_STOCK, stock);

            // 5. Ejecuta la actualización (UPDATE).
            // Retorna el número de filas modificadas.
            int filasAfectadas = db.update(Utilidades.TABLA_PRODUCTOS, valores,
                    Utilidades.CAMPO_ID + "=?", parametros);

            // Muestra el resultado.
            if (filasAfectadas > 0) {
                Toast.makeText(getApplicationContext(),
                        "Producto actualizado correctamente",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "No se pudo actualizar el producto",
                        Toast.LENGTH_SHORT).show();
            }
            db.close();

        } catch (NumberFormatException e) {
            // Error si el precio o stock no son números válidos.
            Toast.makeText(getApplicationContext(),
                    "Precio y stock deben ser números válidos",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // Error genérico.
            Toast.makeText(getApplicationContext(),
                    "Error al actualizar: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void eliminarProducto() {
        // Validación inicial que asegura que haya un ID para eliminar.
        if (campoId.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Primero busque un producto", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtiene una referencia a la base de datos en modo escritura.
        SQLiteDatabase db = conexionbd.getWritableDatabase();

        try {
            // Define el parámetro (ID) del producto a eliminar.
            String[] parametros = { campoId.getText().toString() };
            // Ejecuta la eliminación (DELETE). Retorna el número de filas afectadas.
            int filasAfectadas = db.delete(Utilidades.TABLA_PRODUCTOS,
                    Utilidades.CAMPO_ID + "=?", parametros);

            // Muestra el resultado
            if (filasAfectadas > 0) {
                Toast.makeText(getApplicationContext(),
                        "Producto eliminado correctamente",
                        Toast.LENGTH_SHORT).show();
                limpiarCampos();
            } else {
                Toast.makeText(getApplicationContext(),
                        "No se encontró el producto",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "Error al eliminar: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
    }

    //Limpia el contenido de todos los campos de texto en la interfaz.
    private void limpiarCampos() {
        campoId.setText("");
        campoNombre.setText("");
        campoDescripcion.setText("");
        campoPrecio.setText("");
        campoStock.setText("");
    }
}
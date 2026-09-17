package jdc.ejercicios.inventarioproductossqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import jdc.ejercicios.inventarioproductossqlite.utilidades.Utilidades;


public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    public ConexionSQLiteHelper(@Nullable Context context, @Nullable String name,
                                @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Aquí se ejecuta el script de cración de la tabla productos el cuál es una constante de utilidades
        db.execSQL(Utilidades.CREAR_TABLA_PRODUCTOS);

        // Insertar registros de ejemplos iniciales para probrar la tabla
        insertarProductosEjemplo(db);
    }


    // Método que se ejecuta si la versión de la base de datos cambia.
    // Se usa para actualizar la estructura: eliminar tablas antiguas y recrearlas
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar la tabla anterior si existe con eso previene error al crear de nuevo
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_PRODUCTOS);
        // Volver a crear la estructura y datos iniciales.
        onCreate(db);
    }

    // Inserta una serie de productos de ejemplo solo si la tabla está vacía.
    // Recibe la instancia SQLiteDatabase para ejecutar consultas dentro de onCreate y onUpgrade.
    private void insertarProductosEjemplo(SQLiteDatabase db) {
        // Verificar si ya existen productos: consulta COUNT en la tabla.
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + Utilidades.TABLA_PRODUCTOS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        // Si la tabla está vacía entonces se insertan los productos de ejemplo.
        if (count == 0) {

            // Para cada producto se crea el objeto ContentValues para almacenar los valores correspondientes

            ContentValues producto1 = new ContentValues();
            producto1.put(Utilidades.CAMPO_NOMBRE, "Tarjeta MicroSD SanDisk 256GB");
            producto1.put(Utilidades.CAMPO_DESCRIPCION, "Tarjeta de memoria Clase 10, U3, velocidad de lectura 100MB/s");
            producto1.put(Utilidades.CAMPO_PRECIO, 150000.00);
            producto1.put(Utilidades.CAMPO_STOCK, 45);
            db.insert(Utilidades.TABLA_PRODUCTOS, null, producto1);


            ContentValues producto2 = new ContentValues();
            producto2.put(Utilidades.CAMPO_NOMBRE, "Smartwatch Deportivo Xiaomi");
            producto2.put(Utilidades.CAMPO_DESCRIPCION, "Reloj inteligente con monitor de ritmo cardíaco, GPS y batería de 14 días");
            producto2.put(Utilidades.CAMPO_PRECIO, 320000.00);
            producto2.put(Utilidades.CAMPO_STOCK, 18);
            db.insert(Utilidades.TABLA_PRODUCTOS, null, producto2);

            ContentValues producto3 = new ContentValues();
            producto3.put(Utilidades.CAMPO_NOMBRE, "Sistema WiFi Mesh Tenda Nova");
            producto3.put(Utilidades.CAMPO_DESCRIPCION, "Kit de tres nodos WiFi de malla para cobertura de hasta 500m²");
            producto3.put(Utilidades.CAMPO_PRECIO, 890000.00);
            producto3.put(Utilidades.CAMPO_STOCK, 5);
            db.insert(Utilidades.TABLA_PRODUCTOS, null, producto3);

            ContentValues producto4 = new ContentValues();
            producto4.put(Utilidades.CAMPO_NOMBRE, "Miniproyector Portátil Full HD");
            producto4.put(Utilidades.CAMPO_DESCRIPCION, "Proyector LED compacto con corrección trapezoidal y altavoz integrado");
            producto4.put(Utilidades.CAMPO_PRECIO, 480000.00);
            producto4.put(Utilidades.CAMPO_STOCK, 12);
            db.insert(Utilidades.TABLA_PRODUCTOS, null, producto4);

            ContentValues producto5 = new ContentValues();
            producto5.put(Utilidades.CAMPO_NOMBRE, "Regleta Inteligente Wi-Fi");
            producto5.put(Utilidades.CAMPO_DESCRIPCION, "Regleta con 4 tomas AC y 2 puertos USB controlables por app y voz");
            producto5.put(Utilidades.CAMPO_PRECIO, 110000.00);
            producto5.put(Utilidades.CAMPO_STOCK, 30);
            db.insert(Utilidades.TABLA_PRODUCTOS, null, producto5);

            ContentValues producto6 = new ContentValues();
            producto6.put(Utilidades.CAMPO_NOMBRE, "Tarjeta de Red Gigabit Ethernet");
            producto6.put(Utilidades.CAMPO_DESCRIPCION, "Adaptador PCIe para red cableada de alta velocidad 10/100/1000 Mbps");
            producto6.put(Utilidades.CAMPO_PRECIO, 65000.00);
            producto6.put(Utilidades.CAMPO_STOCK, 25);
            db.insert(Utilidades.TABLA_PRODUCTOS, null, producto6);

            ContentValues producto7 = new ContentValues();
            producto7.put(Utilidades.CAMPO_NOMBRE, "Ventilador de CPU Cooler Master");
            producto7.put(Utilidades.CAMPO_DESCRIPCION, "Disipador de calor de torre única con 4 heatpipes de cobre y bajo ruido");
            producto7.put(Utilidades.CAMPO_PRECIO, 190000.00);
            producto7.put(Utilidades.CAMPO_STOCK, 9);
            db.insert(Utilidades.TABLA_PRODUCTOS, null, producto7);

            ContentValues producto8 = new ContentValues();
            producto8.put(Utilidades.CAMPO_NOMBRE, "Tarjeta de Sonido USB Externa 7.1");
            producto8.put(Utilidades.CAMPO_DESCRIPCION, "Interfaz de audio externa para sonido de alta fidelidad, plug and play");
            producto8.put(Utilidades.CAMPO_PRECIO, 210000.00);
            producto8.put(Utilidades.CAMPO_STOCK, 15);
            db.insert(Utilidades.TABLA_PRODUCTOS, null, producto8);

            ContentValues producto9 = new ContentValues();
            producto9.put(Utilidades.CAMPO_NOMBRE, "Kit Iluminación LED para Streaming");
            producto9.put(Utilidades.CAMPO_DESCRIPCION, "Dos paneles de luz LED con trípodes y temperatura de color ajustable para video");
            producto9.put(Utilidades.CAMPO_PRECIO, 550000.00);
            producto9.put(Utilidades.CAMPO_STOCK, 7);
            db.insert(Utilidades.TABLA_PRODUCTOS, null, producto9);

            ContentValues producto10 = new ContentValues();
            producto10.put(Utilidades.CAMPO_NOMBRE, "Lector de Tarjetas USB-C 5 en 1");
            producto10.put(Utilidades.CAMPO_DESCRIPCION, "Lector compatible con SD, MicroSD, CF, conector USB-C 3.1 de alta velocidad");
            producto10.put(Utilidades.CAMPO_PRECIO, 40000.00);
            producto10.put(Utilidades.CAMPO_STOCK, 55);
            db.insert(Utilidades.TABLA_PRODUCTOS, null, producto10);
        }
    }
}
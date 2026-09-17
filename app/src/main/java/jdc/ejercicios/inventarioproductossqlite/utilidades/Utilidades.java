package jdc.ejercicios.inventarioproductossqlite.utilidades;


public class Utilidades {

    // Es la asignación del nombre de la tabla
    public static final String TABLA_PRODUCTOS = "productos";

    // Campos que se crean de la tabla
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_NOMBRE = "nombre";
    public static final String CAMPO_DESCRIPCION = "descripcion";
    public static final String CAMPO_PRECIO = "precio_unitario";
    public static final String CAMPO_STOCK = "cantidad_stock";

    // Sentencia SQL de Creación de Tabla
    public static final String CREAR_TABLA_PRODUCTOS =
            "CREATE TABLE " + TABLA_PRODUCTOS + " (" +
                    CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CAMPO_NOMBRE + " TEXT NOT NULL, " +
                    CAMPO_DESCRIPCION + " TEXT, " +
                    CAMPO_PRECIO + " REAL NOT NULL, " +
                    CAMPO_STOCK + " INTEGER NOT NULL)";
}
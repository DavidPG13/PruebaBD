package com.example.pruebabdtutorial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;

class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context; // Contexto de la aplicación
    private static final String DATABASE_NAME = "BookLibrary.db"; // Nombre de la base de datos
    private static final int DATABASE_VERSION = 1; // Versión de la base de datos

    // Nombre de la tabla y nombres de las columnas
    private static final String TABLE_NAME = "my_library";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "book_title";
    private static final String COLUMN_AUTHOR = "book_author";
    private static final String COLUMN_PAGES = "book_pages";

    // Constructor
    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla al inicializar la base de datos
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_AUTHOR + " TEXT, "
                + COLUMN_PAGES + " INTEGER);";
        db.execSQL(query); // Ejecuta la consulta SQL para crear la tabla
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Actualizar la base de datos, eliminar tabla si ya existe y crear de nuevo
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Método para agregar un libro a la base de datos
    void addBook(String title, String author, int pages) {
        SQLiteDatabase db = this.getWritableDatabase(); // Obtiene la base de datos en modo escritura
        ContentValues cv = new ContentValues(); // Contenedor para los valores

        // Pone los valores en el contenedor
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_AUTHOR, author);
        cv.put(COLUMN_PAGES, pages);

        // Inserta el libro en la base de datos y obtiene el resultado
        long result = db.insert(TABLE_NAME, null, cv);

        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para leer todos los datos de la base de datos
    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME; // Consulta SQL para seleccionar todos los datos
        SQLiteDatabase db = this.getReadableDatabase(); // Obtiene la base de datos en modo lectura

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null); // Ejecuta la consulta SQL
        }
        return cursor; // Retorna el cursor con los datos
    }

    // Método para actualizar un registro de la base de datos
    void updateData(String row_id, String title, String author, String pages) {
        SQLiteDatabase db = this.getWritableDatabase(); // Obtiene la base de datos en modo escritura
        ContentValues cv = new ContentValues(); // Contenedor para los valores

        // Pone los valores en el contenedor
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_AUTHOR, author);
        cv.put(COLUMN_PAGES, pages);

        // Actualiza el registro en la base de datos y obtiene el resultado
        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});

        // Muestra un mensaje si la actualización fue exitosa o fallida
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para eliminar un registro de la base de datos
    void deleteOneRow(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase(); // Obtiene la base de datos en modo escritura

        // Elimina el registro y obtiene el resultado
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});

        // Muestra un mensaje si la eliminación fue exitosa o fallida
        if (result == -1) {
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para eliminar todos los registros de la base de datos
    void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase(); // Obtiene la base de datos en modo escritura
        db.execSQL("DELETE FROM " + TABLE_NAME); // Ejecuta la consulta SQL para eliminar todos los registros
    }
}

package com.example.pruebabdtutorial;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;           // RecyclerView para mostrar la lista de libros
    FloatingActionButton add_button;     // Botón flotante para agregar un nuevo libro
    ImageView empty_imageview;           // Imagen que se muestra cuando no hay datos
    TextView no_data;                    // Texto que se muestra cuando no hay datos

    MyDatabaseHelper myDB;               // Instancia de la clase para manejar la base de datos
    ArrayList<String> book_id, book_title, book_author, book_pages; // Listas para almacenar los datos de los libros
    CustomAdapter customAdapter;         // Adaptador personalizado para el RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Establece el layout de la actividad

        // Inicializa los componentes de la UI
        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_data);

        // Configura un listener para el botón de agregar que inicia AddActivity cuando se hace clic
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        myDB = new MyDatabaseHelper(MainActivity.this); // Inicializa la instancia de MyDatabaseHelper
        // Inicializa las listas para almacenar los datos de los libros
        book_id = new ArrayList<>();
        book_title = new ArrayList<>();
        book_author = new ArrayList<>();
        book_pages = new ArrayList<>();

        storeDataInArrays(); // Llama al método para cargar los datos de la base de datos en las listas

        // Configura el RecyclerView con un CustomAdapter y un LinearLayoutManager
        customAdapter = new CustomAdapter(MainActivity.this, this, book_id, book_title, book_author, book_pages);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Si el requestCode es 1, se recrea la actividad para actualizar la UI con los nuevos datos
        if (requestCode == 1) {
            recreate();
        }
    }

    void storeDataInArrays() {
        Cursor cursor = myDB.readAllData(); // Obtiene todos los datos de la base de datos usando un Cursor
        if (cursor.getCount() == 0) {
            // Si no hay datos, muestra la imagen y el texto indicando que no hay datos
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        } else {
            // Si hay datos, los añade a las listas correspondientes
            while (cursor.moveToNext()) {
                book_id.add(cursor.getString(0));
                book_title.add(cursor.getString(1));
                book_author.add(cursor.getString(2));
                book_pages.add(cursor.getString(3));
            }
            // Oculta la imagen y el texto de "no datos"
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu); // Infla el menú de opciones en la actividad
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Maneja la selección de elementos del menú
        if (item.getItemId() == R.id.delete_all) {
            confirmDialog(); // Si se selecciona "delete_all", llama al método confirmDialog
        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog() {
        // Muestra un cuadro de diálogo de confirmación para eliminar todos los datos
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all Data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(MainActivity.this);
                myDB.deleteAllData(); // Elimina todos los datos de la base de datos
                // Reinicia la actividad para actualizar la UI
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // No hace nada si se selecciona "No"
            }
        });
        builder.create().show(); // Muestra el cuadro de diálogo
    }
}

package com.example.pruebabdtutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    EditText title_input, author_input, pages_input; // Campos de entrada para título, autor y páginas del libro
    Button add_button; // Botón para agregar un nuevo libro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add); // Establece el layout de la actividad

        // Inicializa los campos de entrada y el botón usando findViewById
        title_input = findViewById(R.id.title_input);
        author_input = findViewById(R.id.author_input);
        pages_input = findViewById(R.id.pages_input);
        add_button = findViewById(R.id.add_button);

        // Configura un listener para el botón de agregar
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crea una instancia de MyDatabaseHelper
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                // Añade el nuevo libro a la base de datos
                myDB.addBook(title_input.getText().toString().trim(),
                        author_input.getText().toString().trim(),
                        Integer.valueOf(pages_input.getText().toString().trim()));
            }
        });
    }
}

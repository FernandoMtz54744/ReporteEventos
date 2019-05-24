package com.gb.eventos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivityFaq extends AppCompatActivity implements View.OnClickListener {

    public static final String DATABASE_NAME = "ReportesEventos";

    TextView textViewViewEmployees, publicacion, bReporte;
    EditText editPregunta, editRespuesta;
    Spinner spinnerCategoria;

    SQLiteDatabase mDatabase;

    SharedPreferences sharedPreferences;

    static final String Preferences = "UserPreferences";
    static final String Name = "nameKey";
    static final String Position = "positionKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_faq);

        sharedPreferences = getSharedPreferences(Preferences, Context.MODE_PRIVATE);

        Toast.makeText(getApplicationContext(),
                "Bienvenido " + sharedPreferences.getString(Name, "default"), Toast.LENGTH_LONG).show();

        textViewViewEmployees = (TextView) findViewById(R.id.textViewViewEmployees);
        editPregunta = (EditText) findViewById(R.id.pre);
        editRespuesta = (EditText) findViewById(R.id.res);
        spinnerCategoria = (Spinner) findViewById(R.id.spinner);
        publicacion = (TextView) findViewById(R.id.publicacion);
        bReporte = (TextView) findViewById(R.id.botonPreguntaReporte);

        bReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(getApplicationContext(), ConsultaReportes.class);
                    startActivity(i);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        publicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Publicacion.class);
                startActivity(i);
            }
        });

        findViewById(R.id.Registrar).setOnClickListener(this);
        textViewViewEmployees.setOnClickListener(this);

        //creating a database
        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        createEmployeeTable();
    }


    private void createEmployeeTable() {
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS correes (\n" +
                        "    id INTEGER NOT NULL CONSTRAINT employe_pk PRIMARY KEY AUTOINCREMENT,\n" +
                        "    pregunta varchar(200) NOT NULL,\n" +
                        "    respuesta varchar(200) NOT NULL,\n" +
                        "    categoria varchar(200) NOT NULL,\n" +
                        "    estado varchar(200) NOT NULL\n," +
                        "    vestado varchar(200) NOT NULL\n" +
                        ");"
        );
    }

    //this method will validate the name and salary
    //dept does not need validation as it is a spinner and it cannot be empty
    private boolean inputsAreCorrect(String pregunta,String respuesta) {
        if (pregunta.isEmpty()) {
            editPregunta.setError("Please enter a name");
            editPregunta.requestFocus();
            return false;
        }

        if (respuesta.isEmpty()) {
            editRespuesta.setError("Please enter salary");
            editRespuesta.requestFocus();
            return false;
        }
        return true;
    }





    //In this method we will do the create operation
    private void addEmployee() {

        String pregunta = editPregunta.getText().toString().trim();
        String respuesta = editRespuesta.getText().toString().trim();
        String categoria = spinnerCategoria.getSelectedItem().toString();
        String estado = "No publicable";
        String vestado = "no";


        //validating the inptus
        if (inputsAreCorrect(pregunta, respuesta)) {

            String insertSQL = "INSERT INTO correes \n" +
                    "(pregunta, respuesta, categoria, estado, vestado)\n" +
                    "VALUES \n" +
                    "(?, ?, ?, ?, ?);";

            //using the same method execsql for inserting values
            //this time it has two parameters
            //first is the sql string and second is the parameters that is to be binded with the query
            mDatabase.execSQL(insertSQL, new String[]{pregunta, respuesta, categoria, estado, vestado});

            Toast.makeText(this, "Pregunta registrada", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Registrar:

                addEmployee();

                break;
            case R.id.textViewViewEmployees:

                startActivity(new Intent(this, EmployeeActivity.class));

                break;
        }
    }
}

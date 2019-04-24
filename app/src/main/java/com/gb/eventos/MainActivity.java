package com.gb.eventos;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DATABASE_NAME = "ReportesEventos";

    TextView verReportes;
    EditText reportante, clasificacion, descripcion, ingeniero;
    Spinner modulos;

    SQLiteDatabase base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verReportes = (TextView) findViewById(R.id.consultar);
        reportante = (EditText) findViewById(R.id.reportante);
        clasificacion = (EditText) findViewById(R.id.clasificacion);
        descripcion = (EditText) findViewById(R.id.descripcion);
        ingeniero =  (EditText) findViewById(R.id.ingeniero);
        modulos = (Spinner) findViewById(R.id.modulos);

        findViewById(R.id.registrar).setOnClickListener(this);
        verReportes.setOnClickListener(this);

        //Se crea la base
        base = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        CrearTabla();
    }

    private void CrearTabla() {

        String tabla = "CREATE TABLE IF NOT EXISTS eventos (\n" +
                "idReporte INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "reportante TEXT NOT NULL,\n" +
                "modulo TEXT NOT NULL,\n" +
                "clasificacion TEXT NOT NULL,\n" +
                "descripcion TEXT NOT NULL,\n" +
                "ingeniero TEXT,\n" +
                "estado TEXT\n" +
                ");";
        base.execSQL(tabla);
    }


    private boolean validar(String rep, String clasi, String desc) {
        if (rep.isEmpty()) {
            reportante.setError("Inserte un reportante");
            reportante.requestFocus();
            return false;
        }

        if (clasi.isEmpty()) {
            clasificacion.setError("Inserte una clsificacion");
            clasificacion.requestFocus();
            return false;
        }

        if(desc.isEmpty()){
            descripcion.setError("Inserte una descripcion");
            descripcion.requestFocus();
            return false;
        }
        return true;
    }

    private void registrar() {

        String rep = reportante.getText().toString().trim();
        String clasi = clasificacion.getText().toString().trim();
        String mod = modulos.getSelectedItem().toString();
        String desc = descripcion.getText().toString().trim();
        String inge = ingeniero.getText().toString().trim();

        if (validar(rep, clasi, desc)) {

            String insertSQL = "INSERT INTO eventos \n" +
                    "(reportante, modulo, clasificacion, descripcion, ingeniero, estado)\n" +
                    "VALUES \n" +
                    "(?, ?, ?, ?, ?, ?);";
            String esta = "abierto";
            base.execSQL(insertSQL, new String[]{rep, mod, clasi, desc, inge,esta});
            Toast.makeText(this, "Reporte Registrado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registrar:
                registrar();

                break;
            case R.id.consultar:
                startActivity(new Intent(this, ConsultaReportes.class));
                break;
        }
    }
}

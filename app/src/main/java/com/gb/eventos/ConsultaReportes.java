package com.gb.eventos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ConsultaReportes extends AppCompatActivity {

    List<Reporte> reportesList;
    SQLiteDatabase mDatabase;
    ListView listViewReportes;
    ReporteAdaptador adapter;
    Button cerrados;

    static final String Preferences = "UserPreferences";
    static final String Name = "nameKey";
    static final String Position = "positionKey";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        listViewReportes = (ListView) findViewById(R.id.listViewReportes);
        reportesList = new ArrayList<>();

        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        sharedPreferences = getSharedPreferences(Preferences, Context.MODE_PRIVATE);

        mostrarReportes();

        cerrados = (Button) findViewById(R.id.cerrados);
        cerrados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ReportesCerrados.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void mostrarReportes() {
        Cursor cursorReportes = mDatabase.rawQuery("SELECT * FROM eventos where estado='abierto'", null);

        if (cursorReportes.moveToFirst()) {
            do {
                reportesList.add(new Reporte(
                        cursorReportes.getInt(0),
                        cursorReportes.getString(1),
                        cursorReportes.getString(2),
                        cursorReportes.getString(3),
                        cursorReportes.getString(4),
                        cursorReportes.getString(5)
                ));
            } while (cursorReportes.moveToNext());
        }

        cursorReportes.close();

        String cargo = sharedPreferences.getString(Position, "default");
        String nombre = sharedPreferences.getString(Name, "default");

        adapter = new ReporteAdaptador(this, R.layout.list_layout_reportes, reportesList, mDatabase, cargo, nombre);

        //adding the adapter to listview
        listViewReportes.setAdapter(adapter);
    }

}

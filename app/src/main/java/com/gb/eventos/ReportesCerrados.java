package com.gb.eventos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ReportesCerrados extends AppCompatActivity {

    List<Reporte> reportesList;
    SQLiteDatabase mDatabase;
    ListView listViewReportes;
    ReporteAdaptadorCerrado adapter;
    Button abierto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_cerrado);

        listViewReportes = (ListView) findViewById(R.id.listViewReportesCerrados);
        reportesList = new ArrayList<>();

        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        mostrarReportes();

        abierto = (Button) findViewById(R.id.abierto);
        abierto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ConsultaReportes.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void mostrarReportes() {
        Cursor cursorReportes = mDatabase.rawQuery("SELECT * FROM eventos where estado='cerrado'", null);

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

        adapter = new ReporteAdaptadorCerrado(this, R.layout.list_layout_reportes_cerrados, reportesList, mDatabase);

        //adding the adapter to listview
        listViewReportes.setAdapter(adapter);
    }

}

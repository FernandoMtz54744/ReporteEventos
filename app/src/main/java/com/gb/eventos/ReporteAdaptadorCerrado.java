package com.gb.eventos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class ReporteAdaptadorCerrado extends ArrayAdapter<Reporte> {

    Context mCtx;
    int listLayoutRes;
    List<Reporte> reporteList;
    SQLiteDatabase mDatabase;

    public ReporteAdaptadorCerrado(Context mCtx, int listLayoutRes, List<Reporte> reporteList, SQLiteDatabase mDatabase) {
        super(mCtx, listLayoutRes, reporteList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.reporteList = reporteList;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        final Reporte reporte = reporteList.get(position);

        TextView reportante = view.findViewById(R.id.reportanteV);
        TextView modulo = view.findViewById(R.id.moduloV);
        TextView clasificacion = view.findViewById(R.id.clasificacionV);
        TextView descripcion = view.findViewById(R.id.descripcionV);
        TextView ingeniero = view.findViewById(R.id.ingenieroV);

        reportante.setText(reporte.getReportante());
        modulo.setText(reporte.getModulo());
        clasificacion.setText(String.valueOf(reporte.getClasificacion()));
        descripcion.setText(reporte.getDescripcion());
        ingeniero.setText(reporte.getIngeniero());

        return view;
    }


    private void updateReporte(final Reporte reporte) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialog_update_reporte, null);
        builder.setView(view);


        final EditText reportante = view.findViewById(R.id.reportanteA);
        final EditText clasificacion = view.findViewById(R.id.clasificaionA);
        final EditText descripcion = view.findViewById(R.id.descripcionA);
        final EditText ingeniero = view.findViewById(R.id.ingenieroA);
        final Spinner modulo = view.findViewById(R.id.modulosA);

        reportante.setText(reporte.getReportante());
        clasificacion.setText(reporte.getClasificacion());
        descripcion.setText(reporte.getDescripcion());
        ingeniero.setText(reporte.getIngeniero());

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.actualizar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rep= reportante.getText().toString().trim();
                String clas = clasificacion.getText().toString().trim();
                String desc = descripcion.getText().toString().trim();
                String inge = ingeniero.getText().toString().trim();
                String modu = modulo.getSelectedItem().toString();

                if (rep.isEmpty()) {
                    reportante.setError("Ingrese un reportante");
                    reportante.requestFocus();
                    return;
                }

                if (clas.isEmpty()) {
                    clasificacion.setError("ingrese una clasificacion");
                    clasificacion.requestFocus();
                    return;
                }
                if (desc.isEmpty()) {
                    descripcion.setError("ingrese una descripcion");
                    descripcion.requestFocus();
                    return;
                }

                if (inge.isEmpty()) {
                    ingeniero.setError("ingrese un ingeniero");
                    ingeniero.requestFocus();
                    return;
                }

                String sql = "UPDATE eventos \n" +
                        "SET reportante = ?, \n" +
                        "modulo = ?, \n" +
                        "clasificacion = ?, \n" +
                        "descripcion = ?, \n" +
                        "ingeniero = ? \n" +
                        "WHERE idReporte = ?;\n";

                mDatabase.execSQL(sql, new String[]{rep, modu, clas,desc,inge, String.valueOf(reporte.getIdReporte())});
                Toast.makeText(mCtx, "Reporte Actualizado", Toast.LENGTH_SHORT).show();
                recargarReportes();

                dialog.dismiss();
            }
        });
    }

    private void recargarReportes() {
        Cursor cursorReporte = mDatabase.rawQuery("SELECT * FROM eventos where estado='abierto'", null);
        if (cursorReporte.moveToFirst()) {
            reporteList.clear();
            do {
                reporteList.add(new Reporte(
                        cursorReporte.getInt(0),
                        cursorReporte.getString(1),
                        cursorReporte.getString(2),
                        cursorReporte.getString(3),
                        cursorReporte.getString(4),
                        cursorReporte.getString(5)
                ));
            } while (cursorReporte.moveToNext());
        }
        cursorReporte.close();
        notifyDataSetChanged();
    }

    private void recargarReportesCerrados() {
        Cursor cursorReporte = mDatabase.rawQuery("SELECT * FROM eventos where estado='cerrado'", null);
        if (cursorReporte.moveToFirst()) {
            reporteList.clear();
            do {
                reporteList.add(new Reporte(
                        cursorReporte.getInt(0),
                        cursorReporte.getString(1),
                        cursorReporte.getString(2),
                        cursorReporte.getString(3),
                        cursorReporte.getString(4),
                        cursorReporte.getString(5)
                ));
            } while (cursorReporte.moveToNext());
        }
        cursorReporte.close();
        notifyDataSetChanged();
    }

}

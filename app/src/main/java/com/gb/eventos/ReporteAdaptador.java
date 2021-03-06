package com.gb.eventos;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class ReporteAdaptador extends ArrayAdapter<Reporte> {

    Context mCtx;
    int listLayoutRes;
    List<Reporte> reporteList;
    SQLiteDatabase mDatabase;
    String cargo, nombre;

    public ReporteAdaptador(Context mCtx, int listLayoutRes, List<Reporte> reporteList, SQLiteDatabase mDatabase, String cargo, String nombre) {
        super(mCtx, listLayoutRes, reporteList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.reporteList = reporteList;
        this.mDatabase = mDatabase;
        this.cargo = cargo;
        this.nombre = nombre;
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

        final Button buttonCerrar = view.findViewById(R.id.cerrar);
        final Button buttonEditar = view.findViewById(R.id.editar);

        if(cargo.equals("gerenteS") || cargo.equals("ingeniero")) {
            if (cargo.equals("gerenteS") || (cargo.equals("ingeniero") && nombre.equals(reporte.getIngeniero().toLowerCase()))) {
                buttonEditar.setVisibility(View.VISIBLE);
            } else {
                buttonEditar.setVisibility(View.GONE);
            }

            if (cargo.equals("ingeniero") && nombre.equals(reporte.getIngeniero().toLowerCase())) {
                buttonCerrar.setVisibility(View.VISIBLE);
            } else {
                buttonCerrar.setVisibility(View.GONE);
            }

            buttonEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateReporte(reporte, cargo);
                }
            });

            //the delete operation
            buttonCerrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                    builder.setTitle("Seguro que quiere cerrar");
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //Codigo de Cerrar reporte
                            String sql = "UPDATE eventos \n" +
                                    "SET estado = 'cerrado' \n" +
                                    "WHERE idReporte = ?;\n";
                            mDatabase.execSQL(sql, new Integer[]{reporte.getIdReporte()});
                            Toast.makeText(mCtx, "Reporte Cerrado", Toast.LENGTH_SHORT).show();

                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });


                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });
        }else if(cargo.equals("editor")) {
            buttonCerrar.setVisibility(View.GONE);
            buttonEditar.setText("Crear pregunta");

            buttonEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean val = createPreguntaFaq(reporte, "editor");
                    if(val == true) {
                        buttonEditar.setText("Pregunta creada");
                        buttonEditar.setEnabled(false);
                    }
                }
            });
        }

        return view;
    }


    private boolean createPreguntaFaq(final Reporte reporte, String cargo) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
        final boolean[] val = {false};

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialog_crear_pregunta_reporte, null);
        builder.setView(view);


        final EditText editTextPregunta = view.findViewById(R.id.actpre);
        final EditText editTextRespuesta = view.findViewById(R.id.actres);
        final Spinner spinnerCategoria= view.findViewById(R.id.actspinner);
        final Spinner spinnerEstado= view.findViewById(R.id.actspinner2);

        editTextRespuesta.setText(reporte.clasificacion);

        final AlertDialog dialog = builder.create();
        dialog.show();


        view.findViewById(R.id.Actualizar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pre = editTextPregunta.getText().toString().trim();
                String res = editTextRespuesta.getText().toString().trim();
                String cat = spinnerCategoria.getSelectedItem().toString();
                String es = spinnerEstado.getSelectedItem().toString();
                String vestado = "no";

                if (pre.isEmpty()) {
                    editTextPregunta.setError("Favor de llenar el campo");
                    editTextPregunta.requestFocus();
                    return;
                }

                if (res.isEmpty()) {
                    editTextPregunta.setError("Favor de llenar el campo");
                    editTextPregunta.requestFocus();
                    return;
                }

                String insertSQL = "INSERT INTO correes \n" +
                        "(pregunta, respuesta, categoria, estado, vestado)\n" +
                        "VALUES \n" +
                        "(?, ?, ?, ?, ?);";

                //using the same method execsql for inserting values
                //this time it has two parameters
                //first is the sql string and second is the parameters that is to be binded with the query
                mDatabase.execSQL(insertSQL, new String[]{pre, res, cat, es, vestado});

                val[0] = true;

                Toast.makeText(view.getContext(), "Pregunta registrada", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        return val[0];
    }

    private void updateReporte(final Reporte reporte, String cargo) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialog_update_reporte, null);
        builder.setView(view);


        final EditText reportante = view.findViewById(R.id.reportanteA);
        final EditText clasificacion = view.findViewById(R.id.clasificaionA);
        final EditText descripcion = view.findViewById(R.id.descripcionA);
        final Spinner ingeniero = view.findViewById(R.id.ingenieroA);
        final Spinner modulo = view.findViewById(R.id.modulosA);
        final TextView tIngeniero = view.findViewById(R.id.tIngenieroA);

        reportante.setText(reporte.getReportante());
        clasificacion.setText(reporte.getClasificacion());
        descripcion.setText(reporte.getDescripcion());

        if(cargo.equals("gerenteS")) {
            ingeniero.setVisibility(View.VISIBLE);
            tIngeniero.setVisibility(View.VISIBLE);
        }else {
            ingeniero.setVisibility(View.GONE);
            tIngeniero.setVisibility(View.GONE);
        }

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.actualizar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rep = reportante.getText().toString().trim();
                String clas = clasificacion.getText().toString().trim();
                String desc = descripcion.getText().toString().trim();
                String inge = ingeniero.getSelectedItem().toString();
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

                if (inge.equals("Selecciona un ingeniero")) {
                    inge = "Ingeniero no asignado";
                }

                String sql = "UPDATE eventos \n" +
                        "SET reportante = ?, \n" +
                        "modulo = ?, \n" +
                        "clasificacion = ?, \n" +
                        "descripcion = ?, \n" +
                        "ingeniero = ? \n" +
                        "WHERE idReporte = ?;\n";

                mDatabase.execSQL(sql, new String[]{rep, modu, clas, desc, inge, String.valueOf(reporte.getIdReporte())});
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
}
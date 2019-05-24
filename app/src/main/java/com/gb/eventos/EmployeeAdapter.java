package com.gb.eventos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class EmployeeAdapter extends ArrayAdapter<Employee> {

    Context mCtx;
    int listLayoutRes;
    List<Employee> employeeList;
    SQLiteDatabase mDatabase;

    public EmployeeAdapter(Context mCtx, int listLayoutRes, List<Employee> employeeList, SQLiteDatabase mDatabase) {
        super(mCtx, listLayoutRes, employeeList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.employeeList = employeeList;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);
        Employee employee = null;

        //getting employee of the specified position
        employee = employeeList.get(position);


        //getting views
        TextView textViewPregunta = view.findViewById(R.id.Pregunta);
        TextView textViewRespuesta = view.findViewById(R.id.Respuesta);
        TextView textViewCategoria = view.findViewById(R.id.Categoria);
        TextView textViewEstado = view.findViewById(R.id.estado);


        //adding data to views
        textViewPregunta.setText(employee.getPregunta());
        textViewRespuesta.setText(employee.getRespuesta());
        textViewCategoria.setText(employee.getCategoria());
        textViewEstado.setText(employee.getEstado());


        //we will use these buttons later for update and delete operation
        Button buttonDelete = view.findViewById(R.id.buttonDeleteEmployee);
        Button buttonEdit = view.findViewById(R.id.buttonEditEmployee);

        final Employee finalEmployee = employee;
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEmployee(finalEmployee);
            }
        });

        final Employee finalEmployee1 = employee;
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Estas seguro?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM correes WHERE id = ?";
                        mDatabase.execSQL(sql, new Integer[]{finalEmployee1.getId()});
                        reloadEmployeesFromDatabase();
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

        return view;
    }


    private void updateEmployee(final Employee employee) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialog_update_employee, null);
        builder.setView(view);


        final EditText editTextPregunta = view.findViewById(R.id.actpre);
        final EditText editTextRespuesta = view.findViewById(R.id.actres);
        final Spinner spinnerCategoria= view.findViewById(R.id.actspinner);
        final Spinner spinnerEstado= view.findViewById(R.id.actspinner2);

        editTextPregunta.setText(employee.getPregunta());
        editTextRespuesta.setText(employee.getRespuesta());

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.Actualizar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pre = editTextPregunta.getText().toString().trim();
                String res = editTextRespuesta.getText().toString().trim();
                String cat = spinnerCategoria.getSelectedItem().toString();
                String es = spinnerEstado.getSelectedItem().toString();

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

                String sql = "UPDATE correes \n" +
                        "SET pregunta = ?, \n" +
                        "respuesta = ?, \n" +
                        "categoria = ?, \n" +
                        "estado = ? \n" +
                        "WHERE id = ?;\n";

                mDatabase.execSQL(sql, new String[]{pre, res, cat, es, String.valueOf(employee.getId())});
                Toast.makeText(mCtx, "Pregunta Modificada", Toast.LENGTH_SHORT).show();
                reloadEmployeesFromDatabase();

                dialog.dismiss();
            }
        });


    }

    private void reloadEmployeesFromDatabase() {
        Cursor cursorEmployees = mDatabase.rawQuery("SELECT * FROM correes", null);
        if (cursorEmployees.moveToFirst()) {
            employeeList.clear();
            do {
                employeeList.add(new Employee(
                        cursorEmployees.getInt(0),
                        cursorEmployees.getString(1),
                        cursorEmployees.getString(2),
                        cursorEmployees.getString(3),
                        cursorEmployees.getString(4),
                        cursorEmployees.getString(5)
                ));
            } while (cursorEmployees.moveToNext());
        }
        cursorEmployees.close();
        notifyDataSetChanged();
    }
}

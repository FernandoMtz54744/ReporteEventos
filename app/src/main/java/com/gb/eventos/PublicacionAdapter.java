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

public class PublicacionAdapter extends ArrayAdapter<Employee> {

    Context mCtx;
    int listLayoutRes;
    List<Employee> employeeList;
    SQLiteDatabase mDatabase;

    public PublicacionAdapter(Context mCtx, int listLayoutRes, List<Employee> employeeList, SQLiteDatabase mDatabase) {
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
        View view = null;

        Log.e("size", Integer.toString(employeeList.size()));
        if(employeeList.get(position).getEstado().equals("Publicable") && employeeList.get(position).getVestado().equals("no")) {
            view = inflater.inflate(listLayoutRes, null);

            Employee employee = null;

            //getting employee of the specified position
            if(employeeList.get(position).getEstado().equals("Publicable")) {
                employee = employeeList.get(position);
            }



            try {
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
            }catch (NullPointerException e) {
                Log.e("publicables", "no hay preguntas publicables");
            }


            //we will use these buttons later for update and delete operation
            final Button buttonEdit = view.findViewById(R.id.buttonEditEmployee);

            final Employee finalEmployee = employee;
            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateEmployee(finalEmployee);
                }
            });

        }

        return view;
    }


    private void updateEmployee(final Employee employee) {

        String vestado = "si";

        String sql = "UPDATE correes \n" +
                "SET vestado = ? \n" +
                "WHERE id = ?;\n";

        mDatabase.execSQL(sql, new String[]{vestado, String.valueOf(employee.getId())});
        Toast.makeText(mCtx, "Pregunta publicada", Toast.LENGTH_SHORT).show();
        reloadEmployeesFromDatabase();

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

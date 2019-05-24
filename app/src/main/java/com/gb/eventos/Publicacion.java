package com.gb.eventos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Publicacion extends AppCompatActivity {

    List<Employee> employeeList;
    SQLiteDatabase mDatabase;
    ListView listViewEmployees;
    PublicacionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        listViewEmployees = (ListView) findViewById(R.id.listViewEmployees);
        employeeList = new ArrayList<>();

        //opening the database
        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        //this method will display the employees in the list
        showEmployeesFromDatabase();
    }

    private void showEmployeesFromDatabase() {

        //we used rawQuery(sql, selectionargs) for fetching all the employees
        Cursor cursorEmployees = mDatabase.rawQuery("SELECT * FROM correes", null);

        //if the cursor has some data
        if (cursorEmployees.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
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
        //closing the cursor
        cursorEmployees.close();

        //creating the adapter object
        List<Employee> newEmployeeList = new ArrayList<>();
        for (int i = 0; i < employeeList.size(); i++) {
            if(employeeList.get(i).getEstado().equals("Publicable")) {
                Log.e("num", Integer.toString(i));
                newEmployeeList.add(employeeList.get(i));
            }
        }

        if(newEmployeeList.size() == 0) {
            Toast.makeText(getApplicationContext(), "No hay ninguna pregunta publicable", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), MainActivityFaq.class));
        }else {
            adapter = new PublicacionAdapter(this, R.layout.list_faq_publicacion, newEmployeeList, mDatabase);
            //adding the adapter to listview
            listViewEmployees.setAdapter(adapter);
        }


    }

}

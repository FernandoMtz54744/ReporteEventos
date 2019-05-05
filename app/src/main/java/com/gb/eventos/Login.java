package com.gb.eventos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText nombre, contra;
    Button ingresar;

    static final String Preferences = "UserPreferences";
    static final String Name = "nameKey";
    static final String Position = "positionKey";

    static final String[][] Users = { // Usuario, Contraseña, Cargo
                                        {"gerente", "gerente", "gerente"},
                                        {"ingeniero 1", "ingeniero 1","ingeniero"},
                                        {"ingeniero 2", "ingeniero 2", "ingeniero"},
                                        {"ingeniero 3", "ingeniero 3", "ingeniero"},
                                        {"operador", "operador", "operador"}
                                    };

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nombre = (EditText) findViewById(R.id.nombre);
        contra = (EditText) findViewById(R.id.contra);
        ingresar = (Button) findViewById(R.id.ingresar);

        sharedPreferences = getSharedPreferences(Preferences, Context.MODE_PRIVATE);

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = nombre.getText().toString().trim();
                String pswd = contra.getText().toString().trim();
                String cargo;

                SharedPreferences.Editor editor = sharedPreferences.edit();
                int bandera = 0;

                for(int i = 0; i < Users.length; i++) {
                    if(usuario.equals(Users[i][0]) && pswd.equals(Users[i][1])) {
                        bandera = 1;
                        cargo = Users[i][2];
                        editor.putString(Name, usuario);
                        editor.putString(Position, cargo);
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }

                if(bandera == 0)
                    Toast.makeText(getApplicationContext(), "Usuario no válido", Toast.LENGTH_LONG).show();

            }
        });


    }
}

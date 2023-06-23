package com.example.lallamada;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText etNUmero;
    Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNUmero = (EditText)findViewById(R.id.etNumero);
        btnGuardar = (Button)findViewById(R.id.btnGuardar);

        Intent serviceIntent = new Intent(this, MyBackgroundService.class);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String telefono = sharedPreferences.getString("telefono", "");

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("telefono", etNUmero.getText().toString());
                editor.apply();

                Toast.makeText(MainActivity.this, "Número guardado", Toast.LENGTH_SHORT).show();

                if(isServiceRunning(MainActivity.this, MyBackgroundService.class)) {
                    stopService(serviceIntent);
                    Toast.makeText(MainActivity.this, "Reiniciando", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        if (!telefono.equals("")) {
            etNUmero.setText(telefono);

            startService(serviceIntent);
        }
    }

    public boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo service : runningServices) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }
}

package mykyta.titarenko.nure;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static final String tag = "MainActivity";
    private static String savedString = "";
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_activity_methods_practice);
        editText = findViewById(R.id.editText);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.d(tag, "Create");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        savedString = editText.getText().toString();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        editText.setText(savedString);
    }

    protected void onStart(){
        super.onStart();
        Log.d(tag, "Start");
    }

    protected void onResume(){
        super.onResume();
        Log.d(tag, "Resume");
        editText.setText(savedString);
    }

    protected void onPause(){
        super.onPause();
        Log.d(tag, "Pause");
    }

    protected void onStop(){
        super.onStop();
        Log.d(tag, "Stop");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.d(tag, "Destroy");
    }

    public void MoveInMainAcivity2(View view){
        Intent intent = new Intent(MainActivity.this, activity_finish_practice.class);
        startActivity(intent);
    }
}
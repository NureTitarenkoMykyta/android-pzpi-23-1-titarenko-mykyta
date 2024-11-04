package mykyta.titarenko.practtask4;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SQLitePracticeActivity extends AppCompatActivity {

    DBHelper db = new DBHelper(SQLitePracticeActivity.this, "MyDb", null, 1);
    TextView listItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sqlite_practice);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button chooseButton = findViewById(R.id.chooseButton);
        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextAge = findViewById(R.id.editTextAge);
        listItems = findViewById(R.id.listItems);
        Button deleteButton = findViewById(R.id.deleteButton);
        Button moveToFilePracticeActivityButton = findViewById(R.id.moveToFilePracticeActivity);

        moveToFilePracticeActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SQLitePracticeActivity.this, FilePracticeActivity.class);
                startActivity(intent);
            }
        });

        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                int age = Integer.parseInt(editTextAge.getText().toString());
                db.onInsert(name, age);
                UpdateList();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteAllUsers();
                UpdateList();
            }
        });
        UpdateList();
    }

    public void UpdateList(){
        String list = "";
        Cursor cursor = db.getAllUsers();
        while (cursor.moveToNext()){
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") int age = cursor.getInt(cursor.getColumnIndex("age"));
            list = list + "Name: " + name + " Age: " + age + "\n";
        }
        cursor.close();
        listItems.setText(list);
    }
}
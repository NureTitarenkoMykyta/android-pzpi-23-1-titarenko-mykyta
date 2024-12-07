package mykyta.titarenko.labtask5;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.slider.Slider;

public class Settings extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        int color = applyStyles();
        setContentView(R.layout.activity_settings);
        LinearLayout layout = findViewById(R.id.main);
        layout.setBackgroundColor(ContextCompat.getColor(this, color));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Spinner fontSizeSpinner = findViewById(R.id.fontSizeSpinner);
        String[] fontSizeType = new String[]{"smallFontSize", "mediumFontSize", "highFontSize"};
        ArrayAdapter<String> fontSizeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fontSizeType);
        fontSizeSpinner.setAdapter(fontSizeAdapter);
        Spinner styleSpinner = findViewById(R.id.styleSpinner);
        String[] styleType = new String[]{"brown", "blue"};
        ArrayAdapter<String> styleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, styleType);
        styleSpinner.setAdapter(styleAdapter);
        Button button = findViewById(R.id.confirmButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("fontSize", fontSizeSpinner.getSelectedItem().toString());
                editor.putString("style", styleSpinner.getSelectedItem().toString());
                editor.apply();
                finish();
            }
        });
    }
}
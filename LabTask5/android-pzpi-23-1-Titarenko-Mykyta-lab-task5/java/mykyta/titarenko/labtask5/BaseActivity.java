package mykyta.titarenko.labtask5;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    public int applyStyles(){
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String fontSizeStyle = sharedPreferences.getString("fontSize", "mediumFontSize");
        switch (fontSizeStyle){
            case "smallFontSize":
                setTheme(R.style.smallFontSize);
                break;
            case "mediumFontSize":
                setTheme(R.style.mediumFontSize);
                break;
            case "highFontSize":
                setTheme(R.style.largeFontSize);
                break;
        }
        int color = 0;
        String style = sharedPreferences.getString("style", "blue");
        switch (style){
            case "brown":
                setTheme(R.style.brownStyle);
                color = R.color.brownBackground;
                break;
            case "blue":
                setTheme(R.style.blueStyle);
                color = R.color.blueBackground;
                break;
            default:
                setTheme(R.style.blueStyle);
                color = R.color.blueBackground;
                break;
        }
        return color;
    }
}

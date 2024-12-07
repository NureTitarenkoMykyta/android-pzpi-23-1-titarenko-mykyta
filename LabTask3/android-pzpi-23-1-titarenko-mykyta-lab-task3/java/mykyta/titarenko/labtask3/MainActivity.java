package mykyta.titarenko.labtask3;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    double resultValue = 0;
    List<Number> numbers = new ArrayList<>();
    List<String> operations = new ArrayList<>();
    boolean resultGet = false;
    String currentNumber = "";
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        result = findViewById(R.id.result);
        Button one = findViewById(R.id.one);
        Button two = findViewById(R.id.two);
        Button three = findViewById(R.id.three);
        Button four = findViewById(R.id.four);
        Button five = findViewById(R.id.five);
        Button six = findViewById(R.id.six);
        Button seven = findViewById(R.id.seven);
        Button eight = findViewById(R.id.eigth);
        Button nine = findViewById(R.id.nine);
        Button zerow = findViewById(R.id.zero);
        Button twoZeros = findViewById(R.id.twoZeros);
        Button coma = findViewById(R.id.coma);
        Button addition = findViewById(R.id.addition);
        Button subtraction = findViewById(R.id.subtraction);
        Button multiplication = findViewById(R.id.multiplication);
        Button division = findViewById(R.id.division);
        Button equal = findViewById(R.id.equal);
        Button AC = findViewById(R.id.AC);
        Button del = findViewById(R.id.Del);

        List<Button> digitsButtons = new ArrayList<>();

        digitsButtons.add(zerow);
        digitsButtons.add(twoZeros);
        digitsButtons.add(coma);
        digitsButtons.add(one);
        digitsButtons.add(two);
        digitsButtons.add(three);
        digitsButtons.add(four);
        digitsButtons.add(five);
        digitsButtons.add(six);
        digitsButtons.add(seven);
        digitsButtons.add(eight);
        digitsButtons.add(nine);

        List<Button> operationsButtons = new ArrayList<>();

        operationsButtons.add(addition);
        operationsButtons.add(subtraction);
        operationsButtons.add(multiplication);
        operationsButtons.add(division);


        for (Button button : digitsButtons){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentNumber.equals("") && (((Button)v).getText().equals("00") || (((Button)v).getText().equals(".")))){
                        return;
                    }

                    if (currentNumber.equals("0") && (((Button)v).getText().equals("00") || ((Button)v).getText().equals("0"))){
                        return;
                    }

                    if (((Button)v).getText().equals(".") && (currentNumber.charAt(currentNumber.length() - 1) == '.' || Double.parseDouble(currentNumber) % 1 != 0)){
                        return;
                    }

                    if (((Button)v).getText().equals(".") && currentNumber.contains(".") && currentNumber.charAt(currentNumber.length() - 1) == '0' && (currentNumber.charAt(currentNumber.length() - 2) == '0' || currentNumber.charAt(currentNumber.length() - 2) == '.')){
                        return;
                    }

                    if (!CheckLength()){
                        return;
                    }

                    if (resultGet == true){
                        numbers.remove(0);
                        resultGet = false;
                        result.setText("");
                    }
                    result.setText(result.getText() + "" + ((Button)v).getText());
                    currentNumber += ((Button)v).getText();
                }
            });
        }

        for (Button button : operationsButtons){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!CheckLength() || (currentNumber.equals("") && numbers.size() == 0)){
                        return;
                    }

                    if (operations.size() >= numbers.size() && currentNumber.equals("")){
                        operations.remove(operations.size() - 1);
                        result.setText(result.getText().subSequence(0, result.getText().length() - 1));
                        result.setText(result.getText() + "" + ((Button)v).getText());
                        operations.add( "" + ((Button)v).getText() );
                        return;
                    }

                    result.setText(result.getText() + "" + ((Button)v).getText());

                    if (resultGet){
                        resultGet = false;
                    } else if (!currentNumber.equals("")){

                        numbers.add(Double.parseDouble(currentNumber));
                        currentNumber = "";
                    }

                    operations.add( "" + ((Button)v).getText() );
                }
            });
        }

        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (numbers.size() == 0){
                    return;
                }

                if (currentNumber != ""){
                    numbers.add(Double.parseDouble(currentNumber));
                    currentNumber = "";
                }

                if (operations.size() >= numbers.size()){
                    operations.remove(operations.size() - 1);
                }

                for (int i = 0; i < operations.size(); i++){
                    if (operations.get(i).equals("*") || operations.get(i).equals("/")){
                        Calculate(i);
                        i--;
                    }
                }

                for (int i = 0; i < operations.size(); i++){
                    if (operations.get(i).equals("+") || operations.get(i).equals("-")){
                        Calculate(i);
                        i--;
                    }
                }

                if (resultValue % 1 == 0 || resultValue * (-1) % 1 == 0){
                    result.setText(String.valueOf( (int) (resultValue) ));
                } else {
                    result.setText(String.valueOf(resultValue));
                }


                resultGet = true;
            }
        });

        AC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllClear();
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (resultGet){
                    AllClear();
                    return;
                }

                if (!currentNumber.equals("")) {
                    currentNumber = currentNumber.substring(0, currentNumber.length() - 1);
                } else if (operations.size() < numbers.size()){
                    int index = numbers.size() - 1;
                    numbers.set(index, Math.floor((double) numbers.get(index) / 10));
                    if (numbers.get(index).equals(0.0)){
                        numbers.remove(index);
                    }
                } else {
                    operations.remove(operations.size() - 1);
                }

                result.setText(result.getText().subSequence(0, result.getText().length() - 1));
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String savedResult = result.getText().toString();
        outState.putString("savedResult", savedResult);
        outState.putString("currentNumber" , currentNumber);
        outState.putSerializable("operations", new ArrayList<>(operations));
        outState.putSerializable("numbers", new ArrayList<>(numbers));
        outState.putBoolean("resultGet", resultGet);
        outState.putDouble("resultValue", resultValue);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        result.setText(savedInstanceState.getString("savedResult"));
        numbers = (ArrayList<Number>) savedInstanceState.getSerializable("numbers");
        operations = (ArrayList<String>) savedInstanceState.getSerializable("operations");
        resultGet = savedInstanceState.getBoolean("resultGet");
        resultValue = savedInstanceState.getDouble("resultValue");
        currentNumber = savedInstanceState.getString("currentNumber");
    }

    public void Calculate(int i){

        double firstOperand = (double) numbers.get(i);
            numbers.remove(i);
        double secondOperand = (double) numbers.get(i);
            numbers.remove(i);
            if (operations.get(i).equals("*")) {
                resultValue = firstOperand * secondOperand;
            } else if (operations.get(i).equals("/")){
                resultValue = firstOperand / secondOperand;
            } else if (operations.get(i).equals("+")) {
                resultValue = firstOperand + secondOperand;
            } else if (operations.get(i).equals("-")){
                resultValue = firstOperand - secondOperand;
            }
            DecimalFormat decimalFormat = new DecimalFormat("#.#####");
            resultValue = Double.parseDouble(decimalFormat.format(resultValue));
            operations.remove(i);
            numbers.add(i, resultValue);
    }

    public boolean CheckLength(){
        if (result.getText().length() >= 11){
            return false;
        } else {
            return true;
        }
    }

    public void AllClear(){
        currentNumber = "";
        resultValue = 0;
        operations.clear();
        numbers.clear();
        result.setText("");
        resultGet = false;
    }

}
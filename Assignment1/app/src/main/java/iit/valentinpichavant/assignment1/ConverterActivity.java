package iit.valentinpichavant.assignment1;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Double.parseDouble;

public class ConverterActivity extends AppCompatActivity {

    // The constraint layout of the Converter Activity.
    private ConstraintLayout constraintLayout;

    // The RadioButton which is check when you want to convert celsius to fahrenheit.
    private RadioButton radioButtonCtoF;

    // The input TextView which contains the input typed by the user.
    private TextView inputTextView;

    // The output TextView which contains the result of the conversion typed by the user.
    private TextView outputTextView;

    // This TextView contains the history of all the conversions done by the user.
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Save the constraint layout
        constraintLayout = (ConstraintLayout) this.findViewById(android.R.id.content).findViewById(R.id.activity_main);
        // Save the radio button celsius to fahrenheit
        radioButtonCtoF = (RadioButton) constraintLayout.findViewById(R.id.radioButtonCtoF);
        // Save the input text view
        inputTextView = (TextView) constraintLayout.findViewById(R.id.inputText);
        // Save the output text view
        outputTextView = (TextView) constraintLayout.findViewById(R.id.outputText);
        // Save the result text view
        textViewResult = (TextView) constraintLayout.findViewById(R.id.textViewResult);
        textViewResult.setMovementMethod(new ScrollingMovementMethod());
    }

    /**
     * This method is used to convert an input value typed by the user.
     * It update the result view to add the history and update the field to show the conversion result.
     * The method handle bad number and display a Toast if the user fail to chose a valid number like ".
     *
     * @param view The view which called the method.
     */
    public void convert(View view) {
        // We get the input value
        String valueToConvert = inputTextView.getText().toString();
        // If the conversion button celsius to fahrenheit is checked then
        if (radioButtonCtoF.isChecked()) {
            // We try to calculate the value of the conversion
            try {
                // We parsed the value to a double
                Double parsedValue = parseDouble(valueToConvert) * 9.0 / 5.0 + 32.0;
                String calculatedValue = parsedValue.toString();
                // We handle if there is more than one value after the "."
                if (calculatedValue.contains(".")) {
                    calculatedValue = calculatedValue.substring(0, calculatedValue.indexOf(".") + 2);
                }
                // We update the output view
                outputTextView.setText(calculatedValue);
                // We save in the history the calculated value
                textViewResult.setText(new String(valueToConvert + " C is " + calculatedValue) + " F.\n" + textViewResult.getText());
                // If we can't format the number
            } catch (NumberFormatException nfe) {
                // We display the error
                nfe.printStackTrace();
                // We indicate the error in the history
                textViewResult.setText(new String("Invalid number!\n" + textViewResult.getText()));
                // We inform the user of the fault with a Toast
                Context context = this.getApplicationContext();
                CharSequence text = "Invalid number!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            // Else means the conversion button fahrenheit to celsius is checked then
        } else {
            // We try to calculate the value of the conversion
            try {
                // We parsed the value to a double
                Double parsedValue = (parseDouble(valueToConvert) - 32.0) * 5.0 / 9.0;
                String calculatedValue = parsedValue.toString();
                // We handle if there is more than one value after the "."
                if (calculatedValue.contains(".")) {
                    calculatedValue = calculatedValue.substring(0, calculatedValue.indexOf(".") + 2);
                }
                // We update the output view
                outputTextView.setText(calculatedValue);
                // We save in the history the calculated value
                textViewResult.setText(new String(valueToConvert + " F is " + calculatedValue) + " C.\n" + textViewResult.getText());
            } catch (NumberFormatException nfe) {
                // We display the error
                nfe.printStackTrace();
                // We indicate the error in the history
                textViewResult.setText(new String("Invalid number!\n" + textViewResult.getText()));
                // We inform the user of the fault with a Toast
                Context context = this.getApplicationContext();
                CharSequence text = "Invalid number!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }
}

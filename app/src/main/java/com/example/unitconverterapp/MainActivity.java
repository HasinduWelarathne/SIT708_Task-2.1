package com.example.unitconverterapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner sourceUnitSpinner, destinationUnitSpinner, UnitSpinner;
    EditText inputValue;
    Button convertButton;
    TextView resultTextView;

    ArrayAdapter<String> sourceUnitAdapter;
    ArrayAdapter<String> destinationUnitAdapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UnitSpinner = findViewById(R.id.spinner_unit);
        sourceUnitSpinner = findViewById(R.id.spinner1);
        destinationUnitSpinner = findViewById(R.id.spinner2);
        inputValue = findViewById(R.id.value1);
        convertButton = findViewById(R.id.convert_button);
        resultTextView = findViewById(R.id.result_text_view);

        sourceUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        sourceUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        destinationUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sourceUnitSpinner.setAdapter(sourceUnitAdapter);
        destinationUnitSpinner.setAdapter(destinationUnitAdapter);

        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(this,
                R.array.measurement_array, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        UnitSpinner.setAdapter(unitAdapter);

        setDefaultSelections();


        UnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMeasurement = parent.getItemAtPosition(position).toString();
                updateAdapters(selectedMeasurement);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertUnits();
            }
        });
    }

    // Method to update adapters based on selected measurement
    private void updateAdapters(String selectedMeasurement) {
        switch (selectedMeasurement) {
            case "Length":
                sourceUnitAdapter.clear();
                destinationUnitAdapter.clear();
                sourceUnitAdapter.addAll(getResources().getStringArray(R.array.length_units_array));
                destinationUnitAdapter.addAll(getResources().getStringArray(R.array.length_units_array));
                break;
            case "Weight":
                sourceUnitAdapter.clear();
                destinationUnitAdapter.clear();
                sourceUnitAdapter.addAll(getResources().getStringArray(R.array.weight_units_array));
                destinationUnitAdapter.addAll(getResources().getStringArray(R.array.weight_units_array));
                break;
            case "Temperature":
                sourceUnitAdapter.clear();
                destinationUnitAdapter.clear();
                sourceUnitAdapter.addAll(getResources().getStringArray(R.array.temp_units_array));
                destinationUnitAdapter.addAll(getResources().getStringArray(R.array.temp_units_array));
                break;
        }
    }

    public void interchangeSpinners(View view) {
        // Swap the selected items of the source and destination spinners
        int sourcePosition = sourceUnitSpinner.getSelectedItemPosition();
        int destinationPosition = destinationUnitSpinner.getSelectedItemPosition();

        sourceUnitSpinner.setSelection(destinationPosition);
        destinationUnitSpinner.setSelection(sourcePosition);
    }


    private void convertUnits() {
        String measurement = UnitSpinner.getSelectedItem().toString();
        String sourceUnit = sourceUnitSpinner.getSelectedItem().toString();
        String destinationUnit = destinationUnitSpinner.getSelectedItem().toString();
        String inputValueStr = inputValue.getText().toString();

        if (!validateInput(inputValueStr)) {
            return;
        }

        // Check if the input value is not empty
        if (!inputValueStr.isEmpty()) {
            double value = Double.parseDouble(inputValueStr);
            double result = 0.0;

            if (sourceUnit.equals(destinationUnit)) {
                resultTextView.setText("Source unit and destination unit are the same");
                return;
            }

            switch (measurement) {
                case "Length":
                    if (value <= 0) {
                        resultTextView.setText("Please enter a value greater than 0");
                        return;
                    }
                    result = convertLength(sourceUnit, destinationUnit, value);
                    break;
                case "Weight":
                    if (value <= 0) {
                        resultTextView.setText("Please enter a value greater than 0");
                        return;
                    }
                    result = convertWeight(sourceUnit, destinationUnit, value);
                    break;
                case "Temperature":
                    result = convertTemperature(sourceUnit, destinationUnit, value);
                    break;
            }

            resultTextView.setText(String.valueOf(result));
        } else {

            resultTextView.setText("Please enter a value");
        }
    }


    private double convertLength(String sourceUnit, String destinationUnit, double value) {


        double convertedValue = 0.0;

        System.out.println("Source Unit: " + sourceUnit);
        System.out.println("Destination Unit: " + destinationUnit);

        if (sourceUnit.equals("Inch")) {
            if (destinationUnit.equals("Centimeter")) {
                convertedValue = value * 2.54;
            } else if (destinationUnit.equals("Foot")) {
                convertedValue = value / 12.0;
            } else if (destinationUnit.equals("Yard")) {
                convertedValue = value / 36.0;
            } else if (destinationUnit.equals("Mile")) {
                convertedValue = value / 63360.0;
            }
        } else if (sourceUnit.equals("Foot")) {
            if (destinationUnit.equals("Inch")) {
                convertedValue = value * 12.0;
            } else if (destinationUnit.equals("Centimeter")) {
                convertedValue = value * 30.48;
            } else if (destinationUnit.equals("Yard")) {
                convertedValue = value / 3.0;
            } else if (destinationUnit.equals("Mile")) {
                convertedValue = value / 5280.0;
            }
        } else if (sourceUnit.equals("Yard")) {
            if (destinationUnit.equals("Inch")) {
                convertedValue = value * 36.0;
            } else if (destinationUnit.equals("Foot")) {
                convertedValue = value * 3.0;
            } else if (destinationUnit.equals("Centimeter")) {
                convertedValue = value * 91.44;
            } else if (destinationUnit.equals("Mile")) {
                convertedValue = value / 1760.0;
            }
        } else if (sourceUnit.equals("Mile")) {
            if (destinationUnit.equals("Inch")) {
                convertedValue = value * 63360.0;
            } else if (destinationUnit.equals("Foot")) {
                convertedValue = value * 5280.0;
            } else if (destinationUnit.equals("Yard")) {
                convertedValue = value * 1760.0;
            } else if (destinationUnit.equals("Centimeter")) {
                convertedValue = value * 160934.4;
            }
        }else if (sourceUnit.equals("Kilometer")) {
            if (destinationUnit.equals("Pound")) {
                convertedValue = value * 2204.62; // 1 Kilometer = 2204.62 Pounds
            } else if (destinationUnit.equals("Ounce")) {
                convertedValue = value * 35274; // 1 Kilometer = 35274 Ounces
            } else if (destinationUnit.equals("Ton")) {
                convertedValue = value / 907.185; // 1 Kilometer = 0.00110231 Tons
            } else if (destinationUnit.equals("Kilogram")) {
                convertedValue = value * 1000; // 1 Kilometer = 1000 Kilograms
            } else {
                convertedValue = Double.NaN; // Unrecognized destination unit
            }
        }

        return convertedValue;

    }

    private double convertWeight(String sourceUnit, String destinationUnit, double value) {
        double convertedValue = 0;

        if (sourceUnit.equals("Pound")) {
            if (destinationUnit.equals("Kilogram")) {
                convertedValue = value * 0.453592;
            } else if (destinationUnit.equals("Gram")) {
                convertedValue = value * 453.592;
            } else if (destinationUnit.equals("Ounce")) {
                convertedValue = value * 16.0;
            } else if (destinationUnit.equals("Ton")) {
                convertedValue = value / 2000.0;
            }
        } else if (sourceUnit.equals("Ounce")) {
            if (destinationUnit.equals("Pound")) {
                convertedValue = value / 16.0;
            } else if (destinationUnit.equals("Kilogram")) {
                convertedValue = value * 0.0283495;
            } else if (destinationUnit.equals("Gram")) {
                convertedValue = value * 28.3495;
            } else if (destinationUnit.equals("Ton")) {
                convertedValue = value / 35274.0;
            }
        } else if (sourceUnit.equals("Ton")) {
            if (destinationUnit.equals("Pound")) {
                convertedValue = value * 2000.0;
            } else if (destinationUnit.equals("Kilogram")) {
                convertedValue = value * 907.185;
            } else if (destinationUnit.equals("Ounce")) {
                convertedValue = value * 35274.0;
            } else if (destinationUnit.equals("Gram")) {
                convertedValue = value * 907185.0;
            }
        } else if (sourceUnit.equals("Kilogram")) {
            if (destinationUnit.equals("Pound")) {
                convertedValue = value * 2.20462;
            } else if (destinationUnit.equals("Ounce")) {
                convertedValue = value * 35.274;
            } else if (destinationUnit.equals("Ton")) {
                convertedValue = value / 907.185;
            } else if (destinationUnit.equals("Gram")) {
                convertedValue = value * 1000.0;
            }
        } else if (sourceUnit.equals("Gram")) {
            if (destinationUnit.equals("Pound")) {
                convertedValue = value * 0.00220462;
            } else if (destinationUnit.equals("Ounce")) {
                convertedValue = value * 0.035274;
            } else if (destinationUnit.equals("Ton")) {
                convertedValue = value / 1000000.0;
            } else if (destinationUnit.equals("Kilogram")) {
                convertedValue = value / 1000.0;
            }
        }


        return convertedValue;
    }

    private double convertTemperature(String sourceUnit, String destinationUnit, double value) {
        double convertedValue = 0.0;


        if (sourceUnit.equals("Celsius")) {
            if (destinationUnit.equals("Fahrenheit")) {
                convertedValue = (value * 1.8) + 32;
            } else if (destinationUnit.equals("Kelvin")) {
                convertedValue = value + 273.15;
            }
        } else if (sourceUnit.equals("Fahrenheit")) {
            if (destinationUnit.equals("Celsius")) {
                convertedValue = (value - 32) / 1.8;
            } else if (destinationUnit.equals("Kelvin")) {
                convertedValue = (value + 459.67) * 5 / 9;
            }
        } else if (sourceUnit.equals("Kelvin")) {
            if (destinationUnit.equals("Celsius")) {
                convertedValue = value - 273.15;
            } else if (destinationUnit.equals("Fahrenheit")) {
                convertedValue = (value * 9 / 5) - 459.67;
            }
        }

        return convertedValue;
    }

    private void setDefaultSelections() {
        // Set default selections for all spinners to the first item
        UnitSpinner.setSelection(0);
        sourceUnitSpinner.setSelection(0);
        destinationUnitSpinner.setSelection(1);
    }

    private boolean validateInput(String inputValueStr) {
        // Check if the input value is empty
        if (inputValueStr.isEmpty()) {
            resultTextView.setText("Please enter a value");
            return false;
        }

        // Check if the input value is a valid number
        try {
            double value = Double.parseDouble(inputValueStr);
        } catch (NumberFormatException e) {
            resultTextView.setText("Invalid input. Please enter a valid number.");
            return false;
        }

        return true;
    }
}

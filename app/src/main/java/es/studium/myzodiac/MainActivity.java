package es.studium.myzodiac;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private Button btnAccept;
    private EditText editTextDate;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private boolean isDateSetByUser = false; // Bandera para controlar la inicialización

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datePicker = findViewById(R.id.datePicker);
        btnAccept = findViewById(R.id.btnAccept);
        editTextDate = findViewById(R.id.editTextDate);

        // Ocultar el DatePicker al inicio si deseas que no se muestre una fecha inicial visible
        datePicker.init(0, 0, 0, null); // Inicializa pero sin asignar una fecha

        // Listener para detectar cambios en el EditText
        editTextDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se necesita implementar
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No se necesita implementar
            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputDate = s.toString();
                if (inputDate.length() == 10) { // Formato esperado: dd/MM/yyyy
                    String[] dateParts = inputDate.split("/");
                    try {
                        int day = Integer.parseInt(dateParts[0]);
                        int month = Integer.parseInt(dateParts[1]);
                        int year = Integer.parseInt(dateParts[2]);

                        // Validar el rango de mes
                        if (month < 1 || month > 12) {
                            Toast.makeText(MainActivity.this, "Mes inválido. Usa un mes entre 1 y 12.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Definir los días máximos para cada mes
                        int maxDaysInMonth;
                        switch (month) {
                            case 4: case 6: case 9: case 11: // Meses con 30 días
                                maxDaysInMonth = 30;
                                break;
                            case 2: // Febrero
                                // Verificar años bisiestos
                                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                                    maxDaysInMonth = 29;
                                } else {
                                    maxDaysInMonth = 28;
                                }
                                break;
                            default: // Meses con 31 días
                                maxDaysInMonth = 31;
                        }

                        // Validar el rango de días
                        if (day < 1 || day > maxDaysInMonth) {
                            Toast.makeText(MainActivity.this, "Día inválido. El mes ingresado tiene un máximo de " + maxDaysInMonth + " días.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Si la fecha es válida, actualiza el DatePicker
                        datePicker.updateDate(year, month - 1, day); // `month - 1` porque `DatePicker` usa meses cero-basados
                        isDateSetByUser = true;
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        Toast.makeText(MainActivity.this, "Fecha inválida. Usa el formato dd/MM/yyyy", Toast.LENGTH_SHORT).show();
                    }
                }
            }


        });

        // Listener del botón de aceptar (sin cambios)
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDateSetByUser) {
                    Toast.makeText(MainActivity.this, "Por favor ingresa una fecha válida.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                Calendar today = Calendar.getInstance();
                Calendar birthDate = Calendar.getInstance();
                birthDate.set(year, month, day);

                if (birthDate.after(today)) {
                    Toast.makeText(MainActivity.this, getString(R.string.fecha_invalida), Toast.LENGTH_SHORT).show();
                } else {
                    int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
                    if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
                        age--;
                    }

                    String zodiacSign = getZodiacSign(day, month + 1); // Month is zero-based
                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                    intent.putExtra("AGE", age);
                    intent.putExtra("ZODIAC", zodiacSign);
                    startActivity(intent);
                }
            }
        });
    }

    private String getZodiacSign(int day, int month) {
        if ((month == 1 && day >= 20) || (month == 2 && day <= 18)) {
            return getString(R.string.acuario);
        } else if ((month == 2 && day >= 19) || (month == 3 && day <= 20)) {
            return getString(R.string.piscis);
        } else if ((month == 3 && day >= 21) || (month == 4 && day <= 19)) {
            return getString(R.string.aries);
        } else if ((month == 4 && day >= 20) || (month == 5 && day <= 20)) {
            return getString(R.string.tauro);
        } else if ((month == 5 && day >= 21) || (month == 6 && day <= 20)) {
            return getString(R.string.geminis);
        } else if ((month == 6 && day >= 21) || (month == 7 && day <= 22)) {
            return getString(R.string.cancer);
        } else if ((month == 7 && day >= 23) || (month == 8 && day <= 22)) {
            return getString(R.string.leo);
        } else if ((month == 8 && day >= 23) || (month == 9 && day <= 22)) {
            return getString(R.string.virgo);
        } else if ((month == 9 && day >= 23) || (month == 10 && day <= 22)) {
            return getString(R.string.libra);
        } else if ((month == 10 && day >= 23) || (month == 11 && day <= 21)) {
            return getString(R.string.escorpio);
        } else if ((month == 11 && day >= 22) || (month == 12 && day <= 21)) {
            return getString(R.string.sagitario);
        } else {
            return getString(R.string.capricornio);
        }
    }
}

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
                if (inputDate.length() == 10) { // dd/MM/yyyy
                    try {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setLenient(false); // Establece modo estricto para lanzar excepción si la fecha es inválida
                        calendar.setTime(dateFormat.parse(inputDate));

                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int month = calendar.get(Calendar.MONTH); // Zero-based
                        int year = calendar.get(Calendar.YEAR);

                        // Validar días para el mes ingresado
                        if (day < 1 || day > calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                            throw new ParseException("Día inválido", 0);
                        }

                        // Actualiza el DatePicker con la fecha ingresada si es válida
                        datePicker.updateDate(year, month, day);
                        isDateSetByUser = true; // Marca la bandera para indicar que el usuario ingresó una fecha
                    } catch (ParseException e) {
                        Toast.makeText(MainActivity.this, "Fecha inválida. Usa dd/MM/yyyy", Toast.LENGTH_SHORT).show();
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

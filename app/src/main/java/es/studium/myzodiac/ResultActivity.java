package es.studium.myzodiac;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private TextView tvAge, tvZodiac;
    private ImageView ivZodiacSymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvAge = findViewById(R.id.tvAge);
        tvZodiac = findViewById(R.id.tvZodiac);
        ivZodiacSymbol = findViewById(R.id.ivZodiacSymbol);

        int age = getIntent().getIntExtra("AGE", 0);
        String zodiac = getIntent().getStringExtra("ZODIAC");

        tvAge.setText(getString(R.string.texto_años, age));
        tvZodiac.setText(zodiac);

        // Set the appropriate zodiac symbol image based on the zodiac sign
        int imageResource = getResources().getIdentifier(zodiac.toLowerCase(), "drawable", getPackageName());
        ivZodiacSymbol.setImageResource(imageResource);
    }
}

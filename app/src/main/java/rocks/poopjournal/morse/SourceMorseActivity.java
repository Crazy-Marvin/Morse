package rocks.poopjournal.morse;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.List;

import rocks.poopjournal.morse.utils.AppPrefrences;

public class SourceMorseActivity extends AppCompatActivity {
    ConstraintLayout main;
    ImageView back_button;

    AppPrefrences prefs;
    RadioButton arabic_radio, chinese_radio, latin_radio, greek_radio, persian_radio, hebrew_radio, cyrilic_radio, devangari_radio, korean_radio, kurdish_radio, thai_radio;

    List<RadioButton> allRadios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_morse);
        main = findViewById(R.id.main);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // Apply insets padding to avoid notch / status bar / nav bar overlap
        ViewCompat.setOnApplyWindowInsetsListener(main, (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        back_button = findViewById(R.id.backIcon);
        arabic_radio = findViewById(R.id.arabic_radio);
        chinese_radio = findViewById(R.id.chinese_radio);
        latin_radio = findViewById(R.id.latin_radio);
        greek_radio = findViewById(R.id.greek_radio);
        persian_radio = findViewById(R.id.persian_radio);
        hebrew_radio = findViewById(R.id.hebrew_radio);
        cyrilic_radio = findViewById(R.id.cyrilic_radio);
        devangari_radio = findViewById(R.id.devanagari_radio);
        korean_radio = findViewById(R.id.korean_radio);
        kurdish_radio = findViewById(R.id.kurdish_radio);
        thai_radio = findViewById(R.id.thai_radio);
        prefs = new AppPrefrences(this);

        allRadios = Arrays.asList(
                arabic_radio,
                chinese_radio,
                latin_radio,
                greek_radio,
                persian_radio,
                hebrew_radio,
                cyrilic_radio,
                devangari_radio,
                korean_radio,
                kurdish_radio,
                thai_radio
        );

        // Load saved selection (defaults to Latin)
        String savedLang = prefs.getSourceLanguage();
        selectSavedLanguage(savedLang);

        setupRadioListeners();
        back_button.setOnClickListener(v -> finish());


    }
    private void setupRadioListeners() {
        View.OnClickListener listener = v -> {
            RadioButton selected = (RadioButton) v;

            // Reset all others
            for (RadioButton rb : allRadios) {
                rb.setChecked(false);
                rb.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#98A0AE"))); // gray
            }

            // Highlight the selected one
            selected.setChecked(true);
            selected.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#00CCAA"))); // teal

            String selectedLang = "";

            // ✅ Replaced switch with if-else chain
            int id = v.getId();
            if (id == R.id.arabic_radio) selectedLang = "Arabic";
            else if (id == R.id.chinese_radio) selectedLang = "Chinese";
            else if (id == R.id.latin_radio) selectedLang = "Latin";
            else if (id == R.id.greek_radio) selectedLang = "Greek";
            else if (id == R.id.persian_radio) selectedLang = "Persian";
            else if (id == R.id.hebrew_radio) selectedLang = "Hebrew";
            else if (id == R.id.cyrilic_radio) selectedLang = "Cyrillic";
            else if (id == R.id.devanagari_radio) selectedLang = "Devanagari";
            else if (id == R.id.korean_radio) selectedLang = "Korean";
            else if (id == R.id.kurdish_radio) selectedLang = "Kurdish";
            else if (id == R.id.thai_radio) selectedLang = "Thai";

            if (!selectedLang.isEmpty()) {
                prefs.saveSourceLanguage(selectedLang);
            }
        };

        for (RadioButton rb : allRadios) rb.setOnClickListener(listener);
    }

    private void selectSavedLanguage(String lang) {
        for (RadioButton rb : allRadios) {
            rb.setChecked(false);
            rb.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#98A0AE"))); // default gray
        }

        RadioButton selected = null;

        switch (lang) {
            case "Arabic": selected = arabic_radio; break;
            case "Chinese": selected = chinese_radio; break;
            case "Greek": selected = greek_radio; break;
            case "Persian": selected = persian_radio; break;
            case "Hebrew": selected = hebrew_radio; break;
            case "Cyrillic": selected = cyrilic_radio; break;
            case "Devanagari": selected = devangari_radio; break;
            case "Korean": selected = korean_radio; break;
            case "Kurdish": selected = kurdish_radio; break;
            case "Thai": selected = findViewById(R.id.thai_radio); break;
            default:
                selected = latin_radio;
                prefs.saveSourceLanguage("Latin");
        }

        if (selected != null) {
            selected.setChecked(true);
            selected.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#00CCAA"))); // highlight color
        }
    }
}

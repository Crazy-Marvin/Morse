package rocks.poopjournal.morse;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;

import rocks.poopjournal.morse.utils.AppPrefrences;

public class SettingsActivity extends AppCompatActivity {

    ConstraintLayout main;
    MaterialCardView card_source, about;
    LinearLayout card_Method, card_duration;

    TextView tv_methodName, tv_durationSpeed, txtLanguage;

    ImageView backIcon;

    AppPrefrences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        main = findViewById(R.id.main);
        prefs = new AppPrefrences(this);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // Apply insets padding to avoid notch / status bar / nav bar overlap
        ViewCompat.setOnApplyWindowInsetsListener(main, (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        card_source = findViewById(R.id.card_source);
        about = findViewById(R.id.about);
        card_Method = findViewById(R.id.card_method);
        card_duration = findViewById(R.id.card_duration);
        tv_methodName = findViewById(R.id.tv_methodName);
        tv_durationSpeed = findViewById(R.id.tv_durationSpeed);
        txtLanguage = findViewById(R.id.txtLanguage);
        backIcon = findViewById(R.id.backIcon);
        updateSummaries();

        about.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this, AboutActivity.class)));

        card_source.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this, SourceMorseActivity.class)));

        card_Method.setOnClickListener(v -> showMethodDialog());

        card_duration.setOnClickListener(v -> showDurationDialog());

        backIcon.setOnClickListener(v -> finish());
    }

    private void updateSummaries() {
        if (txtLanguage != null) txtLanguage.setText(prefs.getSourceLanguage());
        if (tv_methodName != null) tv_methodName.setText(prefs.getMethod());
        if (tv_durationSpeed != null) tv_durationSpeed.setText(prefs.getDuration() + " ms");
    }

    private void showMethodDialog() {
        final String[] methods = {"Standard word", "Farnsworth speed", "Wordsworth speed", "Dot duration"};
        String current = prefs.getMethod();
        TextView customTitle = new TextView(this);
        customTitle.setText("Select Method"); // your title text
        customTitle.setTextColor(Color.WHITE);
        customTitle.setTypeface(ResourcesCompat.getFont(this, R.font.ibmplexsans_regular), Typeface.BOLD);
        customTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        customTitle.setPadding(50, 40, 50, 20);
        // Use the custom theme here 👇
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.RoundedAlertDialog);
        builder.setCustomTitle(customTitle);
        builder.setSingleChoiceItems(methods, getSelectedIndex(methods, current), (dialog, which) -> {
            prefs.saveMethod(methods[which]);
            updateSummaries();
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(d -> {

            // Optional: make list text white if default theme doesn’t apply automatically
            ListView listView = dialog.getListView();
            listView.setBackgroundColor(Color.TRANSPARENT);

            for (int i = 0; i < listView.getChildCount(); i++) {
                TextView tv = (TextView) listView.getChildAt(i);
                if (tv != null) {
                    tv.setTextColor(Color.WHITE);
                    tv.setTypeface(ResourcesCompat.getFont(this, R.font.ibmplexsans_regular));
                }
            }
        });
        dialog.show();

    }

    private int getSelectedIndex(String[] arr, String value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(value)) return i;
        }
        return 0;
    }

    private void showDurationDialog() {
        String selectedMethod = prefs.getMethod();
        int currentMs = prefs.getDuration();

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_duration, null);
        EditText inputMs = dialogView.findViewById(R.id.input_duration);
        inputMs.setText(String.valueOf(currentMs));

        TextView customTitle = new TextView(this);
        customTitle.setText(selectedMethod);
        customTitle.setTextColor(Color.WHITE);
        customTitle.setTypeface(ResourcesCompat.getFont(this, R.font.ibmplexsans_regular), Typeface.BOLD);
        customTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        customTitle.setPadding(50, 40, 50, 20);


        AlertDialog dialog = new AlertDialog.Builder(this, R.style.RoundedAlertDialog)
                .setCustomTitle(customTitle)
                .setView(dialogView)
                .setPositiveButton("Done", (d, which) -> {
                    String val = inputMs.getText().toString().trim();
                    if (!val.isEmpty()) {
                        try {
                            int ms = Integer.parseInt(val);
                            prefs.saveDuration(ms);
                            updateSummaries();
                        } catch (NumberFormatException ignored) {
                        }
                    }
                })
                .create();

        dialog.show();
        dialog.setOnShowListener(d -> {
            int titleId = dialog.getContext().getResources()
                    .getIdentifier("alertTitle", "id", "android");
            TextView title = dialog.findViewById(titleId);
            if (title != null) {
                title.setTextColor(Color.WHITE);
                title.setTypeface(ResourcesCompat.getFont(this, R.font.ibmplexsans_regular), Typeface.BOLD);
            }
            inputMs.setTextColor(Color.WHITE);
            inputMs.setHintTextColor(Color.LTGRAY);
        });
        inputMs.requestFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSummaries(); // refresh language/method/duration every time we return
    }
}

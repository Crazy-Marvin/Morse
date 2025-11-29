package rocks.poopjournal.morse;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class AboutActivity extends AppCompatActivity {

    ImageView marvin_email, marvin_github, marvin_mastodon, mubeen_github, fahad_github, fredrik_github, backIcon;
    LinearLayout translate, report, source, material_icons, android_jetpack;
    ConstraintLayout main;
    TextView version_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        main = findViewById(R.id.main);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewCompat.setOnApplyWindowInsetsListener(main, (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        marvin_email = findViewById(R.id.marvin_email);
        marvin_github = findViewById(R.id.marvin_github);
        marvin_mastodon = findViewById(R.id.marvin_gmail);
        mubeen_github = findViewById(R.id.mubeen_github);
        fahad_github = findViewById(R.id.fahad_github);
        fredrik_github = findViewById(R.id.fredrik_github);
        translate = findViewById(R.id.translate);
        report = findViewById(R.id.report);
        source = findViewById(R.id.source);
        material_icons = findViewById(R.id.material_icons);
        android_jetpack = findViewById(R.id.android_jetpack);
        version_number = findViewById(R.id.version_number);
        backIcon = findViewById(R.id.backIcon);
        version_number.setText(getAppVersion());

        // 🔗 Set click listeners
        setClickListeners();
    }

    private String getAppVersion() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "Version unknown";
        }
    }

    private void setClickListeners() {

        marvin_email.setOnClickListener(v -> openEmail("mailto:marvin@poopjournal.rocks?subject=Morse"));
        marvin_mastodon.setOnClickListener(v -> openLink("https://fosstodon.org/@CrazyMarvinApps"));

        marvin_github.setOnClickListener(v -> openLink("https://github.com/CrazyMarvin"));
        mubeen_github.setOnClickListener(v -> openLink("https://github.com/mubeen1519"));
        fahad_github.setOnClickListener(v -> openLink("https://github.com/FahadSaleem"));
        fredrik_github.setOnClickListener(v -> openLink("https://github.com/fejd"));

        translate.setOnClickListener(v -> openLink("https://hosted.weblate.org/engage/morse"));
        report.setOnClickListener(v -> openLink("https://github.com/Crazy-Marvin/Morse/issues"));
        source.setOnClickListener(v -> openLink("https://github.com/Crazy-Marvin/Morse"));
        material_icons.setOnClickListener(v -> openLink("https://fonts.google.com/icons"));
        android_jetpack.setOnClickListener(v -> openLink("https://developer.android.com/jetpack/"));

        backIcon.setOnClickListener(v -> finish());
    }

    private void openLink(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openEmail(String email) {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + email));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

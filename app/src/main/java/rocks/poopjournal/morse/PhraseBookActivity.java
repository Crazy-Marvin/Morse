package rocks.poopjournal.morse;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PhraseBookActivity extends AppCompatActivity {

    RecyclerView phrasebookRv;
    PhrasebookAdapter phrasebookAdapter;
    DBHelper helper;
    RelativeLayout mainContainer;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrasebook);
        mainContainer = findViewById(R.id.main_container);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // Apply insets padding to avoid notch / status bar / nav bar overlap
        ViewCompat.setOnApplyWindowInsetsListener(mainContainer, (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        helper = new DBHelper(getApplicationContext());

        back = findViewById(R.id.back);
        back.setOnClickListener(view -> finish());
        ArrayList<PhrasebookModel> arrayList = helper.getAllPhrases();


        phrasebookAdapter = new PhrasebookAdapter(this, helper);

        phrasebookRv = findViewById(R.id.rv_phrasebook);
        phrasebookRv.setHasFixedSize(true);// same
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(PhraseBookActivity.this);
        phrasebookRv.setLayoutManager(mLayoutManager);
        phrasebookRv.setAdapter(phrasebookAdapter);


        phrasebookAdapter.setPhrasebookList(arrayList);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(phrasebookAdapter));
        itemTouchHelper.attachToRecyclerView(phrasebookRv);

    }
}

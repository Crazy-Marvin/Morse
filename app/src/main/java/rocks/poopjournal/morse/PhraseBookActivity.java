package rocks.poopjournal.morse;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PhraseBookActivity extends AppCompatActivity {

    RecyclerView phrasebookRv;
    PhrasebookAdapter phrasebookAdapter;
    DBHelper helper;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrasebook);

        helper =new DBHelper(getApplicationContext());

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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

package rocks.poopjournal.morse;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PhrasebookAdapter extends RecyclerView.Adapter<PhrasebookAdapter.ClientViewHolder> {


    DBHelper helper;
    private Context context;
    private ArrayList<PhrasebookModel> list = new ArrayList<>();

    public PhrasebookAdapter(Context context, DBHelper helper) {
        this.context = context;
        this.helper = helper;
    }

    // Overrides
    @NonNull
    @Override
    public PhrasebookAdapter.ClientViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_list_phrasebook, parent, false);
        return new PhrasebookAdapter.ClientViewHolder(view);
    }


    public Context getContext() {
        return context;
    }

    @Override
    public void onBindViewHolder(@NonNull final PhrasebookAdapter.ClientViewHolder holder, final int position) {


        holder.text.setText(list.get(position).text);
        holder.morse.setText(list.get(position).morse);

    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }


    public void setPhrasebookList(ArrayList<PhrasebookModel> list) {
        if (list == null || list.size() == 0)
            return;

        this.list = list;
        notifyDataSetChanged();

    }

    public void deleteItem(int position) {
        Log.d("gotcalled", "true" + list.get(position).id);
        helper.deleteNote(list.get(position).id);
        list.remove(position);
        notifyItemRemoved(position);
        Toast.makeText(context, "Deleted entry", Toast.LENGTH_SHORT).show();
    }

    // View Holders
    public static class ClientViewHolder extends RecyclerView.ViewHolder {

        public TextView text, morse;
        public RelativeLayout containerRl;

        public ClientViewHolder(View itemView) {
            super(itemView);
            this.text = itemView.findViewById(R.id.text);
            this.morse = itemView.findViewById(R.id.morse);
            this.containerRl = itemView.findViewById(R.id.containerRl);
        }
    }

}

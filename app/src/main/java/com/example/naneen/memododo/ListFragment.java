package com.example.naneen.memododo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListFragment extends Fragment{
    View view;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    private FirebaseRecyclerAdapter<ViewSingleItem, ShowDataViewHolder> mFirebaseAdapter;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("Word_list");
        recyclerView = (RecyclerView) view.findViewById(R.id.viewPostData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Toast.makeText(getActivity(), "Please wait, it is loading...", Toast.LENGTH_SHORT).show();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        final ImageButton removeBtn;
        mFirebaseAdapter = new FirebaseRecyclerAdapter<ViewSingleItem, ShowDataViewHolder>(ViewSingleItem.class,
                R.layout.viewsingleitem, ShowDataViewHolder.class, myRef) {
            @Override
            protected void populateViewHolder(final ShowDataViewHolder viewHolder, ViewSingleItem model, final int position) {
                viewHolder.Meaning(model.getMeaning());
                viewHolder.Word(model.getWord());

                // click at any row of the list & we will delete at row out of DB
//                viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
                viewHolder.removeBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick (final View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Delete?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int selectedItem = position;
                                        mFirebaseAdapter.getRef(selectedItem).removeValue();
                                        mFirebaseAdapter.notifyItemRemoved(selectedItem);
                                        recyclerView.invalidate();
                                        onStart();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.setTitle("Are you sure?");
                        dialog.show();
                    }
                });
            }
        };
        boolean temp = mFirebaseAdapter == null;
        recyclerView.setAdapter(mFirebaseAdapter);
    }

    public static class ShowDataViewHolder extends RecyclerView.ViewHolder {
        private final TextView word, meaning;
        ImageButton removeBtn;
//        private final ImageView image_URL;

        public ShowDataViewHolder(final View itemView) {
            super(itemView);
            word = (TextView) itemView.findViewById(R.id.fetch_word);
            meaning  = (TextView) itemView.findViewById(R.id.fetch_meaning);
            removeBtn = (ImageButton) itemView.findViewById(R.id.removeBtn);
        }

        private void Word(String w) {
            word.setText(w);
        }

        private void Meaning(String m) {
            meaning.setText(m);
        }

        private ImageButton RemoveBtn() {
            return removeBtn;
        }

    }
}

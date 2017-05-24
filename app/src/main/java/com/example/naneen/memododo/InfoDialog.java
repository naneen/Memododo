package com.example.naneen.memododo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.R.attr.dialogLayout;

/**
 * Created by naneen on 5/24/2017 AD.
 */

public class InfoDialog extends DialogFragment {
    private View dialogLayout;
    private Dialog dialog;
    private String Word, Meaning, Image_URL;
    ImageView imageView;
    TextView wordView;
    TextView desView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    private FirebaseRecyclerAdapter<ViewSingleItem, ListFragment.ShowDataViewHolder> mFirebaseAdapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogLayout = inflater.inflate(R.layout.popup, null);
        builder.setView(dialogLayout);
        dialog = builder.create();

        imageView = (ImageView) dialogLayout.findViewById(R.id.image_Show);
        wordView = (TextView) dialogLayout.findViewById(R.id.word_show);
        desView = (TextView) dialogLayout.findViewById(R.id.des_show);

        Glide.with(getActivity().getApplicationContext())
                .load(Image_URL)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
        wordView.setText(Word);
        desView.setText(Meaning);

        return dialog;
    }

    public void setImage(String image_URL) {
        Image_URL = image_URL;
    }

    public void setWord(String word) {
        Word = word;
    }

    public void setMeaning(String meaning) {
        Meaning = meaning;
    }
}

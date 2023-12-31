package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.ims.RcsUceAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassStatsFragment extends Fragment {
    Character character;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_class_stats, container, false);

        Intent intent = getActivity().getIntent();
        int characterId = intent.getIntExtra("CharacterId", -1);
        if (characterId < 0) {
            getActivity().finish();
        }
        character = (Character) Character.getObject(Character.class, characterId);

        recyclerView = rootView.findViewById(R.id.classstats_recycler_body);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new ItemAdapter(character.Abilities, getContext());
        recyclerView.setAdapter(recyclerAdapter);


        DatabaseManager db = new DatabaseManager(getContext());
        ImageButton createButton = rootView.findViewById(R.id.classstats_button_create);
        createButton.setOnClickListener(v -> {
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.dialog_create_item);
            Objects.requireNonNull(dialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            AppCompatButton saveButton = dialog.findViewById(R.id.createdialog_button_save);
            saveButton.setOnClickListener(view -> {
                EditText header = dialog.findViewById(R.id.createdialog_editText_header);
                EditText body = dialog.findViewById(R.id.createdialog_edittext_body);

                Item item = new Item(header.getText().toString(), body.getText().toString());
                item.type = "Abilities";
                item.pk = db.setItem(character.pk, item.toJson(), "Abilities");
                character.Abilities.add(item);
                recyclerAdapter.notifyDataSetChanged();
                dialog.dismiss();
            });
            dialog.show();
        });

        TextView name = rootView.findViewById(R.id.textView5);
        name.setText(character.classArc.name);
        TextView level = rootView.findViewById(R.id.textView6);
        level.setText("Level " + character.level);

        Button levelUp = rootView.findViewById(R.id.button);
        levelUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                character.levelUp();
                character.save(getContext(), character);
                level.setText("Level " + character.level);
            }
        });

        return rootView;
    }
}
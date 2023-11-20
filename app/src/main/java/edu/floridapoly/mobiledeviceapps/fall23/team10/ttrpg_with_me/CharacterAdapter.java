package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.MyViewHolder> {
    List<Character> characterList;
    Context context;

    public CharacterAdapter(List<Character> characterList, Context context) {
        this.characterList = characterList;
        this.context = context;
    }

    @NonNull
    @Override
    public CharacterAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.character_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterAdapter.MyViewHolder holder, int position) {
        Character character = characterList.get(position);
        String name = character.getName();
        String desc = character.getDescription();
        String url = character.getImageURL();

        holder.char_name.setText(character.getName());
        holder.char_desc.setText(character.getDescription());

        Glide.with(this.context)
                .load(url)
                .placeholder(R.drawable.ic_loading_image)
                .into(holder.char_icon);

        // When card is clicked on send relevant information to the next activity
        holder.card.setOnClickListener(view -> {
            Intent intent = new Intent(context, NavigationActivity.class);
            intent.putExtra("CharacterName", name);
            intent.putExtra("CharacterDesc", desc);
            intent.putExtra("CharacterImage", url);
            context.startActivity(intent);
        });

        holder.delete_button.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "Deletes character!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView char_icon;
        TextView char_name;
        TextView char_desc;

        CardView card;
        ImageButton delete_button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            char_icon = itemView.findViewById(R.id.charselect_image_item);
            char_name = itemView.findViewById(R.id.charselect_text_itemname);
            char_desc = itemView.findViewById(R.id.charSelect_text_itemdesc);

            card = itemView.findViewById(R.id.charselect_card_item);
            delete_button = itemView.findViewById(R.id.charselect_button_delete);
        }
    }
}
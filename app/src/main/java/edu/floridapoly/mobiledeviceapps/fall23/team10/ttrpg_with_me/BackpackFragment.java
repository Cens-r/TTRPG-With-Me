package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BackpackFragment extends Fragment {
    static Executor executor = Executors.newSingleThreadExecutor();
    static Handler handler = new Handler(Looper.getMainLooper());

    String[] nameList = {"Weapons", "Spells", "Armor", "Items", "Extras"};
    List<ItemContainer> itemContainers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_backpack, container, false);
        itemContainers = new ArrayList<>();
        ViewGroup linearLayout = rootView.findViewById(R.id.backpack_linear_container);
        for (int index = 0; index < linearLayout.getChildCount(); index++) {
            ItemContainer item = new ItemContainer(linearLayout.getChildAt(index), nameList[index]);
            itemContainers.add(item);
        }
        return rootView;
    }

    public static class ItemContainer {
        boolean isExpanded;
        List<View> itemList;

        View container;
        TextView headerText;
        ImageButton generateButton;
        ImageButton arrowButton;

        public ItemContainer(View container, String name) {
            isExpanded = false;
            itemList = new ArrayList<>();

            this.container = container;
            headerText = container.findViewById(R.id.item_text_header);
            generateButton = container.findViewById(R.id.item_button_generate);
            arrowButton = container.findViewById(R.id.item_button_arrow);

            headerText.setText(name);
            generateButton.setOnClickListener(view -> {
                executor.execute(() -> {
                    Item item = Item.Generate(name, null);
                    handler.post(() -> {
                        if (item != null) {
                            View itemView = this.createItem(item.name, item.description);
                            itemList.add(itemView);
                        } else {
                            Toast.makeText(this.container.getContext(), "Couldn't create item!", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            });
            arrowButton.setOnClickListener(view -> {
                ViewGroup layout = container.findViewById(R.id.item_linear_body);
                for (int index = 0; index < layout.getChildCount(); index++) {
                    View element = layout.getChildAt(index);
                    element.setVisibility((isExpanded) ? View.GONE : View.VISIBLE);
                }
                arrowButton.setImageResource((isExpanded) ? R.drawable.ic_collapse_arrow : R.drawable.ic_expand_arrow);
                isExpanded = !isExpanded;
            });
        }

        View createItem(String name, String description) {
            ViewGroup layout = container.findViewById(R.id.item_linear_body);
            LayoutInflater inflater = LayoutInflater.from(layout.getContext());
            View view = inflater.inflate(R.layout.display_container, layout, false);

            TextView hText = view.findViewById(R.id.display_text_header);
            hText.setText(name);
            TextView bText = view.findViewById(R.id.display_text_body);
            bText.setText(description);

            if (!isExpanded) {
                view.setVisibility(View.GONE);
            }
            layout.addView(view);
            return view;
        }
    }
}
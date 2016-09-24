package dynamicGridActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import dynamicGrid.BaseDynamicGridAdapter;
import dynamicGrid.mapGenerator.map.MapDTO;
import karolakpochwala.apploweros.R;


public class CheeseDynamicAdapter extends BaseDynamicGridAdapter {
//    private ArrayList<MapDTO> allMaps;
    private MapDTO currentMap;

    public CheeseDynamicAdapter(Context context, List<?> items, MapDTO currentMap) {
        super(context, items, currentMap);
//        this.allMaps = allMaps;//TODO tymczasowo pierwsza mapa tylko
        this.currentMap = currentMap;//TODO tymczasowo pierwsza mapa tylko
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheeseViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_grid, null);
            holder = new CheeseViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (CheeseViewHolder) convertView.getTag();
        }
        holder.build(getItem(position).toString());
        return convertView;
    }

    private class CheeseViewHolder {
        private TextView titleText;
        private ImageView image;

        private CheeseViewHolder(View view) {
            titleText = (TextView) view.findViewById(R.id.item_title);
            image = (ImageView) view.findViewById(R.id.item_img);
        }

        void build(String title) {
            titleText.setText(title);
            image.setImageResource(R.drawable.ic_launcher);
        }
    }
}
package dynamicGridActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import Simulets.Simulet;
import dynamicGrid.BaseDynamicGridAdapter;
import dynamicGrid.mapGenerator.map.MapDTO;
import dynamicGrid.mapGenerator.map.PlaceInMapDTO;
import karolakpochwala.apploweros.R;


public class CheeseDynamicAdapter extends BaseDynamicGridAdapter {
    private MapDTO currentMap;
    private ArrayList<Simulet> listOfSimulets;
    private int currentSimuletIndex;

    public CheeseDynamicAdapter(Context context, ArrayList<Simulet> listOfSimulets, MapDTO currentMap) {
        super(context, currentMap);
        this.listOfSimulets = listOfSimulets;
        currentSimuletIndex = listOfSimulets.size() - 1;
//        this.allMaps = allMaps;//TODO tymczasowo pierwsza mapa tylko
        this.currentMap = currentMap;//TODO tymczasowo pierwsza mapa tylko
        bindPlacesInMapToSimulets(this.listOfSimulets, this.currentMap);
    }

    private void bindPlacesInMapToSimulets(ArrayList<Simulet> listOfSimulets, MapDTO currentMap) {
        int x = 0;
        for (Simulet simulet : listOfSimulets) {
            for (int index = x; index < currentMap.getPlacesInMap().size(); index++) {
                PlaceInMapDTO place = currentMap.getPlacesInMap().get(index);
                if (place.isDropAllowed() && !place.isItMap() && place.getSimulet() == null) {
                    place.setSimulet(simulet);
                    break;
                }
                x++;
            }
        }
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
        PlaceInMapDTO currentPlace = currentMap.getPlacesInMap().get(position);
        if (currentPlace.getSimulet() != null) {
            holder.buildPlaceForSimulet(Integer.toString(((PlaceInMapDTO) getItem(position)).getPlaceInMapId()),
                    currentPlace.getSimulet().getPictureOff());
        } else if (currentPlace.isDropAllowed() /*&& !currentMap.getPlacesInMap().get(position).isItMap()*/) {
            holder.build(Integer.toString(((PlaceInMapDTO) getItem(position)).getPlaceInMapId()));
        }
        return convertView;
    }

    private class CheeseViewHolder {
        private TextView titleText;
        private ImageView image;

        private CheeseViewHolder(View view) {
            titleText = (TextView) view.findViewById(R.id.item_title);
            image = (ImageView) view.findViewById(R.id.item_img);
        }

        void buildPlaceForSimulet(String title, int pictureOff) {
            titleText.setText(title);
            image.setImageResource(pictureOff);
        }

        void build(String title) {
            titleText.setText(title);
            image.setImageResource(R.drawable.ic_launcher);
        }

        void buildOnlyText(String title) {
            titleText.setText(title);
            image.setImageResource(R.drawable.ic_launcher);

        }
    }
}
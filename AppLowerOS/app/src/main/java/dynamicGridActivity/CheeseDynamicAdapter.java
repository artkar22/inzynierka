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
import TriggerSimulets.TriggerSimulet;
import dynamicGrid.BaseDynamicGridAdapter;
import dynamicGrid.mapGenerator.map.MapDTO;
import dynamicGrid.mapGenerator.map.PlaceInMapDTO;
import karolakpochwala.apploweros.R;


public class CheeseDynamicAdapter extends BaseDynamicGridAdapter {
    private MapDTO currentMap;
    private ArrayList<Simulet> listOfSimulets;
    private TriggerSimulet trigger;

    public CheeseDynamicAdapter(Context context, ArrayList<Simulet> listOfSimulets,
                                TriggerSimulet trigger, MapDTO currentMap, boolean bindSimulets) {
        super(context, currentMap, trigger);
        this.trigger = trigger;
//        createMapForEachTrigger(triggers, currentMap);
        this.listOfSimulets = listOfSimulets;
//        this.allMaps = allMaps;//TODO tymczasowo pierwsza mapa tylko
        this.currentMap = currentMap;//TODO tymczasowo pierwsza mapa tylko
//        bindPlacesInMapToSimulets(this.listOfSimulets, this.currentMap);
        if(bindSimulets){
            bindPlacesInMapToSimulets(this.listOfSimulets);
        }
    }

//    private void createMapForEachTrigger(ArrayList<TriggerSimulet> triggers, MapDTO currentMap) {
//        for (int x = 0; x < triggers.size(); x++) {
//            if (x != 0) {
//                triggers.get(x).deepCopyOfPlacesInMap(currentMap.getPlacesInMap());
//            } else {
//                triggers.get(x).setMyPlacesInMap(currentMap.getPlacesInMap());
//            }
//        }
//    }

//    private void bindPlacesInMapToSimulets(ArrayList<Simulet> listOfSimulets, MapDTO currentMap) {
//        int x = 0;
//        for (Simulet simulet : listOfSimulets) {
//            for (int index = x; index < currentMap.getPlacesInMap().size(); index++) {
//                PlaceInMapDTO place = currentMap.getPlacesInMap().get(index);
//                if (place.isDropAllowed() && !place.isItMap() && place.getSimulet() == null) {
//                    place.setSimulet(simulet);
//                    break;
//                }
//                x++;
//            }
//        }
//    }

    private void bindPlacesInMapToSimulets(ArrayList<Simulet> listOfSimulets) {
        int x = 0;
        for (Simulet simulet : listOfSimulets) {
            for (int index = x; index < trigger.getMyPlacesInMap().size(); index++) {
                PlaceInMapDTO place = trigger.getMyPlacesInMap().get(index);
                if (place.isDropAllowed() && !place.isItMap() && place.getSimulet() == null) {
                    place.setSimulet(simulet);
                    break;
                }
                x++;
            }
        }
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        CheeseViewHolder holder;
//        PlaceInMapDTO currentPlace = currentMap.getPlacesInMap().get(position);
//
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_grid, null);
//            if (currentPlace.getSimulet() != null) {
//                holder = new CheeseViewHolder(convertView, currentMap.getPlacesInMap().get(position).getSimulet());
//
//            } else {
//                holder = new CheeseViewHolder(convertView);
//
//            }
//            convertView.setTag(holder);
//        } else {
//            holder = (CheeseViewHolder) convertView.getTag();
//        }
//        if (currentPlace.getSimulet() != null) {
//            holder.buildPlaceForSimulet(Integer.toString(((PlaceInMapDTO) getItem(position)).getPlaceInMapId()),
//                    currentPlace.getSimulet().getPictureOff());
//        } else if (currentPlace.isDropAllowed() /*&& !currentMap.getPlacesInMap().get(position).isItMap()*/) {
//            holder.build(Integer.toString(((PlaceInMapDTO) getItem(position)).getPlaceInMapId()));
//        }
//        return convertView;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheeseViewHolder holder;
        PlaceInMapDTO currentPlace;
        if(trigger != null){
            currentPlace = trigger.getMyPlacesInMap().get(position);
        }else{
            currentPlace = currentMap.getPlacesInMap().get(position);
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_grid, null);
            if (currentPlace.getSimulet() != null) {
                holder = new CheeseViewHolder(convertView, currentPlace.getSimulet());

            } else {
                holder = new CheeseViewHolder(convertView);

            }
            convertView.setTag(holder);
        } else {
            holder = (CheeseViewHolder) convertView.getTag();
        }
        if (currentPlace.getSimulet() != null) {
            holder.buildPlaceForSimulet(Integer.toString(((PlaceInMapDTO) getItem(position)).getPlaceInMapId()),
                    currentPlace.getSimulet().getPictureOff());
        } else if (currentPlace.isDropAllowed() /*&& !currentMap.getPlacesInMap().get(position).isItMap()*/) {
            holder.build(Integer.toString(((PlaceInMapDTO) getItem(position)).getPlaceInMapId()));
        }
        return convertView;
    }

    private class CheeseViewHolder {
        private ImageView image;
        private final Simulet simulet;

        private CheeseViewHolder(View view, Simulet simulet) {
            this.simulet = simulet;
            image = (ImageView) view.findViewById(R.id.item_img);
        }

        public CheeseViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.item_img);
            simulet = null;
        }

        void buildPlaceForSimulet(String title, int pictureOff) {
            image.setImageResource(getPictureForSimulet());
        }

        void build(String title) {
            image.setImageResource(R.drawable.ic_launcher);
        }

        void buildOnlyText(String title) {
            image.setImageResource(R.drawable.ic_launcher);

        }

        private int getPictureForSimulet() {
            if (simulet.isSimuletOn()) {
                if (simulet.getOptionsStatus().isTimer() && !simulet.getOptionsStatus().isForLoop()) { //timer bez loop
                    return simulet.getPictureNameOnTimer();
                } else if (simulet.getOptionsStatus().isTimer() && simulet.getOptionsStatus().isForLoop()) { // timer i loop
                    return simulet.getPictureNameOnPetlaTimer();
                } else if (!simulet.getOptionsStatus().isTimer() && simulet.getOptionsStatus().isForLoop()) { //loop bez timer
                    return simulet.getPictureNameOnPetla();
                } else if (simulet.getOptionsStatus().isTimer() && simulet.getOptionsStatus().isForLoop()) {//bez opcji
                    return simulet.getPictureOn();
                }
            } else { //gdy simulet wyłączony
                if (simulet.getOptionsStatus().isTimer() && !simulet.getOptionsStatus().isForLoop()) { //timer bez loop
                    return simulet.getPictureNameOffTimer();
                } else if (simulet.getOptionsStatus().isTimer() && simulet.getOptionsStatus().isForLoop()) { // timer i loop
                    return simulet.getPictureNameOffPetlaTimer();
                } else if (!simulet.getOptionsStatus().isTimer() && simulet.getOptionsStatus().isForLoop()) { //loop bez timer
                    return simulet.getPictureNameOffPetla();
                } else if (!simulet.getOptionsStatus().isTimer() && !simulet.getOptionsStatus().isForLoop()) {//bez opcji
                    return simulet.getPictureOff();
                }
            }
            return simulet.getPictureOff();
        }
    }
}
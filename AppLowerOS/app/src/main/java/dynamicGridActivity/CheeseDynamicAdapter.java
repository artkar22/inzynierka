package dynamicGridActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.util.ArrayList;

import Simulets.Simulet;
import Simulets.SimuletsState;
import TriggerSimulets.TriggerSimulet;
import dynamicGrid.BaseDynamicGridAdapter;
import dynamicGrid.mapGenerator.map.MapDTO;
import dynamicGrid.mapGenerator.map.MapDTOBuilder;
import dynamicGrid.mapGenerator.map.PlaceInMapDTO;
import karolakpochwala.apploweros.R;

import static dynamicGrid.DynamicGridUtils.PAUSE_SIMULET;


public class CheeseDynamicAdapter extends BaseDynamicGridAdapter {
    private MapDTO currentMap;
    private ArrayList<Simulet> listOfSimulets;
    private ArrayList<TriggerSimulet> listOfTriggers;
    private Context context;

    public CheeseDynamicAdapter(Context context, ArrayList<Simulet> listOfSimulets,
                                ArrayList<TriggerSimulet> triggers, MapDTO currentMap, boolean bindSimulets) {
        super(context, currentMap, listOfSimulets, triggers);
        this.context = context;
        this.listOfTriggers = triggers;
        this.listOfSimulets = listOfSimulets;
//        this.allMaps = allMaps;//TODO tymczasowo pierwsza mapa tylko
        this.currentMap = currentMap;//TODO tymczasowo pierwsza mapa tylko
        bindPlacesInMapToTriggers(this.listOfTriggers, this.currentMap);
        bindPlacesInMapToSimulets(this.listOfSimulets, this.currentMap, this.listOfTriggers.size());
        bindPlaceInMapToPauseSimulet(this.currentMap);
    }

    private void bindPlaceInMapToPauseSimulet(MapDTO currentMap) {
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.timer_nasycony);
        Bitmap hm = BitmapFactory.decodeResource(context.getResources(), R.drawable.timer_highlight);
        currentMap.getPlacesInMap().get(6).setSimuletState(new SimuletsState(PAUSE_SIMULET, bm, hm, null));
    }

    private void bindPlacesInMapToTriggers(ArrayList<TriggerSimulet> listOfTriggers, MapDTO currentMap) {
        for (int x = 0; x < listOfTriggers.size(); x++) {
            final TriggerSimulet currentSim = listOfTriggers.get(x);
            for (int y = 0; y < currentSim.getStates().size(); y++) {
                final PlaceInMapDTO place = currentMap.getPlacesInMap().get(x + (y * currentMap.getNumberOfColums()));
                if (place.getTypeOfPlace().equals(MapDTOBuilder.CONTAINER) && place.getSimuletState() == null) {
                    place.setSimuletState(currentSim.getStates().get(y));
                }
            }
        }
    }

    private void bindPlacesInMapToSimulets(ArrayList<Simulet> listOfSimulets, MapDTO currentMap, int numberOfTriggers) {
        for (int x = 0; x < listOfSimulets.size(); x++) {
            final Simulet currentSim = listOfSimulets.get(x);
            for (int y = 0; y < currentSim.getStates().size(); y++) {
                final PlaceInMapDTO place = currentMap.getPlacesInMap().get(numberOfTriggers + x + (y * currentMap.getNumberOfColums()));
                if (place.getTypeOfPlace().equals(MapDTOBuilder.CONTAINER) && place.getSimuletState() == null) {
                    place.setSimuletState(currentSim.getStates().get(y));
                }
            }
        }
    }

//    private void bindPlacesInMapToSimulets(ArrayList<Simulet> listOfSimulets) {
//        int x = 0;
//        for (Simulet simulet : listOfSimulets) {
//            for (int index = x; index < trigger.getMyPlacesInMap().size(); index++) {
//                PlaceInMapDTO place = trigger.getMyPlacesInMap().get(index);
//                if (place.isDropAllowed() && !place.isItMap() && place.getSimulet() == null) {
//                    place.setSimulet(simulet);
//                    break;
//                }
//                x++;
//            }
//        }
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheeseViewHolder holder;
        PlaceInMapDTO currentPlace = currentMap.getPlacesInMap().get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_grid, null);
            if (currentPlace.getSimuletState() != null) {
                holder = new CheeseViewHolder(convertView, currentMap.getPlacesInMap().get(position).getSimuletState());

            } else {
                holder = new CheeseViewHolder(convertView);

            }
            convertView.setTag(holder);
        } else {
            holder = (CheeseViewHolder) convertView.getTag();
            if (currentPlace.getSimuletState() != null) {
                holder.simuletState = currentPlace.getSimuletState();
            }
        }
        if (currentPlace.getSimuletState() != null) {
            holder.buildPlaceForSimulet();
        } else if (currentMap.getPlacesInMap().get(position).getTypeOfPlace().equals(MapDTOBuilder.PAUSE_SIMULET_PLACE)) {
            holder.buildPauseSimulet(position);
        } else if (currentMap.getPlacesInMap().get(position).getTypeOfPlace().equals(MapDTOBuilder.SIMULET_PLACE)) {
            holder.build(Integer.toString(((PlaceInMapDTO) getItem(position)).getPlaceInMapId()));
        } else if (currentMap.getPlacesInMap().get(position).getTypeOfPlace().equals(MapDTOBuilder.TRIGGER_PLACE)) {
            holder.buildTriggerPlace(Integer.toString(((PlaceInMapDTO) getItem(position)).getPlaceInMapId()));
        } else if (currentMap.getPlacesInMap().get(position).getTypeOfPlace().equals(MapDTOBuilder.SPACE_BEETWEEN)) {
            holder.buildSpaceBeetween(position);
        }  else if (currentMap.getPlacesInMap().get(position).getTypeOfPlace().equals(MapDTOBuilder.ARROW_PLACE)) {
            holder.buildArrow(Integer.toString(((PlaceInMapDTO) getItem(position)).getPlaceInMapId()));
        }
        return convertView;
    }


    private class CheeseViewHolder {
        private ImageView image;
        private SimuletsState simuletState;

        private CheeseViewHolder(View view, SimuletsState simuletState) {
            this.simuletState = simuletState;
            image = (ImageView) view.findViewById(R.id.item_img);
        }

        public CheeseViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.item_img);
            simuletState = null;
        }

        void buildPlaceForSimulet() {
            if (simuletState.getMiniature() != null)
                image.setImageBitmap(simuletState.getMiniature());
        }

        void build(String title) {
            image.setImageResource(R.drawable.ic_launcher);
        }

        void buildTriggerPlace(String title) {
            image.setImageResource(R.drawable.ic_square_launcher);
        }

        void buildArrow(String title) {
            image.setImageResource(R.drawable.ic_arrow_launcher);
        }
         void buildPauseSimulet(int position){
             if (position == 6) {
                 image.setImageResource(R.drawable.timer_nasycony);
             }
         }
        void buildSpaceBeetween(int position) {
            if (position == 15) {
                image.setImageResource(R.drawable.ic_finger_0);
                return;
            }
            if (position == 16) {
                image.setImageResource(R.drawable.ic_finger_1);
                return;
            }
            if (position == 17) {
                image.setImageResource(R.drawable.ic_finger_2);
                return;
            }
            if (position == 18) {
                image.setImageResource(R.drawable.ic_finger_3);
                return;
            }
            if (position == 19) {
                image.setImageResource(R.drawable.ic_finger_4);
                return;
            }
        }

        void buildOnlyText(String title) {
            image.setImageResource(R.drawable.ic_launcher);

        }

    }
}
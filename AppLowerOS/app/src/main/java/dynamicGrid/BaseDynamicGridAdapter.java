package dynamicGrid;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedList;

import Simulets.ActionSimulet;
import TriggerSimulets.EventSimulet;
import dynamicGrid.mapGenerator.map.MapDTO;
import dynamicGrid.mapGenerator.map.PlaceInMapDTO;

public abstract class BaseDynamicGridAdapter extends AbstractDynamicGridAdapter {
    private Context mContext;

    private LinkedList<PlaceInMapDTO> mItems = new LinkedList<PlaceInMapDTO>();
    private MapDTO currentMap;
    private ArrayList<ActionSimulet> listOfActionSimulets;
    private ArrayList<EventSimulet> listOfTriggers;
    public BaseDynamicGridAdapter(Context context, MapDTO currentMap,
                                  ArrayList<ActionSimulet> listOfActionSimulets, ArrayList<EventSimulet> triggers) {
        mContext = context;
        this.currentMap = currentMap;
        this.listOfActionSimulets = listOfActionSimulets;
        this.listOfTriggers = triggers;
        init(currentMap.getPlacesInMap());
    }

    private void init(LinkedList<PlaceInMapDTO> items) {
        addAllStableId(items);
        this.mItems = items;
    }

    public void setItems(LinkedList<PlaceInMapDTO> items) {
        clear();
        addAllStableId(items);
        this.mItems = items;
        notifyDataSetChanged();
    }


    public void set(LinkedList<PlaceInMapDTO> items) {
        clear();
        init(items);
        notifyDataSetChanged();
    }

    public void clear() {
        clearStableIdMap();
        mItems.clear();
        notifyDataSetChanged();
    }


//    public void remove(Object item) {
//        mItems.remove(item);
//        removeStableID(item);
//        notifyDataSetChanged();
//    }


    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public MapDTO getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(final MapDTO currentMap) {
        this.currentMap = currentMap;
        notifyDataSetChanged();
    }

    @Override
    public void reorderItems(int originalPosition, int newPosition) {
        if (newPosition < getCount()) {
//            DynamicGridUtils.reorder(mItems, originalPosition, newPosition);
            DynamicGridUtils.swap(mItems, currentMap, listOfActionSimulets, listOfTriggers, originalPosition, newPosition);
            notifyDataSetChanged();
        }
    }

    @Override
    public boolean canReorder(int position) {
        return true;
    }


    protected Context getContext() {
        return mContext;
    }
}

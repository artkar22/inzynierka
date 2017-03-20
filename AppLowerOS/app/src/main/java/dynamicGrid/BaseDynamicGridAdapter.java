package dynamicGrid;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Simulets.Simulet;
import TriggerSimulets.TriggerSimulet;
import dynamicGrid.mapGenerator.map.MapDTO;
import dynamicGrid.mapGenerator.map.PlaceInMapDTO;

/**
 * Author: alex askerov
 * Date: 9/7/13
 * Time: 10:49 PM
 */
public abstract class BaseDynamicGridAdapter extends AbstractDynamicGridAdapter {
    private Context mContext;

    private LinkedList<PlaceInMapDTO> mItems = new LinkedList<PlaceInMapDTO>();
    private MapDTO currentMap;
    private ArrayList<Simulet> listOfSimulets;
    private ArrayList<TriggerSimulet> listOfTriggers;
    public BaseDynamicGridAdapter(Context context, MapDTO currentMap,
                                  ArrayList<Simulet> listOfSimulets, ArrayList<TriggerSimulet> triggers) {
        mContext = context;
        this.currentMap = currentMap;
        this.listOfSimulets = listOfSimulets;
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
            DynamicGridUtils.swap(mItems, currentMap, listOfSimulets, listOfTriggers, originalPosition, newPosition);
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

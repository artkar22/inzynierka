package dynamicGrid;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import dynamicGrid.mapGenerator.map.MapDTO;
import dynamicGrid.mapGenerator.map.PlaceInMapDTO;

/**
 * Author: alex askerov
 * Date: 9/7/13
 * Time: 10:49 PM
 */
public abstract class BaseDynamicGridAdapter extends AbstractDynamicGridAdapter {
    private Context mContext;

    private ArrayList<PlaceInMapDTO> mItems = new ArrayList<PlaceInMapDTO>();
    private MapDTO currentMap;

    protected BaseDynamicGridAdapter(Context context, MapDTO currentMap) {
        this.mContext = context;
        this.currentMap = currentMap;
    }

    public BaseDynamicGridAdapter(Context context, List<?> items, MapDTO currentMap) {
        mContext = context;
        this.currentMap = currentMap;
        init(currentMap.getPlacesInMap());
    }

    private void init(ArrayList<PlaceInMapDTO> items) {
        addAllStableId(items);
        this.mItems = items;
    }


    public void set(ArrayList<PlaceInMapDTO> items) {
        clear();
        init(items);
        notifyDataSetChanged();
    }

    public void clear() {
        clearStableIdMap();
        mItems.clear();
        notifyDataSetChanged();
    }

//    public void add(Object item) {
//        addStableId(item);
//        mItems.add(item);
//        notifyDataSetChanged();
//    }
//
//    public void add(int position, Object item) {
//        addStableId(item);
//        mItems.add(position, item);
//        notifyDataSetChanged();
//    }
//
//    public void add(List<?> items) {
//        addAllStableId(items);
//        this.mItems.addAll(items);
//        notifyDataSetChanged();
//    }


    public void remove(Object item) {
        mItems.remove(item);
        removeStableID(item);
        notifyDataSetChanged();
    }


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
            DynamicGridUtils.reorder(mItems, originalPosition, newPosition);
            notifyDataSetChanged();
        }
    }

    @Override
    public boolean canReorder(int position) {
        return true;
    }

//    public List<Object> getItems() {
//        return mItems;
//    }

    protected Context getContext() {
        return mContext;
    }
}

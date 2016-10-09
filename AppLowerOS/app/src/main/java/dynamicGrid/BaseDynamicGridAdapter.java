package dynamicGrid;

import android.content.Context;

import java.util.LinkedList;
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

    private LinkedList<PlaceInMapDTO> mItems = new LinkedList<PlaceInMapDTO>();
    private MapDTO currentMap;

    public BaseDynamicGridAdapter(Context context, MapDTO currentMap) {
        mContext = context;
        this.currentMap = currentMap;
        init(currentMap.getPlacesInMap());
    }

    private void init(LinkedList<PlaceInMapDTO> items) {
        addAllStableId(items);
        this.mItems = items;
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
//            DynamicGridUtils.reorder(mItems, originalPosition, newPosition);
            DynamicGridUtils.swap(mItems, originalPosition, newPosition);
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

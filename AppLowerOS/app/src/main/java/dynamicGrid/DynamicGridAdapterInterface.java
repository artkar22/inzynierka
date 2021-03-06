package dynamicGrid;

/**
 * Author: alex askerov
 * Date: 18/07/14
 * Time: 23:44
 */

import java.util.LinkedList;

import dynamicGrid.mapGenerator.map.MapDTO;
import dynamicGrid.mapGenerator.map.PlaceInMapDTO;

/**
 * Any adapter used with DynamicGridView must implement DynamicGridAdapterInterface.
 * Adapter implementation also must has stable items id.
 * See {AbstractDynamicGridAdapter} for stable id implementation example.
 */

public interface DynamicGridAdapterInterface {

    /**
     * Determines how to reorder items dragged from <code>originalPosition</code> to <code>newPosition</code>
     */
    void reorderItems(int originalPosition, int newPosition);

    /**
     * @return return columns number for GridView. Need for compatibility
     * (@link android.widget.GridView#getNumColumns() requires api 11)
     */
    MapDTO getCurrentMap();

    /**
     * Determines whether the item in the specified <code>position</code> can be reordered.
     */
    boolean canReorder(int position);

    void setItems(LinkedList<PlaceInMapDTO> items);
}

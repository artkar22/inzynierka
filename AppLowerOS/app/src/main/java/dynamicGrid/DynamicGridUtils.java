package dynamicGrid;

import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import dynamicGrid.mapGenerator.map.PlaceInMapDTO;

/**
 * Author: alex askerov
 * Date: 9/7/13
 * Time: 10:14 PM
 */
public class DynamicGridUtils {
    /**
     * Delete item in <code>list</code> from position <code>indexFrom</code> and insert it to <code>indexTwo</code>
     *  @param list
     * @param indexFrom
     * @param indexTwo
     */
    public static void reorder(LinkedList<PlaceInMapDTO> list, int indexFrom, int indexTwo) {
//        if (list.get(indexTwo).isDropAllowed()) {
        PlaceInMapDTO obj = list.remove(indexFrom);
        list.add(indexTwo, obj);
//        }
    }

    /**
     * Swap item in <code>list</code> at position <code>firstIndex</code> with item at position <code>secondIndex</code>
     *
     * @param list        The list in which to swap the items.
     * @param firstIndex  The position of the first item in the list.
     * @param secondIndex The position of the second item in the list.
     */
    public static void swap(LinkedList<PlaceInMapDTO> list, int firstIndex, int secondIndex) {
        if (list.get(secondIndex).isDropAllowed() && list.get(firstIndex).isDropAllowed()) {
//            PlaceInMapDTO firstObject = list.get(firstIndex);
            PlaceInMapDTO secondObject = list.get(secondIndex);
//            list.set(firstIndex, secondObject);
            list.set(secondIndex, list.set(firstIndex, secondObject));
        }

    }

    public static float getViewX(View view) {
        return Math.abs((view.getRight() - view.getLeft()) / 2);
    }

    public static float getViewY(View view) {
        return Math.abs((view.getBottom() - view.getTop()) / 2);
    }
}

package dynamicGrid;

import java.util.LinkedList;

import dynamicGrid.mapGenerator.map.MapDTO;
import dynamicGrid.mapGenerator.map.PlaceInMapDTO;

public interface DynamicGridAdapterInterface {

    void reorderItems(int originalPosition, int newPosition);

    MapDTO getCurrentMap();

    boolean canReorder(int position);

    void setItems(LinkedList<PlaceInMapDTO> items);
}

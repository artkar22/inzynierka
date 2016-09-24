package ApplicationData;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import Simulets.Simulet;
import dynamicGrid.mapGenerator.map.MapDTO;

/**
 * Created by ArturK on 2016-09-24.
 */
public class ApplicationData implements Parcelable {

    private ArrayList<MapDTO> allMaps;
    private ArrayList<Simulet> simulets;


    public ApplicationData() {
        allMaps = new ArrayList<>();
        simulets = new ArrayList<>();
    }

    public void addMap(final MapDTO newMap) {
        allMaps.add(newMap);
    }

    public ArrayList<MapDTO> getAllMaps() {
        return allMaps;
    }

    public ArrayList<Simulet> getSimulets() {
        return simulets;
    }

    public void addSimulet(final Simulet simulet) { //TODO dodawanie simuletów powinno być tą metodą, zmiana na pozniej
        this.simulets.add(simulet);
    }

    public void removeSimulet(final String simuletId) {
        //TODO gdy utrace łączność usuwam simulet
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}

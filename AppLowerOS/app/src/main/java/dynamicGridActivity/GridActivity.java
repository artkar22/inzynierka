package dynamicGridActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import ApplicationData.ApplicationData;
import dynamicGrid.DynamicGridView;
import dynamicGrid.mapGenerator.MapGenerator;
import dynamicGrid.mapGenerator.map.MapDTO;
import karolakpochwala.apploweros.R;
import mainUtils.Consts;

public class GridActivity extends Activity {

    private static final String TAG = GridActivity.class.getName();

    private DynamicGridView gridView;
    private ApplicationData applicationData;
    private MapGenerator mapGenerator;
    private Gson gSON;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        //zmiana
        gSON = new Gson();
        String ApplicationDataJSON = getIntent().getStringExtra(Consts.APPLICATION_DATA);
        applicationData = gSON.fromJson(ApplicationDataJSON, ApplicationData.class);
        applicationData.addMap(mapGenerator.loadMap(getAssets(), "map0.json"));//TODO SYSTEM WCZYTYWANIA MAP - POBIERANIE NAZW MAP Z KATALOGU I SYSTEM WCZYTYWANIA KOLEJNYCH
        //zmiana
        gridView = (DynamicGridView) findViewById(R.id.dynamic_grid);
        gridView.setNumColumns(applicationData.getAllMaps().get(0).getNumberOfColums());
        gridView.setAdapter(new CheeseDynamicAdapter(this,
                new ArrayList<String>(Arrays.asList(Cheeses.sCheeseStrings)),
                applicationData.getAllMaps().get(0))); //TODO TYLKO PIERWSZA MAPA NA RAZIE
//        add callback to stop edit mode if needed
//        gridView.setOnDropListener(new DynamicGridView.OnDropListener()
//        {
//            @Override
//            public void onActionDrop()
//            {
//                gridView.stopEditMode();
//            }
//        });
        gridView.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
                Log.d(TAG, "drag started at position " + position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {
                Log.d(TAG, String.format("drag item position changed from %d to %d", oldPosition, newPosition));
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                gridView.startEditMode(position);
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GridActivity.this, parent.getAdapter().getItem(position).toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (gridView.isEditMode()) {
            gridView.stopEditMode();
        } else {
            super.onBackPressed();
        }
    }
}

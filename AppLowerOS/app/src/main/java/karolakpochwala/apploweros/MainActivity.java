package karolakpochwala.apploweros;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.gson.Gson;

import ApplicationData.ApplicationData;
import Simulets.Simulet;
import coapClient.CoapClientThread;
import dynamicGridActivity.GridActivity;
import mainUtils.Consts;

public class MainActivity extends AppCompatActivity {
    private static final int BACK_TO_MAIN_MENU_AND_TRIGGER_SIMULET_SEARCHING = 0;
    private ApplicationData applicationData;
    private Thread coapClient;
    private CoapClientThread coapRunnable;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        applicationData = new ApplicationData();
        sendButton = (Button) findViewById(R.id.button1);
        coapRunnable = new CoapClientThread(sendButton, applicationData.getSimulets(),
                this);
        coapClient = new Thread(coapRunnable);
        coapClient.start();

        Button newGameButton = (Button) findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newGame = new Intent();
                Gson gS = new Gson();
                String appDataGson = gS.toJson(applicationData);
                newGame.putExtra(Consts.APPLICATION_DATA, appDataGson);
                newGame.setClass(MainActivity.this, GridActivity.class);
                startActivityForResult(newGame, BACK_TO_MAIN_MENU_AND_TRIGGER_SIMULET_SEARCHING);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BACK_TO_MAIN_MENU_AND_TRIGGER_SIMULET_SEARCHING) {
//            applicationData.removeAllSimulets(); TODO wyszukiwanie po wstecz
////            coapRunnable.runDiscovering();
////            coapClient.run();
////            Thread.State state = coapClient.getState();
////            state.name();
//            coapClient.notify();
//            coapRunnable = new CoapClientThread(sendButton, applicationData.getSimulets(),
//                    this);
//            coapClient = new Thread(coapRunnable);
//            coapClient.start();
        }
    }
}

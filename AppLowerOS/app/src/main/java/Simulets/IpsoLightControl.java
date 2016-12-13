package Simulets;

import java.net.URI;

import karolakpochwala.apploweros.R;

/**
 * Created by ArturK on 2016-05-17.
 */
public class IpsoLightControl extends Simulet {
    private static final int PICTURE_NAME_OFF = R.drawable.zarowka_off;
    private static final int PICTURE_NAME_OFF_PETLA = R.drawable.zarowka_off_petla;
    private static final int PICTURE_NAME_OFF_TIMER = R.drawable.zarowka_off_timer;
    private static final int PICTURE_NAME_OFF_PETLA_TIMER = R.drawable.zarowka_off_timer_petla;

    private static final int PICTURE_NAME_ON = R.drawable.zarowka_on;
    private static final int PICTURE_NAME_ON_PETLA = R.drawable.zarowka_on_petla;
    private static final int PICTURE_NAME_ON_TIMER = R.drawable.zarowka_on_timer;
    private static final int PICTURE_NAME_ON_PETLA_TIMER = R.drawable.zarowka_on_petla_timer;

    public IpsoLightControl(URI simuletsURI) {
        super(simuletsURI);
        setPictures(PICTURE_NAME_OFF,
                PICTURE_NAME_OFF_PETLA,
                PICTURE_NAME_OFF_TIMER,
                PICTURE_NAME_OFF_PETLA_TIMER,
                PICTURE_NAME_ON,
                PICTURE_NAME_ON_PETLA,
                PICTURE_NAME_ON_TIMER,
                PICTURE_NAME_ON_PETLA_TIMER);
    }
}

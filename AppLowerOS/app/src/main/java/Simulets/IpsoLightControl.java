package Simulets;

import java.net.URI;

import karolakpochwala.apploweros.R;

/**
 * Created by ArturK on 2016-05-17.
 */
public class IpsoLightControl extends Simulet {
    private static final int PICTURE_NAME_OFF = R.drawable.zarowka_off;
    private static final int PICTURE_NAME_ON = R.drawable.zarowka_on;

    public IpsoLightControl(URI simuletsURI) {
        super(simuletsURI);
        setPictures(PICTURE_NAME_OFF, PICTURE_NAME_ON);
    }
}

package Simulets;

import java.net.URI;

import karolakpochwala.apploweros.R;

/**
 * Created by ArturK on 2016-05-17.
 */
public class IpsoDigitalOutput extends Simulet {

    public IpsoDigitalOutput(URI simuletsURI) {
        super(simuletsURI);
        setPictures(UriToPicture.choosePicture(simuletsURI.toString()), UriToPicture.choosePicture(simuletsURI.toString()));
    }
}

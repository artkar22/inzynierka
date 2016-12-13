package Simulets;

import java.net.URI;

import karolakpochwala.apploweros.R;
import mainUtils.Consts;

/**
 * Created by ArturK on 2016-05-17.
 */
public class IpsoDigitalOutput extends Simulet {

    public IpsoDigitalOutput(URI simuletsURI) {
        super(simuletsURI);
        setPictures(UriToPicture.choosePicture(simuletsURI.toString(), Consts.PICTURE_NAME_OFF),
                UriToPicture.choosePicture(simuletsURI.toString(), Consts.PICTURE_NAME_OFF_PETLA),
                UriToPicture.choosePicture(simuletsURI.toString(), Consts.PICTURE_NAME_OFF_TIMER),
                UriToPicture.choosePicture(simuletsURI.toString(), Consts.PICTURE_NAME_OFF_PETLA_TIMER),
                UriToPicture.choosePicture(simuletsURI.toString(), Consts.PICTURE_NAME_ON),
                UriToPicture.choosePicture(simuletsURI.toString(), Consts.PICTURE_NAME_ON_PETLA),
                UriToPicture.choosePicture(simuletsURI.toString(), Consts.PICTURE_NAME_ON_TIMER),
                UriToPicture.choosePicture(simuletsURI.toString(), Consts.PICTURE_NAME_ON_PETLA_TIMER));
    }
}

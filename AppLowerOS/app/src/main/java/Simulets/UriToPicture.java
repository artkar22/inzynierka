package Simulets;

import karolakpochwala.apploweros.R;

/**
 * Created by ArturK on 2016-10-16.
 */
public class UriToPicture {
    private static String wiatraczekSuffix = "11112";
    private static String radioSuffix = "11113";
    private static String samochodSuffix = "11114";

    public static int choosePicture(String uri){
        if(uri.endsWith(wiatraczekSuffix)){
            return R.drawable.wiatraczek_off;
        }else if(uri.endsWith(samochodSuffix)){
            return R.drawable.samochod_off ;
        } else if(uri.endsWith(radioSuffix)){
            return R.drawable.radio_off ;
        }
        return R.drawable.ic_launcher;
    }
}

package Simulets;

import karolakpochwala.apploweros.R;
import mainUtils.Consts;

/**
 * Created by ArturK on 2016-10-16.
 */
public class UriToPicture {
    private static String wiatraczekSuffix = "11112";
    private static String radioSuffix = "11113";
    private static String samochodSuffix = "11114";

    public static int choosePicture(final String uri, final String whatToSet) {
        if (uri.endsWith(wiatraczekSuffix)) {
            if (whatToSet.equals(Consts.PICTURE_NAME_OFF)) {
                return R.drawable.wiatraczek_off;
            } else if (whatToSet.equals(Consts.PICTURE_NAME_OFF_PETLA)) {
                return R.drawable.wiatrak_off_petla;
            } else if (whatToSet.equals(Consts.PICTURE_NAME_OFF_TIMER)) {
                return R.drawable.wiatrak_off_timer;
            } else if (whatToSet.equals(Consts.PICTURE_NAME_OFF_PETLA_TIMER)) {
                return R.drawable.wiatrak_off_timer_petla;
            } else if (whatToSet.equals(Consts.PICTURE_NAME_ON)) {
                return R.drawable.wiatrak_on;
            } else if (whatToSet.equals(Consts.PICTURE_NAME_ON_PETLA)) {
                return R.drawable.wiatrak_on_petla;
            } else if (whatToSet.equals(Consts.PICTURE_NAME_ON_TIMER)) {
                return R.drawable.wiatrak_on_timer;
            } else if (whatToSet.equals(Consts.PICTURE_NAME_ON_PETLA_TIMER)) {
                return R.drawable.wiatrak_on_timer_petla;
            }
        } else if (uri.endsWith(samochodSuffix)) {
            if (whatToSet.equals(Consts.PICTURE_NAME_OFF)) {
                return R.drawable.samochod_off;
            } else if (whatToSet.equals(Consts.PICTURE_NAME_OFF_PETLA)) {
                return R.drawable.samochod_off_petla;
            } else if (whatToSet.equals(Consts.PICTURE_NAME_OFF_TIMER)) {
                return R.drawable.samochod_off_timer;
            } else if (whatToSet.equals(Consts.PICTURE_NAME_OFF_PETLA_TIMER)) {
                return R.drawable.samochod_off_timer_petla;
            } else if (whatToSet.equals(Consts.PICTURE_NAME_ON)) {
                return R.drawable.samochod_on;//TODO
            } else if (whatToSet.equals(Consts.PICTURE_NAME_ON_PETLA)) {
                return R.drawable.samochod_on_petla;
            } else if (whatToSet.equals(Consts.PICTURE_NAME_ON_TIMER)) {
                return R.drawable.samochod_on_timer;
            } else if (whatToSet.equals(Consts.PICTURE_NAME_ON_PETLA_TIMER)) {
                return R.drawable.samochod_on_timer_petla;
            }
        } else if (uri.endsWith(radioSuffix)) {
            if (whatToSet.equals(Consts.PICTURE_NAME_OFF)) {
                return R.drawable.radio_off;
            } else if (whatToSet.equals(Consts.PICTURE_NAME_OFF_PETLA)) {
                return R.drawable.radio_off_petla;
            } else if (whatToSet.equals(Consts.PICTURE_NAME_OFF_TIMER)) {
                return R.drawable.radio_off_timer;
            } else if (whatToSet.equals(Consts.PICTURE_NAME_OFF_PETLA_TIMER)) {
                return R.drawable.radio_off_timer_petla;
            } else if (whatToSet.equals(Consts.PICTURE_NAME_ON)) {
                return R.drawable.radio_on;//TODO
            } else if (whatToSet.equals(Consts.PICTURE_NAME_ON_PETLA)) {
                return R.drawable.radio_on_petla;
            } else if (whatToSet.equals(Consts.PICTURE_NAME_ON_TIMER)) {
                return R.drawable.radio_on_timer;
            } else if (whatToSet.equals(Consts.PICTURE_NAME_ON_PETLA_TIMER)) {
                return R.drawable.radio_on_timer_petla;
            }
        }
        return R.drawable.ic_launcher;
    }
}

package otto_library_android.coderzheaven.com.ottolibraryandroiddemo;

import com.squareup.otto.Bus;

/**
 * Created by vipinvijayan on 30/12/17.
 */

public class OttoBus {
    private static Bus sBus;
    public static Bus getBus() {
        if (sBus == null)
            sBus = new Bus();
        return sBus;
    }
}
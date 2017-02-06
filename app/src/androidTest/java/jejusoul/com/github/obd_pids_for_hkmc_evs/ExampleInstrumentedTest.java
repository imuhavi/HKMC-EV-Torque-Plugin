package jejusoul.com.github.obd_pids_for_hkmc_evs;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.opencsv.CSVReader;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("jejusoul.com.github.obd_pids_for_hkmc_evs", appContext.getPackageName());


        File filesDir = appContext.getFilesDir();

        AssetManager assetManager = appContext.getAssets();
        String[] files = assetManager.list("");



        for(String filename : files) {

            CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(appContext.getAssets().open("SoulEV/Kia_Soul_EV_Battery_Cell_data.csv"))));
            String[] nextLine;
        }

        assertEquals("jejusoul.com.github.obd_pids_for_hkmc_evs", appContext.getPackageName());
    }

}

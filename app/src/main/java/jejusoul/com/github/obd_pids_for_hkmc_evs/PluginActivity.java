package jejusoul.com.github.obd_pids_for_hkmc_evs;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.prowl.torque.remote.ITorqueService;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import java.util.List;


public class PluginActivity extends Activity {

    private ITorqueService torqueService;
    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            try {

                List<String> name = new ArrayList<String>();
                List<String> shortName = new ArrayList<String>();
                List<String> modeAndPID = new ArrayList<String>();
                List<String> equation = new ArrayList<String>();
                List<Float> minValue = new ArrayList<Float>();
                List<Float> maxValue = new ArrayList<Float>();
                List<String> unit = new ArrayList<String>();
                List<String> header = new ArrayList<String>();

                String[] nextLine;
                for (String filename : getAssets().list("Soul EV")) {
                    File filesDir = getFilesDir();
                    CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(getAssets().open("Soul EV/" + filename), Charset.forName("UTF-8"))));

                    while ((nextLine = reader.readNext()) != null) {
                        // Manage extra PIDs/Sensors >> Add predefined set >> HKMC EV
                        name.add(nextLine[0]);
                        shortName.add(nextLine[1]);
                        modeAndPID.add(nextLine[2]);
                        equation.add(nextLine[3]);
                        minValue.add(Float.parseFloat(nextLine[4]));
                        maxValue.add(Float.parseFloat(nextLine[5]));
                        unit.add(nextLine[6]);
                        header.add(nextLine[7]);
                    }
                }

                float[] minvalueArray = new float[minValue.size()];
                for (int i = 0; i < minValue.size(); i++) {
                    minvalueArray[i] = minValue.get(i).floatValue();
                }
                float[] maxvalueArray = new float[maxValue.size()];
                for (int i = 0; i < maxValue.size(); i++) {
                    maxvalueArray[i] = maxValue.get(i).floatValue();
                }

                boolean success = torqueService.sendPIDDataV2(getPackageName(),
                        name.toArray(new String[name.size()]),
                        shortName.toArray(new String[shortName.size()]),
                        modeAndPID.toArray(new String[modeAndPID.size()]),
                        equation.toArray(new String[equation.size()]),
                        minvalueArray,
                        maxvalueArray,
                        unit.toArray(new String[unit.size()]),
                        header.toArray(new String[header.size()]),
                        null,
                        null
                );

                if (!success) {
                    Toast.makeText(getApplicationContext(), "Fail to add PID data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Finish", Toast.LENGTH_SHORT).show();
                }

            }
            catch (Exception ex)
            {
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    };


    /**
     * Bits of service code. You usually won't need to change this.
     */
    private ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName arg0, IBinder service) {
            torqueService = ITorqueService.Stub.asInterface(service);

            try {
                if (torqueService.getVersion() < 19) {
                    return;
                }
            } catch (RemoteException e) {

            }

        }

        ;

        public void onServiceDisconnected(ComponentName name) {
            torqueService = null;
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.soul_since_2015).setOnClickListener(mClickListener);


        Intent intent = new Intent();
        intent.setClassName("org.prowl.torque", "org.prowl.torque.remote.TorqueService");
        boolean successfulBind = bindService(intent, connection, 0);

        if (successfulBind) {

            // Not really anything to do here.  Once you have bound to the service, you can start calling
            // methods on torqueService.someMethod()  - look at the aidl file for more info on the calls

        } else {

            Toast.makeText(getApplicationContext(), "Fail to bind torque service", Toast.LENGTH_SHORT).show();
        }

    }
}

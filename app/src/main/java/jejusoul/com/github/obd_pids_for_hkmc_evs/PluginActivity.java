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

public class PluginActivity extends Activity {

    private ITorqueService torqueService;
    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            try {
                // Manage extra PIDs/Sensors >> Add predefined set >> HKMC EV
                // TODO: Categorize by vehicle model
                boolean success = torqueService.sendPIDData("HKMC EV",
                        new String[]{"XXXXX"},
                        new String[]{"TPID"},
                        new String[]{"2101"},
                        new String[]{"A*100"},
                        new float[]{0.0f},
                        new float[]{1000.0f},
                        new String[]{"mA"},
                        new String[]{"Auto"});

                if (!success) {
                    Toast.makeText(getApplicationContext(), "Fail to add PID data", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Successfully added PID data", Toast.LENGTH_SHORT).show();
                }

            } catch (RemoteException ex) {
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

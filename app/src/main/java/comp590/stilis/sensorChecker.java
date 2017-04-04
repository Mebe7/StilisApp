package comp590.stilis;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class sensorChecker extends AppCompatActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mAccelerometer, mGyro;
    private long mTime = System.nanoTime();
    private float vX=0, vY=0,vZ=0;
    private float coordX=0, coordY= 0;
    private float aX=0, aY=0, aZ=0;

    //distance in meters from center of phone to corner
    private final float hypotenuse = 0.1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_checker);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, 10000);
        mSensorManager.registerListener(this, mGyro, 10000);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //set a standard.
    //update position based off previous velocity
    //update velocity based off current location
    public void onSensorChanged(SensorEvent sensorEvent){
        long cTime = System.nanoTime();
        float tDif = cTime-mTime;
        float nvX, nvY;
        if (sensorEvent.sensor.getType() == sensorEvent.sensor.TYPE_GYROSCOPE){
            gyroChange(tDif, sensorEvent.values);
            return;
        }
        else{
            accelChange(tDif, sensorEvent.values);
        }
    }

/*this should, in order
update position based off initial velocity, initial acceleration, initial positon
update velocity
update "initial" acceleration
 */
    private void accelChange(float tDif, float values[]){
        float halfTimeSquared = (tDif * tDif)/2;
        coordX = coordX + (tDif * vX) + halfTimeSquared*aX;
        coordY = coordY + (tDif * vY) + halfTimeSquared*aY;
        vX = vX + tDif * values[0];
        vY = vY + tDif * values[1];

        /*
        potential thing:
        only update position based on accelerometer data very rarely, maybe once per 50 ms
        keep average of accelerometer values, then  just call this event with those average
        over the time period
         */
        aX = values[0];
        aY = values[1];
    }

    private void gyroChange(float tDif, float values[]){

    }

}

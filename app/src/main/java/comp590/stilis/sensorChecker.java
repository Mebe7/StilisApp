package comp590.stilis;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static android.hardware.Sensor.TYPE_GRAVITY;

public class sensorChecker extends AppCompatActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mLinearAccel, mGyro;
    private long mTime = System.nanoTime();
    private float vX=0, vY=0,vZ=0; //actual velocity
    private float coordXCenter=0, coordYCenter= 0; //used by accelerometer to keep track of center of phone
    private float aX=0, aY=0, aZ=0; //actual acceleration as last measured
    private float mPitch = 0, mYaw = 0, mRoll = 0; //tilt of phone
    private float coordXTip, coordYTip; // actual coordinates of tip of phone, to be returned to UI

    private final float hypotenuse = 0.086f;//distance in meters from center of phone to corner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_checker);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //Note, this automatically takes account of gravity for us
        mLinearAccel= mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mLinearAccel, 10000);
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
        else if (sensorEvent.sensor.getType() == sensorEvent.sensor.TYPE_LINEAR_ACCELERATION){
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
        coordXCenter = coordXCenter + (tDif * vX) + halfTimeSquared*aX;
        coordYCenter = coordYCenter + (tDif * vY) + halfTimeSquared*aY;
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


    //updates coordXtip and coordYtip based off center location and orientation
    private void updateTip{

    }
}

package comp590.stilis;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static android.content.Context.SENSOR_SERVICE;
import static android.hardware.Sensor.TYPE_GRAVITY;

public class sensorChecker extends AppCompatActivity implements SensorEventListener{


    private SensorManager mSensorManager;
    private Sensor mLinearAccel, mGyro;
    private long mTime = System.nanoTime();
    private float vX=0, vY=0, vZ=0; //absolute velocity
    private float coordXCenter=0, coordYCenter=0, coordZCenter=0; //used by accelerometer to keep track of center of phone
    private float aX=0, aY=0, aZ=0; //absolute acceleration as last measured
    private float axisX = 0, axisY = 0, axisZ = 0; //tilt of phone,
    private float angsX = 0, angsY = 0, angsZ = 0; //angular velocity
    private float coordXTip, coordYTip; // actual coordinates of tip of phone, to be returned to UI

    private final float halfPhoneWidth = -0.0377f;//distance in meters from center of phone to left
    private final float halfPhoneHeight = 0.0782f;// distance in meters from center of phone to top

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

//update current location based off previous sensor values
//then updates sensor values
//should also figure out way to force change if sensor values haven't changed in so long

    public void onSensorChanged(SensorEvent sensorEvent){
        long cTime = System.nanoTime();
        float tDif = cTime-mTime;
        float nvX, nvY;
        updateLoc(tDif);
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
    private void accelChange(float tDif, float[] values){
        /*
        potential thing:
        only update position based on accelerometer data very rarely, maybe once per 50 ms
        keep average of accelerometer values, then  just call this event with those average
        over the time period
         */

        //slide on table mode
        aX = values[0];
        aY = values[1];
        aZ = values[2];
        //absolute mode
        /* gonna debug slide on table mode first
        float[] rotated = rotateVector(values, true);
        aX = rotated[0];
        aY = rotated[1];
        aZ = rotated[2];
        */
    }

    private void gyroChange(float tDif, float[] values){

    }


    //updates center location based off current velocity, acceleration
    //updates coordXtip and coordYtip based off center location and orientation
    private void updateLoc(float tDif){

        //update orientation
        axisX = axisX + angsX*tDif;
        axisY = axisY + angsY*tDif;
        axisZ = axisZ + angsZ*tDif;

        float halfTimeSquared = (tDif * tDif)/2;
        coordXCenter = coordXCenter + (tDif * vX) + halfTimeSquared*aX;
        coordYCenter = coordYCenter + (tDif * vY) + halfTimeSquared*aY;
        coordZCenter = coordZCenter + (tDif * vZ) + halfTimeSquared*aZ;
        //update velocities
        vX = vX + tDif * aX;
        vY = vY + tDif * aY;
        vZ = vZ + tDif * aZ;
    }

    private float[] rotateVector( float[] values, boolean isInverted){
        if (values.length!= 3){
            return values;
        }
        //potentially make custom method for sin, cos that uses estimation
        //would require making sure angles aren't over 2pi
        double plusminus1;
        if (isInverted){
            plusminus1 = -1.0;
        }
        else{
            plusminus1 = 1.0;
        }
        float[] rotated = new float[3];
        double cosX = Math.cos(plusminus1 * (double)axisX);
        double sinX = Math.sin(plusminus1 * (double)axisX);
        double cosY = Math.cos(plusminus1 * (double)axisY);
        double sinY = Math.sin(plusminus1 * (double)axisY);
        double cosZ = Math.cos(plusminus1 * (double)axisZ);
        double sinZ = Math.sin(plusminus1 * (double)axisZ);

        //complicated ass matrix rotation
        rotated[0] =(float)(cosZ*(values[0]*cosY + sinY*(values[1]*sinX + values[2]*cosX))
                        -sinZ*(values[1]*cosX - values[2]*sinX));
        rotated[1] =(float)(sinZ*(values[0]*cosY + sinY*(values[1]*sinX + values[2]*cosX))
                        +cosZ*(values[1]*cosX - values[2]*sinX));
        rotated[2] =(float)(-sinY*values[0] + cosY*(values[1]*sinX + values[2]*cosX));

        return rotated;
    }

    //returns x, y, z coordinates
    //can check in main thread if the z coordinate is too high too allow writing
    protected float[] getCoords(){
        //slide on table mode
        return new float[]{coordXCenter,coordYCenter,coordZCenter};
        //absolute mode
        //return getTip();
    }

    //spins the vector from the center to the bottom right corner of the phone around based
    //on orientation, then adds it to the phone center to get absolute coordinates of the tip
    private float[] getTip(){

        float[] rotated = rotateVector(new float[]{halfPhoneWidth,halfPhoneHeight,0f}, false);
        rotated[0] = rotated[0] + coordXCenter;
        rotated[1] = rotated[1] + coordYCenter;
        rotated[2] = rotated[2] + coordZCenter;
        return rotated;
    }
}

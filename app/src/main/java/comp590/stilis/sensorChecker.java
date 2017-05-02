package comp590.stilis;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;

import java.util.ArrayList;

public class sensorChecker implements SensorEventListener{


    private SensorManager mSensorManager;
    private Sensor mLinearAccel, mGyro;
    private StylishView notepad;
    private long mTime = System.nanoTime();
    private ArrayList<Float> last5Height = new ArrayList<Float>(5);

    private final float halfPhoneWidth = -0.0377f;//distance in meters from center of phone to left
    private final float halfPhoneHeight = 0.0782f;// distance in meters from center of phone to top
    private Phone p;
    private long timeSinceScreenUpdate = 1000000000L;

    public sensorChecker(Sensor linAccel, Sensor gyro, StylishView customView, Phone phone) {
        mLinearAccel = linAccel;
        mGyro = gyro;
        notepad = customView;
        p = phone;
        //mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //Note, this automatically takes account of gravity for us
        //mLinearAccel= mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        //mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }



    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

//should also figure out way to force change if sensor values haven't changed in so long
/*this should, in order
update position based off initial velocity, initial acceleration, initial positon
update velocity
update "initial" acceleration / update gyro
 */

    public void onSensorChanged(SensorEvent sensorEvent){
        long cTime = System.nanoTime();
        double tDif = ((double)(cTime-mTime)) * .000000001;
        p.updateLoc(tDif);

        if (sensorEvent.sensor.getType() == sensorEvent.sensor.TYPE_GYROSCOPE){
            p.setGyro(sensorEvent.values);
        }
        else if (sensorEvent.sensor.getType() == sensorEvent.sensor.TYPE_LINEAR_ACCELERATION){
            p.setAccel(sensorEvent.values);
        }

        notepad.victorSentMeSomething(p.getCoords());

        mTime = System.nanoTime();
        timeSinceScreenUpdate += mTime - cTime + tDif;
    }
}

class Phone{
    //all arrays are of size three, with[0] referring to x, [1] to y, [2] to z
    protected float[] cCenter = new float[]{0,0,0};     //center of phone
    private float[] vel = new float[]{0,0,0};         //absolute velocity
    private float[] accel = new float[]{0,0,0};       //absolute acceleration as last measured
    private float[] axis = new float[]{0,0,0};        //tilt of phone,
    private float[] angs = new float[]{0,0,0};        //angular velocity
    private int vCounter = 0;
    private float[] accelSum =new float[]{0,0,0};
    private final float halfPhoneWidth;     //distance in meters from center of phone to left
    private final float halfPhoneHeight;    // distance in meters from center of phone to top
    private final float xScaler, yScaler;   //pixels per meter
    protected final float xMax, yMax;

    Phone(float halfHeight, float halfWidth, DisplayMetrics metrics){

        xScaler = metrics.xdpi * 39.3701f; //number of inches per meter
        yScaler = metrics.ydpi * 39.3701f;
        xMax = ((float)metrics.widthPixels -1)/ xScaler;
        yMax = ((float)metrics.heightPixels -1)/ yScaler;
        cCenter[0] = xMax/2.0f;
        cCenter[1] = -yMax/2.0f;
        halfPhoneHeight = ((float)metrics.heightPixels/2.0f) / yScaler;
        halfPhoneWidth = -((float)metrics.widthPixels/2.0f) / xScaler;

    }

    //returns x, y, z coordinates
    //can check in main thread if the z coordinate is too high too allow writing
    protected float[] getCoords(){
        //slide on table mode
        float[] boundsChecked;
        //boundsChecked = cCenter.clone();
        //absolute mode
        boundsChecked = getTip();
        boundsChecked[0] = boundsChecked[0]*xScaler;
        boundsChecked[1] = yMax - boundsChecked[1]* yScaler;
        return boundsChecked;

    }
    protected float[] getWidthHeight(){
        return new float[]{halfPhoneWidth, halfPhoneHeight, 0f};
    }

    //spins the vector from the center to the bottom right corner of the phone around based
    //on orientation, then adds it to the phone center to get absolute coordinates of the tip
    private float[] getTip(){

        float[] rotated = rotateVector(new float[]{halfPhoneWidth/2.0f,halfPhoneHeight/2.0f,0f}, false);
        for(int i = 0; i<rotated.length; i++){
            rotated[i] += cCenter[i];
        }
        return rotated;
    }

    private float[] rotateVector( float[] values, boolean isInverted){
        if (values.length!= 3){
            throw new IllegalArgumentException("Array must be of length three");
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
        double cos[] = new double[3];
        double sin[] = new double[3];
        for (int i = 0; i<3; i++){
            cos[i] = Math.cos(plusminus1 * (double) axis[i]);
            sin[i] = Math.sin(plusminus1 * (double) axis[i]);
        }
        double ysinxPlusZcosX = values[1]*sin[0] + values[2]*cos[0];
        //double xcosyPLUSysinxPLUSZcosX = ysinxPlusZcosX + values[0]*cos[1]+ sin[1]*ysinxPlusZcosX;
        //complicated ass matrix rotation
        rotated[0] =(float)(cos[2]*(values[0]*cos[1] + sin[1]*(ysinxPlusZcosX))
                -sin[2]*(values[1]*cos[0] - values[2]*sin[0]));
        rotated[1] =(float)(sin[2]*(values[0]*cos[1] + sin[1]*(ysinxPlusZcosX))
                +cos[2]*(values[1]*cos[0] - values[2]*sin[0]));
        rotated[2] =(float)(-sin[1]*values[0] + cos[1]*(ysinxPlusZcosX));

        return rotated;
    }

    void updateLoc(double tDif){

        double halfTimeSquared = (tDif * tDif)/2.0;
        for(int i = 0; i<3; i++){
            axis[i] += angs[i]*tDif;
            //must be in this order to make physical sense
            cCenter[i] += tDif*vel[i] + halfTimeSquared * accel[i];
           // vel[i] += tDif* accel[i]; //try to not keep track of veloctiy, only use recent acceleration values
        }
        vCounter++;
/*
        if (cCenter[0] <0){
            cCenter[0] = 0;
        }
        else if (cCenter[0] > xMax){
            cCenter[0] = xMax;
        }
        if (cCenter[1] <0){
            cCenter[1] = 0;
        }
        else if (cCenter[1] > yMax){
            cCenter[1] = yMax;
        }


        if (cCenter[2] <0){
            cCenter[2] = 0;
        }

        if (vCounter == 10){
            for (int i = 0; i<3; i++){
                vel[i] = 0;
            }
        }
*/
    }

    void setAccel(float[] values){
        if (values.length != 3){
            throw new IllegalArgumentException("Array must be of length 3");
        }
        /*
        potential thing:
        only update position based on accelerometer data very rarely, maybe once per 50 ms
        keep average of accelerometer values, then  just call this event with those average
        over the time period
         */
        /*
        for (int i = 0; i<3; i++){
                accelSum[i] += values[i];
        }
        accelCounter++;
        if (accelCounter <50){
            for (int i = 0; i<3; i++){
                accel[i] = accelSum[i]/50.0f;
            }
            accelCounter = 0;
        }

        */
        //try rounding it down

        for (int i = 0; i<3; i++){
            int temp = (int)(values[i]*100.0f);
            accel[i] = (float) (temp/100);
        }



        //slide on table mode

        //absolute mode
        /* gonna debug slide on table mode first
       accel = rotateVector(values, true).clone();
        */
    }

    void setGyro(float[] values){
        angs = values.clone();
    }

}

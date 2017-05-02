package comp590.stilis;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnClickListener {
    private DrawerLayout drawer;
    private ListView menuList;
    private StylishView notepad;
    private sensorChecker sensorListener;
    private SensorManager mSensorManager;
    private Sensor mLinearAccel;
    private Sensor mGyro;
    private AlertDialog messageBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] menuItems = getResources().getStringArray(R.array.menu_strings);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuList = (ListView) findViewById(R.id.left_drawer);
        notepad = (StylishView) findViewById(R.id.notepad);

        notepad.setDrawingCacheEnabled(true);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mLinearAccel= mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        sensorListener = new sensorChecker(mLinearAccel, mGyro, notepad);

        menuList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, menuItems));

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                poicker(position);
            }
        });

        start();

    }

    //TODO: REPLACE LOREM IPSUM
    private void help() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.dialog_message)
                .setTitle("Help");

        builder.show();

    }

    private void clear() {
        notepad.clear();
    }

    private void save() {
        notepad.buildDrawingCache();
        Bitmap image = Bitmap.createBitmap(notepad.getDrawingCache());

        String imageName = UUID.randomUUID().toString() + "_stylish.bmp";

        MediaStore.Images.Media.insertImage(getContentResolver(), image, imageName, "Stilis note");
    }

    private void color() {
        String[] colors = {"Black", "Blue", "Red", "Green", "Yellow"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Pick a Color")
                .setItems(colors, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Paint currentPaint = notepad.getPaint();

                        switch(which){
                            //Color Black
                            case 0:
                                currentPaint.setColor(Color.BLACK);
                                notepad.setPaint(currentPaint);
                                break;

                            //Color Blue
                            case 1:
                                currentPaint.setColor(Color.BLUE);
                                notepad.setPaint(currentPaint);
                                break;

                            //Color Red
                            case 2:
                                currentPaint.setColor(Color.RED);
                                notepad.setPaint(currentPaint);
                                break;

                            //Color Green
                            case 3:
                                currentPaint.setColor(Color.GREEN);
                                notepad.setPaint(currentPaint);
                                break;

                            //Color Yellow
                            case 4:
                                currentPaint.setColor(Color.YELLOW);
                                notepad.setPaint(currentPaint);
                                break;
                        }
                    }
                });
        builder.show();
    }

    private void weight() {
        String[] weights = {"Hairline", "2px", "3px", "4px", "5px", "6px", "7px", "8px", "9px", "10px"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Pick a Line Thickness")
                .setItems(weights, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Paint currentPaint = notepad.getPaint();

                        switch(which){
                            //Hairline
                            case 0:
                                currentPaint.setStrokeWidth(0);
                                notepad.setPaint(currentPaint);
                                break;

                            //2px
                            case 1:
                                currentPaint.setStrokeWidth(2);
                                notepad.setPaint(currentPaint);
                                break;

                            //3px
                            case 2:
                                currentPaint.setStrokeWidth(3);
                                notepad.setPaint(currentPaint);
                                break;

                            //4px
                            case 3:
                                currentPaint.setStrokeWidth(4);
                                notepad.setPaint(currentPaint);
                                break;

                            //5px
                            case 4:
                                currentPaint.setStrokeWidth(5);
                                notepad.setPaint(currentPaint);
                                break;

                            //6px
                            case 5:
                                currentPaint.setStrokeWidth(6);
                                notepad.setPaint(currentPaint);
                                break;

                            //7px
                            case 6:
                                currentPaint.setStrokeWidth(7);
                                notepad.setPaint(currentPaint);
                                break;

                            //8px
                            case 7:
                                currentPaint.setStrokeWidth(8);
                                notepad.setPaint(currentPaint);
                                break;

                            //9px
                            case 8:
                                currentPaint.setStrokeWidth(9);
                                notepad.setPaint(currentPaint);
                                break;

                            //10px
                            case 9:
                                currentPaint.setStrokeWidth(10);
                                notepad.setPaint(currentPaint);
                                break;
                        }
                    }
                });
        builder.show();
    }

    private void stroke() {
        String[] colors = {"Solid", "Dashed", "Dotted"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Pick a Line Style")
                .setItems(colors, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Paint currentPaint = notepad.getPaint();

                        switch(which){
                            //Solid Line
                            case 0:
                                currentPaint.setPathEffect(new DashPathEffect(new float[] {0,0}, 0));
                                notepad.setPaint(currentPaint);
                                break;

                            //Dashed Line
                            case 1:
                                currentPaint.setPathEffect(new DashPathEffect(new float[] {10,20}, 0));
                                notepad.setPaint(currentPaint);
                                break;

                            //Dotted Line
                            case 2:
                                currentPaint.setPathEffect(new DashPathEffect(new float[] {10,10}, 0));
                                notepad.setPaint(currentPaint);
                                break;

                        }
                    }
                });
        builder.show();

    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(sensorListener, mLinearAccel, 10000);
        mSensorManager.registerListener(sensorListener, mGyro, 10000);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(sensorListener);
    }

    protected void poicker(int position){
        switch (position) {
            //Save
            case 0:
                menuList.setItemChecked(0, false);
                save();
                drawer.closeDrawers();
                break;

            //Clear
            case 1:
                menuList.setItemChecked(1, false);
                clear();
                drawer.closeDrawers();
                break;

            //Color
            case 2:
                menuList.setItemChecked(2, false);
                color();
                drawer.closeDrawers();
                break;

            //Weight
            case 3:
                menuList.setItemChecked(3, false);
                weight();
                drawer.closeDrawers();
                break;

            //Stroke
            case 4:
                menuList.setItemChecked(4, false);
                stroke();
                drawer.closeDrawers();
                break;

            //Help
            case 5:
                menuList.setItemChecked(5, false);
                help();
                drawer.closeDrawers();
                break;
        }
    }

    private void start(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Please lay your phone on the surface in front of you")
                .setTitle("Calibrating");
        builder.setPositiveButton("Done!", this );

        messageBox = builder.create();
        messageBox.show();
    }

    public void onClick(DialogInterface d, int b){
        messageBox.dismiss();
        //register the listener only once the
        mSensorManager.registerListener(sensorListener, mLinearAccel, 10000);
        mSensorManager.registerListener(sensorListener, mGyro, 10000);
    }
}

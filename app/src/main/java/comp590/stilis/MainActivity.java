package comp590.stilis;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private String[] menuItems;
    private DrawerLayout drawer;
    private ListView menuList;
    private StylishView notepad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuItems = getResources().getStringArray(R.array.menu_strings);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuList = (ListView) findViewById(R.id.left_drawer);
        notepad = (StylishView) findViewById(R.id.notepad);

        notepad.setDrawingCacheEnabled(true);

        menuList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, menuItems));

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.wtf("CLICK", "CLICK IN ANONYMOUS CLASS");
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
        });
    }

    //TODO: REPLACE LOREM IPSUM
    protected void help() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.dialog_message)
                .setTitle("Help");

        builder.show();

    }

    protected void clear() {
        notepad.clear();
    }

    protected void save() {
        notepad.buildDrawingCache();
        Bitmap image = Bitmap.createBitmap(notepad.getDrawingCache());

        String imageName = UUID.randomUUID().toString() + "_stylish.bmp";

        MediaStore.Images.Media.insertImage(getContentResolver(), image, imageName, "Stilis note");
    }

    protected void color() {
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
                                notepad.changePaint(currentPaint);
                                break;

                            //Color Blue
                            case 1:
                                currentPaint.setColor(Color.BLUE);
                                notepad.changePaint(currentPaint);
                                break;

                            //Color Red
                            case 2:
                                currentPaint.setColor(Color.RED);
                                notepad.changePaint(currentPaint);
                                break;

                            //Color Green
                            case 3:
                                currentPaint.setColor(Color.GREEN);
                                notepad.changePaint(currentPaint);
                                break;

                            //Color Yellow
                            case 4:
                                currentPaint.setColor(Color.YELLOW);
                                notepad.changePaint(currentPaint);
                                break;
                        }
                    }
                });
        builder.show();
    }

    protected void weight() {
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
                                notepad.changePaint(currentPaint);
                                break;

                            //2px
                            case 1:
                                currentPaint.setStrokeWidth(2);
                                notepad.changePaint(currentPaint);
                                break;

                            //3px
                            case 2:
                                currentPaint.setStrokeWidth(3);
                                notepad.changePaint(currentPaint);
                                break;

                            //4px
                            case 3:
                                currentPaint.setStrokeWidth(4);
                                notepad.changePaint(currentPaint);
                                break;

                            //5px
                            case 4:
                                currentPaint.setStrokeWidth(5);
                                notepad.changePaint(currentPaint);
                                break;

                            //6px
                            case 5:
                                currentPaint.setStrokeWidth(6);
                                notepad.changePaint(currentPaint);
                                break;

                            //7px
                            case 6:
                                currentPaint.setStrokeWidth(7);
                                notepad.changePaint(currentPaint);
                                break;

                            //8px
                            case 7:
                                currentPaint.setStrokeWidth(8);
                                notepad.changePaint(currentPaint);
                                break;

                            //9px
                            case 8:
                                currentPaint.setStrokeWidth(9);
                                notepad.changePaint(currentPaint);
                                break;

                            //10px
                            case 9:
                                currentPaint.setStrokeWidth(10);
                                notepad.changePaint(currentPaint);
                                break;
                        }
                    }
                });
        builder.show();
    }

    protected void stroke() {
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
                                notepad.changePaint(currentPaint);
                                break;

                            //Dashed Line
                            case 1:
                                currentPaint.setPathEffect(new DashPathEffect(new float[] {10,20}, 0));
                                notepad.changePaint(currentPaint);
                                break;

                            //Dotted Line
                            case 2:
                                currentPaint.setPathEffect(new DashPathEffect(new float[] {10,10}, 0));
                                notepad.changePaint(currentPaint);
                                break;

                        }
                    }
                });
        builder.show();

    }
}

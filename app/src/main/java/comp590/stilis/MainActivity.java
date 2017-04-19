package comp590.stilis;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

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
<<<<<<< HEAD
=======

        menuItems = getResources().getStringArray(R.array.menu_strings);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuList = (ListView) findViewById(R.id.left_drawer);
        notepad = (StylishView) findViewById(R.id.drawing_surface);

        notepad.setDrawingCacheEnabled(true);

        menuList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, menuItems));

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    //Save
                    case 1:
                        save();
                        break;

                    //Clear
                    case 2:
                        clear();
                        break;

                    //Color
                    case 3:
                        Paint newColor = color();
                        notepad.changePaint(newColor);
                        break;

                    //Weight
                    case 4:
                        Paint newWeight = weight();
                        notepad.changePaint(newWeight);
                        break;

                    //Stroke
                    case 5:
                        Paint newStroke = stroke();
                        notepad.changePaint(newStroke);
                        break;
                }
            }
        });
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

    private Paint color() {

    }

    private Paint weight() {

    }

    private Paint stroke() {
>>>>>>> origin/master
    }
}

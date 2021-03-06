package comp590.stilis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class StylishView extends View {

    private Paint stylishPaint = new Paint();
    private Path stylishPath = new Path();
    private float lastX = 0; //Might need to do something with these to increase UX
    private float lastY = 0; //Might need to do something with these to increase UX
    private float tallestPoint = 1; //maximum drawing height; if not set, cannot draw by design

    public StylishView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public StylishView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    public StylishView(Context context){
        super(context);
        init();
    }

    public StylishView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init();
    }

    public Paint getPaint(){
        return stylishPaint;
    }

    //Lets us change the paint from the MainActivity
    public void setPaint(Paint newPaint) {
        stylishPaint = newPaint;
    }

    private void init() {
        stylishPaint.setColor(Color.BLACK);
        stylishPaint.setStyle(Paint.Style.STROKE);
        stylishPaint.setAntiAlias(true);
        stylishPaint.setStrokeWidth(5);


        stylishPath.moveTo(lastX, lastY);
        //float[] toot = {50f, 50f, 0.2f};
        //victorSentMeSomething(toot);
    }

    public void setBrush(float x, float y){
        stylishPath.moveTo(x, y);
    }
    //TODO: RENAME and flesh out
    public void victorSentMeSomething(float[] tip) {


        float newX = tip[0];
        float newY = tip[1];
        Log.wtf("New Coord", newX + ", " + newY);


        //if(Math.pow((newX - lastX),2) > .002 || Math.pow((newY - lastY),2) > .002) {
            //lastX = newX;
            //lastY = newY;
            stylishPath.lineTo(newX, newY);
            stylishPath.moveTo(newX, newY);
            invalidate();
        //}
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(255, 255, 255, 255);
        canvas.drawPath(stylishPath, stylishPaint);
    }

    //Method that allows the MainActivity to clear the canvas
    public void clear() {
        stylishPath.rewind();
        stylishPath.moveTo(lastX, lastY);

        invalidate();
    }

    private void setLastX(float newX) {
        lastX = newX;
    }

    private void setLastY(float newY) {
        lastY = newY;
    }

    public void setStart(float x, float y){
        setLastX(x);
        setLastY(y);
    }

    public void setDrawingHeight(float height){
        tallestPoint = height;
    }
}

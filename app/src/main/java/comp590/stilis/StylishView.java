package comp590.stilis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class StylishView extends View {

    Paint stylishPaint = new Paint();
    Path stylishPath = new Path();
    float lastX = 0; //Might need to do something with these to increase UX
    float lastY = 0; //Might need to do something with these to increase UX

    public StylishView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    //Lets us change the paint from the MainActivity
    public void changePaint(Paint newPaint) {
        stylishPaint = newPaint;
    }

    private void init() {
        stylishPaint.setColor(Color.BLACK);
        stylishPaint.setStyle(Paint.Style.STROKE);
        stylishPaint.setAntiAlias(true);

        stylishPath.moveTo(lastX, lastY);
    }

    //TODO: RENAME and flesh out exact method signature
    public void victorSentMeSomething() {
        float newX = 123; //TODO: replace with actual code to take what given and plot
        float newY = 123; //TODO: replace with actual code to take what given and plot

        stylishPath.lineTo(newX, newY);
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(stylishPath, stylishPaint);
    }

    //Method that allows the MainActivity to clear the canvas
    public void clear() {
        stylishPath.rewind();
        stylishPath.moveTo(lastX, lastY);

        invalidate();
    }

    public void setLastX(float newX) {
        lastX = newX;
    }

    public void setLastY(float newY) {
        lastY = newY;
    }
}

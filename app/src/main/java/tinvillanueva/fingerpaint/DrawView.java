package tinvillanueva.fingerpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by tinvillanueva on 14/03/15.
 */
public class DrawView extends View  {

    /*****variable declaration*****/
    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    /**The initial paint color corresponds to the first color in the palette
     * created in the palette section @ activity_main.xml,
     * which will be initially selected when the app launches.
     */

    private int paintColor = 0xFF000000;  //initial color
    private String paintShape = "triangle"; //initial shape
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;

    private boolean erase = false;
    private  float brushSize, lastBrushSize;
    private int lastSelectedColor;

    private Triangle triangle;


    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing() {
        /*initialize brush size*/
        brushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = brushSize;
        /* instantiating Path and Paint object */
        drawPath = new Path();
        drawPaint = new Paint();
        canvasPaint = new Paint(Paint.DITHER_FLAG);
        //drawPaint.setShape();  ??
        drawPaint.setColor(paintColor); //sets initial color


        /* setting the initial path */
        /** Setting the anti-alias, stroke join and cap styles
         *  will make the user's drawings appear smoother.
         */
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        //triangle
        triangle = new Triangle();


    }

    /* for different drawing tool sizes */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //instantiate bitmap using width and height values
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /* draw canvas and drawing path */
        canvas.drawBitmap(canvasBitmap, 0, 0,canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    /* This method facilitates drawing activity.
    *  It detects user touch using MotionEvent parameter of the onTouchEvent.
    *  The actions needed for the drawing are: down, move, and up.
    **/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //retrieves the X and Y positions of the user touch
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchX);
                break;
            case MotionEvent.ACTION_MOVE:
                //drawPath.lineTo(touchX, touchY);
                switch (paintShape) {
                    case "circle" :
                        drawPath.addCircle(touchX, touchY, 25, Path.Direction.CW);
                        break;
                    case "square" :
                        drawPath.addRect(touchX-25, touchY-25, touchX + 25, touchY + 25, Path.Direction.CW);
                        break;
                    default:

                }
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    //selecting shape from button menu
    public void setShape(String newShape){
        invalidate();
        paintShape = newShape;
    }

    //selecting brush size
    public void setBrushSize(float newSize) {
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize = pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastBrushSize) {
        this.lastBrushSize = lastBrushSize;
    }

    public float getLastBrushSize(){
        return lastBrushSize;
    }

    //selecting color from color palette
    public void setColor(String newColor){
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }


    //remembering the last selected color
    //used after the erase mode
    public void setLastSelectedColor(int lastColor){
        this.lastSelectedColor = lastColor;
    }

    public int getLastSelectedColor(){
        return lastSelectedColor;
    }


    public void newPage(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void setErase(boolean isErasing){
        erase = isErasing;
        if (erase) {
            drawPaint.setAlpha(0xFFFFFFFF); //TODO not working. trying to make it white
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        else {
            drawPaint.setXfermode(null);
        }
    }

    public void resetPage(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }
}

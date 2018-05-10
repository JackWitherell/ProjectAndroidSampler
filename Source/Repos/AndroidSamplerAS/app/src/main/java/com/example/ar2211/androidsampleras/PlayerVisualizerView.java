package com.example.ar2211.androidsampleras;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;

import java.util.Arrays;

public class PlayerVisualizerView extends View {

    public static final int VISUALIZER_HEIGHT = 58;

    private Paint playedStatePainting = new Paint();
    /**
     * Canvas painting for sample scale, filling not played part of audio sample
     */
    private Paint notPlayedStatePainting = new Paint();

    private int width;
    private float progress;
    private float amp;
    private float[] amps = new float[200];

    public PlayerVisualizerView(Context context) {
        super(context);
        init();
        progress=0;
        amp=0.5f;
        Arrays.fill(amps,-1);
    }

    public PlayerVisualizerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        progress=0;
        amp=0.5f;
        Arrays.fill(amps,-1);
    }

    private void init() {

        playedStatePainting.setStrokeWidth(1f);
        playedStatePainting.setAntiAlias(true);
        playedStatePainting.setColor(ContextCompat.getColor(getContext(), R.color.custColor));
        notPlayedStatePainting.setStrokeWidth(1f);
        notPlayedStatePainting.setAntiAlias(true);
        notPlayedStatePainting.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
    }

    /**
     * update and redraw Visualizer view
     */
    public void updateVisualizer() {
        invalidate();
    }
    public void updateLoc(float track_progress, float ampy) {
        progress=track_progress;
        invalidate();
        amp= (float) (((1.5*amp)+(ampy*0.5))/2);
    }



    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0,dp(VISUALIZER_HEIGHT)-(amp*dp(VISUALIZER_HEIGHT)),width,dp(VISUALIZER_HEIGHT),notPlayedStatePainting);
        for(int a=0; a<200; a++){
            if(amps[a]!=-1) {
                canvas.drawRect(((float)a/200)*width,(1-amps[a])*dp(VISUALIZER_HEIGHT),(((float)(a+1)/200))*width,dp(VISUALIZER_HEIGHT),playedStatePainting);
            }
            else{
                a=201;
            }
        }
        if(amps[(int)(progress*200)]==-1){
            amps[(int)(progress*200)]=amp;
            Log.d("meme",""+((int)(progress*200)));
        }
        canvas.drawRect(width*progress,0,(width*progress)+dp(1),dp(VISUALIZER_HEIGHT),notPlayedStatePainting);
    }

    public int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(getContext().getResources().getDisplayMetrics().density * value);
    }
}
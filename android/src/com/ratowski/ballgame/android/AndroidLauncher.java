package com.ratowski.ballgame.android;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureStore;
import android.gesture.GestureStroke;
import android.gesture.Prediction;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.ratowski.ballgame.BGame;

import java.util.ArrayList;

public class AndroidLauncher extends AndroidApplication implements GestureOverlayView.OnGesturePerformedListener, GestureOverlayView.OnGestureListener {

    private GestureLibrary gLibrary;
    private Gesture myGesture;
    boolean gesturePerformed = false;
    GestureOverlayView overlayView;
    View gameView;
    BGame game;
    Thread thread;

    int timer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        game = new BGame();
        gameView = initializeForView(game, config);

        setupGestureLibrary();
        setupOverlayView();
        setupLayout();

    }

    @Override
    public void onGestureStarted(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
    }

    @Override
    public void onGesture(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
        timer++;
        System.out.println("Timer: " + timer);
        gesturePerformed = true;

        if(timer>30){
            gestureOverlayView.cancelGesture();
            timer = 0;
        }
    }

    @Override
    public void onGestureEnded(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
        timer = 0;
        myGesture = overlayView.getGesture();
        if (gesturePerformed) {
            recognizeGesture();
        }
    }

    @Override
    public void onGestureCancelled(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {

    }

    @Override
    public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {

    }

    public void setupOverlayView() {
        overlayView = new GestureOverlayView(this);

        overlayView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                game.getGameScreen().passTouchEvent(event.getAction(), (int) event.getX(), (int) event.getY());
                return true;
            }
        });

        overlayView.addOnGestureListener(this);
        overlayView.addOnGesturePerformedListener(this);
        overlayView.setGestureColor(0x99dddddd);
        overlayView.setUncertainGestureColor(0x99dddddd);
        overlayView.setFadeOffset(0);
        overlayView.setFadeEnabled(false);
        overlayView.setBackgroundColor(0x00000000);
        overlayView.setEventsInterceptionEnabled(false);
        overlayView.setGestureStrokeLengthThreshold(100);
    }

    public void setupGestureLibrary() {
        gLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        gLibrary.setOrientationStyle(GestureStore.ORIENTATION_INVARIANT);
        gLibrary.setOrientationStyle(GestureStore.SEQUENCE_INVARIANT);
        if (!gLibrary.load()) {
            finish();
        }
    }

    public void setupLayout() {
        RelativeLayout layout = new RelativeLayout(this);
        layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.addView(overlayView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(layout);
    }

    private void recognizeGesture() {
        ArrayList<Prediction> predictions = gLibrary.recognize(myGesture);
        if (predictions.size() > 0 && gesturePerformed) {
            gesturePerformed = false;
            Prediction prediction = predictions.get(0);
            if (prediction.score > 3) {
                RectF box = myGesture.getBoundingBox();
                Vector2[] points = getGesturePoints();
                game.getGameScreen().getWorld().passGesture(prediction.score, prediction.name, box.centerX(), box.centerY(), box.width(), box.height(), points);
            }
        }
    }

    private Vector2[] getGesturePoints() {
        GestureStroke stroke = myGesture.getStrokes().get(0);
        Vector2[] points = new Vector2[stroke.points.length / 2];
        for (int i = 0; i < stroke.points.length / 2; i++) {
            points[i] = new Vector2(stroke.points[2 * i], stroke.points[2 * i + 1]);
        }
        return points;
    }

}

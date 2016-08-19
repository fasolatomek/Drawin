package com.ratowski.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.ratowski.world.GameRenderer;
import com.ratowski.world.GameWorld;
import com.ratowski.helpers.InputHandler;


public class GameScreen implements Screen {

    private GameWorld world;
    private GameRenderer renderer;
    private float runTime = 0;

    public GameScreen() {

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float gameWidth = 480;
        float gameHeight = screenHeight / (screenWidth / gameWidth);

        int midPointY = (int) (gameHeight / 2);

        world = new GameWorld(midPointY, screenWidth / gameWidth, screenHeight / gameHeight);
        Gdx.input.setInputProcessor(new InputHandler(world, screenWidth / gameWidth, screenHeight / gameHeight));
        renderer = new GameRenderer(world, (int) gameHeight, midPointY);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        runTime += delta;
        world.update(delta);
        renderer.render(delta, runTime);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public void passTouchEvent(int action, int x, int y) {
        switch (action) {
            case 0:
                ((InputHandler) Gdx.input.getInputProcessor()).touchDown(x, y);
            case 1:
                ((InputHandler) Gdx.input.getInputProcessor()).touchUp(x, y);
            case 2:
                ((InputHandler) Gdx.input.getInputProcessor()).touchDragged(x, y);
        }
    }

    public GameWorld getWorld() {
        return world;
    }
}

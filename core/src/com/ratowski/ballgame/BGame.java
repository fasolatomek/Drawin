package com.ratowski.ballgame;

import com.badlogic.gdx.Game;
import com.ratowski.helpers.AssetLoader;
import com.ratowski.screens.GameScreen;

public class BGame extends Game {

    GameScreen gameScreen;

    @Override
    public void create() {
        AssetLoader.load();
        //setScreen(new SplashScreen(this));
        gameScreen = new GameScreen();
        setScreen(gameScreen);
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

}

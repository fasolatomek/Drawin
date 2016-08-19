package com.ratowski.ballgame;

import com.badlogic.gdx.Game;
import com.ratowski.helpers.AssetLoader;
import com.ratowski.helpers.OverlayInterface;
import com.ratowski.screens.GameScreen;

public class BGame extends Game {

    GameScreen gameScreen;
    OverlayInterface overlayInterface;

    public BGame(OverlayInterface overlayInterface){
        this.overlayInterface=overlayInterface;
    }

    @Override
    public void create() {
        AssetLoader.load();
        //setScreen(new SplashScreen(this));
        gameScreen = new GameScreen(overlayInterface);
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

package com.ratowski.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ratowski.objects.Enemy;
import com.ratowski.objects.Spell;

import java.util.ArrayList;
import java.util.Random;

public class GameWorld {

    private int score = 0;
    private float runTime = 0;
    float scaleFactorX, scaleFactorY;
    float[] spellPoints = {0};

    private ParticleEffect effect;
    ParticleEffectPool effectPool;
    Array<ParticleEffectPool.PooledEffect> effects = new Array();

    public enum GameState {MENU, READY, RUNNING, GAMEOVER, HIGHSCORE}

    public ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    public ArrayList<Spell> spells = new ArrayList<Spell>();

    private GameState currentState;
    public int midPointY;

    public GameWorld(int midPointX, int midPointY, float scaleFactorX, float scaleFactorY) {
        currentState = GameState.MENU;
        this.midPointY = midPointY;
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;

        Random random = new Random();
        enemies.add(new Enemy(new Vector2(0, 0), 5, new Vector2(40, 0), new Vector2(200, 200),random.nextInt(4)+1));

        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("data/effects/shatter.p"), Gdx.files.internal("data/effects/"));
        effectPool = new ParticleEffectPool(effect, 1, 1);
    }


    public void update(float delta) {
        runTime += delta;

        updateCombat(delta);

        for (int i = 0; i < spellPoints.length / 2; i++) {
            System.out.println("Spell point " + i + ": (" + spellPoints[2 * i] + "," + spellPoints[2 * i + 1] + ")");
        }

        if(enemies.size()==0){
            Random random = new Random();
            enemies.add(new Enemy(new Vector2(0, 0), 5, new Vector2(0, 0), new Vector2(200, 200),random.nextInt(4)+1));
        }

        for(int i=0;i<enemies.size();i++){
            enemies.get(i).update(delta);
            if(!enemies.get(i).alive){
                enemies.remove(i);
            }
        }

        for (int i = effects.size - 1; i >= 0; i--) {
            ParticleEffectPool.PooledEffect effect = effects.get(i);
            if (effect.findEmitter("good").getPercentComplete() >= 0.5) {
                effect.free();
                effects.removeIndex(i);
            }
        }

        switch (currentState) {
            case READY:
            case MENU:
                updateReady(delta);
                break;
            case RUNNING:
                updateRunning(delta);
                break;
            default:
                break;
        }
    }

    private void updateReady(float delta) {

        if (delta > .15f) {
            delta = .15f;
        }

    }


    public void updateRunning(float delta) {

        if (delta > .15f) {
            delta = .15f;
        }
    }

    public int getScore() {
        return score;
    }

    public void addScore(int increment) {
        score += increment;
    }

    public int getMidPointY() {
        return midPointY;
    }

    public void start() {
        currentState = GameState.RUNNING;
    }

    public void menu() {
        currentState = GameState.MENU;
    }

    public void ready() {
        currentState = GameState.READY;
    }

    public void restart() {
        score = 0;
    }

    public boolean isReady() {
        return currentState == GameState.READY;
    }

    public boolean isGameOver() {
        return currentState == GameState.GAMEOVER;
    }

    public boolean isHighScore() {
        return currentState == GameState.HIGHSCORE;
    }

    public boolean isMenu() {
        return currentState == GameState.MENU;
    }

    public boolean isRunning() {
        return currentState == GameState.RUNNING;
    }

    public void passGesture(double score, String name, float x, float y, float width, float height, Vector2[] points) {
        System.out.println("Gesture received! " + name + " with score: " + score);
        for (int i = 0; i < points.length; i++) {
            points[i].x /= scaleFactorX;
            points[i].y /= scaleFactorY;
        }

        Spell spell = new Spell(getSpellType(name), 0.5f, new Vector2(x / scaleFactorX, y / scaleFactorY), new Vector2(width, height), 0.5f, points);
        spells.add(spell);
    }


    private void updateCombat(float delta) {
        for (int i = 0; i < spells.size(); i++) {
            spells.get(i).update(delta);
            for (int j = 0; j < enemies.size(); j++) {
                if (enemies.get(j).hit(spells.get(i))) {
                    enemies.get(j).isHit(spells.get(i));
                }
            }
            if(spells.get(i).scale.getValue()==2){
                spells.remove(i);
            }
        }
        for (int i = 0; i < spells.size(); i++) {
            if (spells.get(i).depth > 50) {
                spells.remove(i);
            }
        }
    }

    public void addSpellEffect(float x, float y) {
        ParticleEffectPool.PooledEffect effect = effectPool.obtain();
        effect.setPosition(x, y);
        effects.add(effect);
    }

    int getSpellType(String name) {
        if (name.equals("triangle")) {
            return 1;
        } else if (name.equals("square")) {
            return 2;
        } else if (name.equals("circle")) {
            return 3;
        } else {
            return 4;
        }
    }

}

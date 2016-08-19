package com.ratowski.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.ratowski.TweenAccessors.Value;
import com.ratowski.TweenAccessors.ValueAccessor;
import com.ratowski.objects.Enemy;
import com.ratowski.objects.Spell;
import com.ratowski.helpers.AssetLoader;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

public class GameRenderer {

    private GameWorld myWorld;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;

    FrameBuffer buffer;

    private TweenManager manager;
    private Value alpha = new Value();

    private SpriteBatch batcher;
    private int midPointY;
    private int gameHeight;

    public GameRenderer(GameWorld world, int gameHeight, int midPointY) {
        myWorld = world;

        this.midPointY = midPointY;
        this.gameHeight = gameHeight;

        cam = new OrthographicCamera();
        cam.setToOrtho(true, 480, gameHeight);

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);

        buffer = new FrameBuffer(Pixmap.Format.RGBA8888, 480, gameHeight, false);
    }


    public void render(float delta, float runTime) {
        batcher.setProjectionMatrix(cam.combined);
        shapeRenderer.setProjectionMatrix(cam.combined);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(myWorld.isRunning()) {

            shapeRenderer.begin(ShapeType.Filled);
            displayEnemies();
            shapeRenderer.end();

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            shapeRenderer.begin(ShapeType.Filled);
            displaySpells();
            shapeRenderer.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);

            batcher.begin();
            batcher.enableBlending();

            for (int i = myWorld.effects.size - 1; i >= 0; i--) {
                ParticleEffectPool.PooledEffect effect = myWorld.effects.get(i);
                effect.draw(batcher, delta);
            }

            AssetLoader.font.draw(batcher, myWorld.getScore() + "", 10, 10);
            AssetLoader.font.draw(batcher, String.format("%.2f", myWorld.time), 100, 10);

            batcher.end();
        }

        else if (myWorld.isGameOver()){
            batcher.begin();
            AssetLoader.font.draw(batcher, "GAME OVER",100,100);
            batcher.end();
        }

    }

    private void setupTweens() {
        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();
        Tween.to(alpha, -1, .5f).target(0).ease(TweenEquations.easeOutQuad).start(manager);
    }

    private void displaySpells(){
        for (int i = 0; i < myWorld.spells.size(); i++) {
            Spell spell = myWorld.spells.get(i);

            shapeRenderer.setColor(spell.color[0], spell.color[1], spell.color[2], (1.6f-spell.scale.getValue()));

            for (int j = 0; j < spell.points.length - 1; j++) {
                shapeRenderer.rectLine(
                        spell.points[j + 1].x-(spell.position.x-spell.points[j + 1].x)*(spell.scale.getValue()-1),
                        spell.points[j + 1].y-(spell.position.y-spell.points[j + 1].y)*(spell.scale.getValue()-1),
                        spell.points[j].x-(spell.position.x-spell.points[j].x)*(spell.scale.getValue()-1),
                        spell.points[j].y-(spell.position.y-spell.points[j].y)*(spell.scale.getValue()-1),
                        5+10*(spell.scale.getValue()-1)
                );
            }
        }
    }

    private void displayEnemies(){
        for (int i = 0; i < myWorld.enemies.size(); i++) {
            Enemy enemy = myWorld.enemies.get(i);
            if(enemy.isAlive) {
                if(enemy.isEasy){

                }
                shapeRenderer.setColor(enemy.color[0], enemy.color[1], enemy.color[2], alpha.getValue());
            }
            else{
                shapeRenderer.setColor(0.1f,0.1f,0.1f, alpha.getValue());
            }
            shapeRenderer.rect(enemy.position.x, enemy.position.y, enemy.size.x, enemy.size.y);
        }
    }

}

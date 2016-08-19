package com.ratowski.objects;

import com.badlogic.gdx.math.Vector2;
import com.ratowski.TweenAccessors.Value;
import com.ratowski.TweenAccessors.ValueAccessor;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

public class Spell {
    public Vector2 position;
    public Vector2 size;
    public float depth;
    float speed;
    float power;
    public float[] color = {0};
    public Vector2[] points;
    public Value scale = new Value();
    private TweenManager manager;
    public int type;
    public boolean isFinished=false;

    public Spell(int type, float power, Vector2 position, Vector2 size, float speed, Vector2[] points) {
        this.position = position;
        this.speed = speed;
        this.type = type;
        this.points = points;
        this.size = size;

        scale.setValue(1.1f);
        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();
        Tween.to(scale, -1, 0.5f).target(1.6f).delay(0).ease(TweenEquations.easeOutQuad).start(manager);

        color = setColor(type);

    }

    public void update(float delta) {
        depth += speed;

        if(scale.getValue() >= 1.6){
           isFinished=true;
        }
        else{
            manager.update(delta);
        }
    }

    private float[] setColor(int type) {
        if (type == 1) {
            return new float[]{1, 0, 0};
        } else if (type == 2) {
            return new float[]{0, 1, 0};
        } else if (type == 3) {
            return new float[]{0, 0, 1};
        } else {
            return new float[]{1, 1, 1};
        }
    }


}

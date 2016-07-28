package com.ratowski.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Enemy {
    public Vector2 position;
    float depth;
    Vector2 velocity;
    public Vector2 size;
    public int type;
    public float color[] = {0};
    public boolean alive = true;

    public Enemy(Vector2 position, float depth, Vector2 velocity, Vector2 size, int type) {
        this.position = position;
        this.depth = depth;
        this.size = size;
        this.velocity = velocity;
        this.type = type;
        spawn(type);
    }

    public void update(float delta) {
        position.add(velocity.cpy().scl(delta));

        if(position.x<0 || (position.x+size.x)>480){
            velocity.x = -velocity.x;
        }
    }

    public boolean hit(Spell spell) {
        Rectangle rectangle = new Rectangle(position.x, position.y, size.x, size.y);
        return (rectangle.contains(spell.position.x, spell.position.y) && spell.depth >= depth);
    }

    public void spawn(int type) {
        Random random = new Random();
        position.x = random.nextInt(280);
        position.y = random.nextInt(280);
        if(type==1){
            color=new float[]{1,0,0};
        }
        else if(type==2){
            color=new float[]{0,1,0};
        }
        else if(type==3){
            color=new float[]{0,0,1};
        }
        else if(type==4){
            color=new float[]{1,1,1};
        }
    }

    public void isHit(Spell spell) {
        if(spell.type == type){
            alive=false;
        }
    }


}

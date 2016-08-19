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
    public boolean isAlive = true;
    public boolean isEasy = true;

    public Enemy(Vector2 position, float depth, Vector2 size, int type) {
        this.position = position;
        this.depth = depth;
        this.size = size;
        this.velocity = new Vector2(0,0);
        this.type = type;
        this.setColor(type);
    }

    public Enemy(){
        Random random = new Random();
        this.position = new Vector2(random.nextInt(280),random.nextInt(280));
        this.velocity = new Vector2(0,0);
        this.size = new Vector2(200,200);
        this.depth = 0;
        this.type = random.nextInt(3)+1;
        this.setColor(type);
    }

    public void update(float delta) {
        position.add(velocity.cpy().scl(delta));

        if(position.x<0 || (position.x+size.x)>480){
            velocity.x = -velocity.x;
        }
        if(position.y<0 || (position.y+size.y)>480){
            velocity.y = -velocity.y;
        }
    }

    public boolean hit(Spell spell) {
        Rectangle rectangle = new Rectangle(position.x, position.y, size.x, size.y);
        return (rectangle.contains(spell.position.x, spell.position.y) && spell.depth >= depth);
    }

    public void isHit(Spell spell) {
        if(spell.type == type){
            isAlive = false;
        }
    }

    public void setColor (int type){
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


}

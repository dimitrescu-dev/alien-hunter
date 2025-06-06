package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

// Manages blood splatter effects and blood textures
public class BloodManager {
    ArrayList<Rectangle> blood = new ArrayList<>();
    Animator bloodSplatter = new Animator("bloodSplatter.png",21);
    Texture bloodTexture = new Texture(Gdx.files.internal("blood.png"));
    boolean bloodParticleRunning = false;

    float x,y;

    public BloodManager() {
    }

    // Render all blood effects and handle blood splatter animation
    public void render(SpriteBatch batch)
    {
        for(Rectangle b : blood) batch.draw(bloodTexture,b.x,b.y,b.width,b.height);
        if(bloodParticleRunning)
        {
            if(bloodSplatter.isDone()) {bloodSplatter.end();bloodParticleRunning=false;}
            else bloodSplatter.render(batch,x-128,y-128,256,256);
        }
    }

    // Start a new blood splatter effect at the given position
    public void fire(float x,float y)
    {
        bloodParticleRunning = true;
        this.x=x;this.y=y;
    }

    // Add a static blood texture at the given position
    public void addBlood(float x,float y)
    {
        blood.add(new Rectangle(x,y,48,48));
    }
}

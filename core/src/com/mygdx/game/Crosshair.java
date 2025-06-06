package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Handles the crosshair rendering and animation for the player
public class Crosshair {
    Texture texture = new Texture(Gdx.files.internal("crosshair.png"));
    int size,minStretch,maxStretch;
    float width=32;
    float vel = 0.2f;

    public Crosshair(int minStretch, int maxStretch) {
        this.minStretch = minStretch;
        this.maxStretch = maxStretch;
        width = (float) (maxStretch + minStretch) /2;
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None); // Hide system cursor
    }

    // Render the crosshair at the mouse position
    public void render(SpriteBatch batch)
    {
        tick();
        batch.draw(texture,Gdx.input.getX()-width/2,Gdx.graphics.getHeight()-Gdx.input.getY()-width/2,width,width);
    }

    // Animate the crosshair width for a pulsing effect
    public void tick()
    {
        if(width+vel < minStretch || width+vel > maxStretch) vel = -vel;
        width+=vel;
    }
}

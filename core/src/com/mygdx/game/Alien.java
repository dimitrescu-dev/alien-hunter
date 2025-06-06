package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

// Represents a single alien in the game, with movement and animation
public class Alien extends Rectangle {

    private final Animator animator;
    // Velocity controls how fast the alien moves
    private final float velocity;
    private float xVelocity;
    private float yVelocity;

    public int index;
    boolean gameFinished = false;

    public Alien(float x, float y, float width, float height,int index,float velocity) {
        super(x, y, width, height);
        this.velocity = velocity;
        this.index = index;
        animator = new Animator(index);
        generateVelocity(); // Assign random movement direction
    }

    // Render the alien and update its state
    public void render(SpriteBatch batch,float shakeVelocity)
    {
        if(!gameFinished)tick();
        else runAwayTick();
        animator.render(batch,x+shakeVelocity,y,width,height);
    }

    // Generate a random velocity for the alien
    public void generateVelocity() {
        do {
            xVelocity = MathUtils.random(-velocity, velocity);
            yVelocity = MathUtils.random(-velocity, velocity);
        } while (!(Math.abs(xVelocity) > 0.1f) || !(Math.abs(yVelocity) > 0.1f));
    }

    // Generate maximum velocity for the alien to run away
    public void generateMaxVelocity()
    {
        if(x <= Gdx.graphics.getWidth()/2) xVelocity = -velocity*2;
        else xVelocity = velocity*2;
        if(y <= Gdx.graphics.getHeight()/2) yVelocity = -velocity*2;
        else yVelocity = velocity*2;
        if(yVelocity != 0) xVelocity = MathUtils.random(1,4) == 2 ? 0 : xVelocity;
        if(xVelocity != 0) yVelocity = MathUtils.random(1,4) == 2 ? 0 : yVelocity;
    }

    // Set the alien to run away mode
    public void runAway()
    {
        generateMaxVelocity();
        gameFinished = true;
    }

    // Update the alien's position while running away
    public void runAwayTick()
    {
        x+=xVelocity;
        y+=yVelocity;
    }

    // Update the alien's position and handle collisions
    public void tick()
    {
        if(MathUtils.random(1,600) != 17) {
            if (x + xVelocity < 192 || x + xVelocity + width > Gdx.graphics.getWidth()) xVelocity = -xVelocity;
            if (y + yVelocity < 0 || y + yVelocity + height > Gdx.graphics.getHeight()) yVelocity = -yVelocity;
            y += yVelocity;
            x += xVelocity;
        }
        else {
            generateVelocity();
        }
    }
}

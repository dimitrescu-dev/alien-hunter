package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// Handles animation for aliens and other animated sprites
public class Animator {
    private static int FRAME_COLS = 4;

    private Animation<TextureRegion> alienAnimation;
    private Texture alienSheet;
    private float stateTime;

    private int index;
    private String custom;

    // Constructor for alien animation by index
    public Animator(int index) {
        this.index = index;
        FRAME_COLS = 4;
        init();
    }

    // Constructor for custom animation with specified columns
    public Animator(String a,int cols) {
        custom = a;
        FRAME_COLS = cols;
        init();
    }

    // Initialize the animation frames
    public void init() {
        if(custom == null) alienSheet = new Texture(Gdx.files.internal("aliens/alien"+index+".png"));
        else alienSheet = new Texture(Gdx.files.internal(custom));

        TextureRegion[][] tmp = TextureRegion.split(alienSheet,
                alienSheet.getWidth() / FRAME_COLS,
                alienSheet.getHeight());

        TextureRegion[] alienFrames = new TextureRegion[FRAME_COLS];
        int index = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                alienFrames[index++] = tmp[i][j];
            }
        }
        float dur = 0.15f;
        if(custom != null) dur = 0.02f;
        alienAnimation = new Animation<TextureRegion>(dur, alienFrames);

        stateTime = 0f;
    }

    public void render(SpriteBatch batch,float x,float y,float width,float height) {
        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame = alienAnimation.getKeyFrame(stateTime, custom == null);
        batch.draw(currentFrame, x, y,width,height);
    }

    public boolean isDone()
    {
        return alienAnimation.isAnimationFinished(stateTime);
    }

    public void end()
    {
        stateTime = 0f;
    }

    public void dispose() {
        alienSheet.dispose();
    }
}

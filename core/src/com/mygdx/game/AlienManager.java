package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

// Manages all aliens, game state, and rendering for Alien Hunter
public class AlienManager {
    public ArrayList<Alien> aliens = new ArrayList<>();
    int level = 1;
    int[] targets = new int[3];
    int creatureCount = 36;

    boolean done = false;
    boolean lost = false;
    boolean lostOnce = true;
    int endFrameCounter = 0;

    // Background and overlay textures
    Texture bg = new Texture(Gdx.files.internal("white.png"));
    Texture dead = new Texture(Gdx.files.internal("deadOverlay.png"));

    // Screen shake variables
    float shakeOffset = 0f;
    boolean shaking = false;
    float maxShake = 20f;
    float shakeIntensity = 10f;
    float shakeVelocity = shakeIntensity;
    int cycles = 0;

    // Font and layout for rendering text
    BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
    GlyphLayout layout = new GlyphLayout();

    Crosshair crosshair;
    TextureRegion region1;
    TextureRegion region2;
    TextureRegion region3;

    Sound sound = Gdx.audio.newSound(Gdx.files.internal("shotgun.mp3"));
    Music music = Gdx.audio.newMusic(Gdx.files.internal("soundtrack.ogg"));


    BloodManager manager;

    public AlienManager()
    {
        crosshair = new Crosshair(40,52);
        music.setLooping(true);
        music.setVolume(0.2f);
        music.play();
        spawnEnemies();
    }

    public void spawnEnemies()
    {
        aliens.clear();
        endFrameCounter = 0;
        lost = false;
        done = false;
        shaking = false;
        lostOnce = true;
        manager = new BloodManager();
        targets[0] = MathUtils.random(1,creatureCount);
        targets[1]  = MathUtils.random(1,creatureCount);
        targets[2]  = MathUtils.random(1,creatureCount);
        spawnTargets();
        for(int i = 0; i < Math.min(5 * level-3,197);i++)
        {
            int random = MathUtils.random(1,creatureCount);
            while(random == targets[0]  || random == targets[1]  || random == targets[2]) random = MathUtils.random(1,creatureCount);
            aliens.add(new Alien(MathUtils.random(192+48, Gdx.graphics.getWidth()-48),
                    MathUtils.random(48,Gdx.graphics.getHeight()-48),48,48,random,Math.min(level*0.2f+1f,4f)));
        }
    }

    private void spawnTargets() {
        aliens.add(new Alien(MathUtils.random(192+48, Gdx.graphics.getWidth()-48),
                MathUtils.random(48,Gdx.graphics.getHeight()-48),48,48,targets[0],level*0.2f+1f));
        aliens.add(new Alien(MathUtils.random(192+48, Gdx.graphics.getWidth()-48),
                MathUtils.random(48,Gdx.graphics.getHeight()-48),48,48,targets[1],level*0.2f+1f));
        aliens.add(new Alien(MathUtils.random(192+48, Gdx.graphics.getWidth()-48),
                MathUtils.random(48,Gdx.graphics.getHeight()-48),48,48,targets[2],level*0.2f+1f));
        Texture alienSheet = new Texture(Gdx.files.internal("aliens/alien" + targets[0]  + ".png"));
        region1 = new TextureRegion(alienSheet, 0, 0, alienSheet.getWidth() / 4, alienSheet.getHeight());
        alienSheet = new Texture(Gdx.files.internal("aliens/alien" + targets[1]  + ".png"));
        region2 = new TextureRegion(alienSheet, 0, 0, alienSheet.getWidth() / 4, alienSheet.getHeight());
        alienSheet = new Texture(Gdx.files.internal("aliens/alien" + targets[2]  + ".png"));
        region3 = new TextureRegion(alienSheet, 0, 0, alienSheet.getWidth() / 4, alienSheet.getHeight());
    }

    public void render(SpriteBatch batch)
    {
        if(!done) tick();else endTick();
        layout.setText(font,String.valueOf(level));
        font.setColor(new Color(65f/255f,74f/255f,76f/255,0.25f));
        font.draw(batch,String.valueOf(level),Gdx.graphics.getWidth()/2-layout.width/2+196/2,Gdx.graphics.getHeight()/2 +layout.height/2);
        manager.render(batch);
        for(Alien e : aliens) e.render(batch,shakeOffset);
        batch.draw(bg,0,0,196,Gdx.graphics.getHeight());
        if(targets[0] == -1) batch.draw(dead,0,Gdx.graphics.getHeight()-196,196,196);
        else batch.draw(region1,0,Gdx.graphics.getHeight()-196,196,196);
        if(targets[1] == -1) batch.draw(dead,0,Gdx.graphics.getHeight()/2-196/2,196,196);
        else batch.draw(region2,0,Gdx.graphics.getHeight()/2-196/2,196,196);
        if(targets[2] == -1) batch.draw(dead,0,0,196,196);
        else batch.draw(region3,0,0,196,196);
        crosshair.render(batch);
        Gdx.graphics.setTitle("Alien Hunter - FPS : " + Gdx.graphics.getFramesPerSecond());
    }

    public void tick()
    {
        fire();
        if(shaking)
        {
            if(shakeOffset+ shakeVelocity < -maxShake || shakeOffset+shakeVelocity > maxShake) {
                cycles++;
                shakeVelocity = -shakeVelocity;
            }
            shakeOffset+= shakeVelocity;
            if(cycles == 2 && shakeOffset == 0) shaking = false;

        }
    }

    private void fire() {
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            startCameraShake();
            sound.play(0.2f);
            checkFire();
            endLevel();
        }
    }

    public void loseGame()
    {
        done = true;
        lost = true;
    }

    public void endLevel()
    {
        boolean allDone = false;
        if(!done) {
            allDone = true;
            for (int target : targets) {
                if (target != -1) {
                    allDone = false;
                    break;
                }
            }
        }
        if (allDone || (lost && lostOnce)) {
            lostOnce = false;
            for(Alien a : aliens) a.runAway();
            done = true;
        }
        if (endFrameCounter >= 180) {
            if(!lost)level++;
            else level = 1;
            spawnEnemies();
        }
    }

    public void endTick()
    {
        endFrameCounter++;
        endLevel();
    }

    private void checkFire() {
        int x = Gdx.input.getX();
        int y = Gdx.graphics.getHeight()-Gdx.input.getY();
        for(int j = 0; j < aliens.size();j++) {
            Alien a = aliens.get(j);
            if (a.contains(x, y)) {
                for (int i = 0; i < targets.length; i++) {
                    if (targets[i] == a.index) {
                        targets[i] = -1;
                        manager.addBlood(a.x,a.y);
                        manager.fire(x,y);
                        aliens.remove(a);
                        return;
                    }
                }
                manager.addBlood(a.x,a.y);
                manager.fire(x,y);
                aliens.remove(a);
                loseGame();
            }
        }
    }

    private void startCameraShake() {
        shaking = true;
        cycles = 0;
        shakeVelocity = shakeIntensity;
    }
}

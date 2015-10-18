package com.virginia.cs.SlingshotSam.main;

/**
 * Created by Mac on 9/27/2015.
 */
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.virginia.cs.SlingshotSam.handlers.GameStateManager;

public class Game implements ApplicationListener {
    public static final String TITLE = "Slingshot Sam";
    public static final int V_WIDTH = 320;
    public static final int V_HEIGHT = 240;
    public static final int SCALE = 2;
    public static final float STEP = 0.016666668F;
    private float accum;
    private SpriteBatch sb;
    private OrthographicCamera cam;
    private OrthographicCamera hudCam;
    private GameStateManager gsm;

    public Game() {
    }

    public void create() {
        this.sb = new SpriteBatch();
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, 320.0F, 240.0F);
        this.hudCam = new OrthographicCamera();
        this.hudCam.setToOrtho(false, 320.0F, 240.0F);
        this.gsm = new GameStateManager(this);
    }

    public void render() {
        this.accum += Gdx.graphics.getDeltaTime();

        while(this.accum >= 0.016666668F) {
            this.accum -= 0.016666668F;
            this.gsm.update(0.016666668F);
            this.gsm.render();
        }

    }

    public void dispose() {
    }

    public SpriteBatch getSpriteBatch() {
        return this.sb;
    }

    public OrthographicCamera getCamera() {
        return this.cam;
    }

    public OrthographicCamera getHUDCamera() {
        return this.hudCam;
    }

    public void resize(int w, int h) {
    }

    public void pause() {
    }

    public void resume() {
    }
}

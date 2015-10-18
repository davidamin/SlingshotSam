package com.virginia.cs.SlingshotSam.states;

/**
 * Created by Mac on 9/27/2015.
 */

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.virginia.cs.SlingshotSam.handlers.GameStateManager;
import com.virginia.cs.SlingshotSam.main.Game;

public abstract class GameState {
    protected GameStateManager gsm;
    protected Game game;
    protected SpriteBatch sb;
    protected OrthographicCamera cam;
    protected OrthographicCamera hudCam;

    protected GameState(GameStateManager gsm) {
        this.gsm = gsm;
        this.game = gsm.game();
        this.sb = this.game.getSpriteBatch();
        this.cam = this.game.getCamera();
        this.hudCam = this.game.getHUDCamera();
    }

    public abstract void handleInput();

    public abstract void update(float var1);

    public abstract void render();

    public abstract void dispose();
}


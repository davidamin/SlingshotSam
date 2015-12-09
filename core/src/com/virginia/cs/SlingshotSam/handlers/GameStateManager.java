package com.virginia.cs.SlingshotSam.handlers;

/**
 * Created by Mac on 9/27/2015.
 */


import com.virginia.cs.SlingshotSam.main.Game;
import com.virginia.cs.SlingshotSam.states.GameState;
import com.virginia.cs.SlingshotSam.states.Menu;
import com.virginia.cs.SlingshotSam.states.Play;
import com.virginia.cs.SlingshotSam.states.Play2;


import java.util.Stack;

public class GameStateManager {
    private Game game;
    private Stack<GameState> gameStates;
    public static final int PLAY = 912837;

    public static final int MENU = 8675309;
    public static final int PLAY2 = 123456789;


    public GameStateManager(Game game) {
        this.game = game;
        this.gameStates = new Stack();
        this.pushState(MENU);
        //this.pushState(912837);
    }

    public Game game() {
        return this.game;
    }

    public void update(float dt) {

        ((GameState)this.gameStates.peek()).update(dt);
        //player.update(dt);
    }
    public void reset(){
        this.popState();
        this.pushState(MENU);
    }

    public void start1(){
        this.popState();
        this.pushState(912837);
    }

    public void start2(){
        this.popState();
        this.pushState(PLAY2);
    }

    public void render() {
        ((GameState)this.gameStates.peek()).render();
    }

    private GameState getState(int state) {

        if(state == 912837) {
            return  new Play(this);
        }
        if(state == MENU) {
            return  new Menu(this);
        }
        if(state == PLAY2) {
            //Make this a play2 when we have that
            return  new Play2(this);
        }
        return null;
    }

    public void setState(int state) {
        this.popState();
        this.pushState(state);
    }

    public void pushState(int state) {
        this.gameStates.push(this.getState(state));
    }

    public void popState() {
        GameState g = (GameState)this.gameStates.pop();
        g.dispose();
    }
}

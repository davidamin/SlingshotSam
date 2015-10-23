package com.virginia.cs.SlingshotSam.handlers;

/**
 * Created by Mac on 9/27/2015.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.virginia.cs.SlingshotSam.entities.Sam;
import com.virginia.cs.SlingshotSam.states.Play;

public class MyContactListener implements ContactListener {
    public MyContactListener() {
    }

    public void beginContact(Contact c) {
        /*Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();
        //System.out.println(fa.getUserData() + ", " + fb.getUserData());
        //Gdx.app.log("SlingshotSam", fa.getUserData().getClass().getName());
        //Gdx.app.log("SlingshotSam", fb.getUserData().getClass().getName());
        if(fb.getUserData().equals("foot") && fa.getUserData().equals("ground")){
            Play.this.Lives -= 1;
            Play.this.Shots -= 1;
            Play.this.sam.bodyDef.position.set(1.6F, 2.0F);
        }*/
    }

    public void endContact(Contact c) {
    }

    public void preSolve(Contact c, Manifold m) {
    }

    public void postSolve(Contact c, ContactImpulse ci) {
    }
}

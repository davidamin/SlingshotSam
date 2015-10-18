package com.virginia.cs.SlingshotSam.handlers;

/**
 * Created by Mac on 9/27/2015.
 */

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {
    public MyContactListener() {
    }

    public void beginContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();
        System.out.println(fa.getUserData() + ", " + fb.getUserData());
    }

    public void endContact(Contact c) {
    }

    public void preSolve(Contact c, Manifold m) {
    }

    public void postSolve(Contact c, ContactImpulse ci) {
    }
}

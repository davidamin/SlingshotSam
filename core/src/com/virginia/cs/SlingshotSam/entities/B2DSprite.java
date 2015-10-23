package com.virginia.cs.SlingshotSam.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Michael Snider on 10/22/2015.
 */
public class B2DSprite {
    protected Body body;
    protected Animation animation;
    protected float width;
    protected float height;

    public B2DSprite() {
    }

    public Body getBody() { return body; }
    public Vector2 getPosition() { return body.getPosition(); }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
}

package com.virginia.cs.SlingshotSam.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Michael Snider on 10/23/2015.
 */
public class TouchIndicator {

    protected boolean visible = false;
    protected float centerX, centerY;
    protected Sam sam;
    protected float radius = 0.15f;

    public TouchIndicator(Sam sam) {
        this.sam = sam;
    }

    public void setPosition(float x, float y) {
        centerX = x;
        centerY = y;
    }

    public void setVisible(boolean t) {
        visible = t;
    }

    public void draw(ShapeRenderer renderer) {
        if (!visible) {
            return;
        }

        // line
        renderer.setColor(new Color(0.8196f, 0.4121f, 0.5098f, 1.0f));
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.line(this.centerX, this.centerY, this.sam.getPosition().x, this.sam.getPosition().y);
        renderer.end();

        // circle
        renderer.setColor(new Color(0.4549f, 0.8196f, 0.5098f, 1.0f));
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.circle(this.centerX, this.centerY, this.radius);
        renderer.end();
    }
}

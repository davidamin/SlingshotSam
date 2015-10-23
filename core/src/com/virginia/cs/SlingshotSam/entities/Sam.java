package com.virginia.cs.SlingshotSam.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.virginia.cs.SlingshotSam.main.TouchController;

import sun.rmi.runtime.Log;

/**
 * Created by Michael Snider on 10/22/2015.
 */
public class Sam extends B2DSprite implements TouchController.BoundedTouchListener {

    protected BodyDef bodyDef;
    protected FixtureDef fixtureDef;
    protected float radius = 0.05F;
    protected TouchIndicator touchIndicator;

    public Sam(World world) {
        super();

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(1.6F, 2.0F);
        bodyDef.fixedRotation = false;
        // bodyDef.linearVelocity.set(1f, 0f);
        this.body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(2 * radius, 2 * radius);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 1f;
        fixtureDef.filter.categoryBits = 4;
        fixtureDef.filter.maskBits = 2;
        this.body.createFixture(fixtureDef).setUserData("foot");

        touchIndicator = new TouchIndicator(this);
    }

    public void drawTouchIndicator(ShapeRenderer shapeRenderer) {
        touchIndicator.draw(shapeRenderer);
    }

    @Override
    public boolean isInBounds(float screenX, float screenY) {
        Gdx.app.log("SlingshotSam", String.format("IsInBounds\t\t%.4f %.4f %.4f %.4f", screenX, screenY,
                body.getWorldCenter().x, body.getWorldCenter().y));
        return (Math.abs(screenX - body.getWorldCenter().x) <= radius &&
                Math.abs(screenY - body.getWorldCenter().y) <= radius);
    }

    @Override
    public void handleTouchDown(float screenX, float screenY) {
        Gdx.app.log("SlingshotSam", String.format("Bounded Touch Down!\t\t%.4f, %.4f", screenX, screenY));
        touchIndicator.setVisible(true);
        touchIndicator.setPosition(screenX, screenY);
    }

    @Override
    public void handleTouchDragged(float screenX, float screenY) {
        Gdx.app.log("SlingshotSam", String.format("Bounded Touch Dragged!\t\t%.4f, %.4f", screenX, screenY));
        touchIndicator.setPosition(screenX, screenY);
    }

    @Override
    public void handleTouchUp(float screenX, float screenY) {
        Gdx.app.log("SlingshotSam", String.format("Bounded Touch Up!\t\t%.4f, %.4f", screenX, screenY));
        body.applyForceToCenter(4 * (body.getPosition().x - screenX), 8*(body.getPosition().y - screenY), true);
        touchIndicator.setVisible(false);
    }

    @Override
    public void handleFling(float velocitX, float velocityY) {
        // Nothing for now
    }
}

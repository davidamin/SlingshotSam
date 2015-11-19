package com.virginia.cs.SlingshotSam.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by David on 11/11/2015.
 */
public class GameObject {
    private Sprite s;
    private Texture t;
    private BodyDef bDef;
    private Body b;
    private FixtureDef fDef;

    public void GameObject(String textureFile, float x, float y, float length, float height, float fric, BodyDef.BodyType type, String userdata, World w, float scale){
        bDef = new BodyDef();
        bDef.position.set(x, y);
        bDef.type = type;

        b = w.createBody(bDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(length, height);

        fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.friction = fric;
        fDef.filter.categoryBits = 2;
        fDef.filter.maskBits = 12;
        b.createFixture(fDef).setUserData(userdata);

        t = new Texture(Gdx.files.internal(textureFile));
        s = new Sprite(t);
        s.scale(scale);
    }

    public void update(float timeElapsed, OrthographicCamera cam){
        Vector3 pos = new Vector3(b.getPosition().x,b.getPosition().y,0);
        s.setPosition(cam.project(pos).x,cam.project(pos).y);
    }
    public void render(SpriteBatch sb){
        s.draw(sb);
    }
}

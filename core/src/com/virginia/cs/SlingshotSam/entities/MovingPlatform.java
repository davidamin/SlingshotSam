package com.virginia.cs.SlingshotSam.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


/**
 * Created by Mac on 11/4/2015.
 */

public class MovingPlatform extends B2DSprite{

	public Body platform;
	public Vector2 pos = new Vector2();
	public float xpos;
	public float ypos;
	public Vector2 dir = new Vector2();
	public float dist = 0;
	public float maxDist = 0;
	public World w;
	public FixtureDef fixtureDef;

		public MovingPlatform(float x, float y, float width, float height, float dx, float dy, float maxDist, String userdata, World world) {
			super();
			w = world;
			ypos = y;
			xpos = x;
			platform = createBox(BodyDef.BodyType.KinematicBody, width, height, 1);
			dir.x = dx;
			dir.y = dy;
			this.maxDist = maxDist;
			fixtureDef = new FixtureDef();
			fixtureDef.density = 0.5f;
			fixtureDef.friction = 1f;
			fixtureDef.filter.categoryBits = 2;
			fixtureDef.filter.maskBits = 12;
			PolygonShape poly = new PolygonShape();
			poly.setAsBox(width, height);
			fixtureDef.shape = poly;
			platform.createFixture(fixtureDef).setUserData(userdata);

		}

	public void update(float deltaTime) {
		dist += dir.len() * deltaTime;
		if (dist > maxDist) {
                dir.y *= -1;
				dir.x *=-1;
                dist = 0;
		}

		platform.setLinearVelocity(dir);
	}

    private Body createBox(BodyDef.BodyType type, float width, float height, float density) {
        BodyDef def = new BodyDef();
        def.type = type;
		def.position.set(xpos, ypos);
        Body box = this.w.createBody(def);
        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width, height);

//        box.createFixture(poly, density);
        //poly.dispose();

        return box;
    }
}
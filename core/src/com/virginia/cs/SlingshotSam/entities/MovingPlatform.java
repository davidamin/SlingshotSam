package com.virginia.cs.SlingshotSam.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


/**
 * Created by Mac on 11/4/2015.
 */

public class MovingPlatform extends GameObject{

	public Body platform;
	public Vector2 pos = new Vector2();
	public float xpos;
	public float ypos;
	public Vector2 dir = new Vector2();
	public float dist = 0;
	public float maxDist = 0;
	public World w;
	public FixtureDef fixtureDef;
	private Sprite s;
	private Texture t;
	private BodyDef bDef;
	private Body b;
	private FixtureDef fDef;
	private float x_off;
	private float y_off;


	public MovingPlatform(float x, float y, float width, float height, float dx, float dy, float maxDist, World world){
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
			fixtureDef.friction = 0f;
			fixtureDef.filter.categoryBits = 2;
			fixtureDef.filter.maskBits = 12;
			PolygonShape poly = new PolygonShape();
			poly.setAsBox(width, height);
			fixtureDef.shape = poly;
			platform.createFixture(fixtureDef).setUserData("mplat");
			t = new Texture(Gdx.files.internal("box.png"));
			s = new Sprite(t);
			s.scale(.45f);
			y_off = -30;
			x_off = -30;

		}

	public void update(float deltaTime, OrthographicCamera cam) {
		dist += dir.len() * deltaTime;
		if (dist > maxDist) {
                dir.y *= -1;
				dir.x *=-1;
                dist = 0;
		}
		Vector3 pos = cam.project(new Vector3(platform.getPosition().x, platform.getPosition().y, 0));
		s.setPosition(pos.x+ x_off,pos.y+y_off);

		platform.setLinearVelocity(dir);

	}
	public void render(SpriteBatch sb){
		s.draw(sb);
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
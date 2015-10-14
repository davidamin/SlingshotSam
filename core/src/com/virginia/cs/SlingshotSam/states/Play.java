package com.virginia.cs.SlingshotSam.states;

/**
 * Created by Mac on 9/27/2015.
 */

    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.graphics.Color;
    import com.badlogic.gdx.graphics.OrthographicCamera;
    import com.badlogic.gdx.graphics.g2d.BitmapFont;
    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
    import com.badlogic.gdx.math.Vector2;
    import com.badlogic.gdx.physics.box2d.Body;
    import com.badlogic.gdx.physics.box2d.BodyDef;
    import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
    import com.badlogic.gdx.physics.box2d.CircleShape;
    import com.badlogic.gdx.physics.box2d.FixtureDef;
    import com.badlogic.gdx.physics.box2d.PolygonShape;
    import com.badlogic.gdx.physics.box2d.World;
    import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
    import com.virginia.cs.SlingshotSam.handlers.GameStateManager;
    import com.virginia.cs.SlingshotSam.handlers.MyContactListener;
    import com.virginia.cs.SlingshotSam.states.GameState;

public class Play extends GameState {
    private World world = new World(new Vector2(0.0F, -2.81F), true);
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera b2dCam;
    private SpriteBatch sb;
    private BitmapFont hello;

    private BitmapFont createFont(FreeTypeFontGenerator generator, float dp)
    {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        int fontSize = (int)(dp * Gdx.graphics.getDensity());
        parameter.size = fontSize;
        return generator.generateFont(parameter);
    }

    public Play(GameStateManager gsm) {
        super(gsm);
        this.sb = new SpriteBatch();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Montserrat-Regular.ttf"));
        this.hello = createFont(generator, 32);
        generator.dispose();
        this.hello.setColor(Color.GREEN);
        this.world.setContactListener(new MyContactListener());
        this.b2dr = new Box2DDebugRenderer();
        BodyDef bdef = new BodyDef();
        bdef.position.set(1.6F, 1.2F);
        bdef.type = BodyType.StaticBody;
        Body body = this.world.createBody(bdef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1.5F, 0.05F);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = 2;
        fdef.filter.maskBits = 12;
        body.createFixture(fdef).setUserData("ground");
        bdef.position.set(1.6F, 2.0F);
        bdef.type = BodyType.DynamicBody;
        body = this.world.createBody(bdef);
        shape.setAsBox(0.05F, 0.05F);
        fdef.shape = shape;
        fdef.filter.categoryBits = 4;
        fdef.filter.maskBits = 2;
        body.createFixture(fdef).setUserData("box");
        bdef.position.set(1.53F, 2.2F);
        body.applyForceToCenter(10,10,true);
        body = this.world.createBody(bdef);
        CircleShape cshape = new CircleShape();
        cshape.setRadius(0.05F);
        fdef.shape = cshape;
        fdef.filter.categoryBits = 8;
        fdef.filter.maskBits = 2;
        body.createFixture(fdef).setUserData("ball");
        this.b2dCam = new OrthographicCamera();
        this.b2dCam.setToOrtho(false, 3.2F, 2.4F);

    }

    public void handleInput() {
    }

    public void update(float dt) {
        this.world.step(dt, 6, 2);
    }

    public void render() {
        Gdx.gl20.glClear(16384);
        this.sb.begin();
        hello.draw(this.sb, "Hello World!", 200,400);
        this.sb.end();
        this.b2dr.render(this.world, this.b2dCam.combined);
    }

    public void dispose() {
    }
}

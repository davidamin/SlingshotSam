package com.virginia.cs.SlingshotSam.states;

/**
 * Created by Mac on 9/27/2015.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
    import com.badlogic.gdx.utils.viewport.FitViewport;
    import com.badlogic.gdx.utils.viewport.ScreenViewport;
    import com.badlogic.gdx.utils.viewport.Viewport;
    import com.virginia.cs.SlingshotSam.handlers.GameStateManager;
import com.virginia.cs.SlingshotSam.handlers.MyContactListener;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.virginia.cs.SlingshotSam.main.Game;
import com.virginia.cs.SlingshotSam.main.TouchController;
import com.virginia.cs.SlingshotSam.entities.Sam;

public class Play extends GameState {
    private World world = new World(new Vector2(0.0F, -2.81F), true);
    private Box2DDebugRenderer b2dr;
    public OrthographicCamera b2dCam;
    private Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch sb;
    private BitmapFont hello;
    private Sprite background;
    private Sprite background2;
    private Texture bg_texture;
    private Texture bg_texture2;
    public Sam sam;

    public int height = Gdx.graphics.getHeight();
    public int width = Gdx.graphics.getWidth();

    private ShapeRenderer shapeRenderer;
    private TouchController touchController;

    private long timeElapsed;
    private long levelTime;
    private long maxTime;
    private Timer time;
    private Boolean timeOut = false;

    private BitmapFont createFont(FreeTypeFontGenerator generator, float dp)
    {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        int fontSize = (int)(dp * Gdx.graphics.getDensity());
        parameter.size = fontSize;
        return generator.generateFont(parameter);
    }

    public Play(GameStateManager gsm) {
        super(gsm);

        time = new Timer();

        maxTime = 60000;

        time.schedule(new Timer.Task() {
            public void run() {
                timeOut = true;
            }
        }, maxTime / 1000);

        Music m = Gdx.audio.newMusic(Gdx.files.internal("samSong1.ogg"));
        m.setLooping(true);
        m.play();

        levelTime  = TimeUtils.millis();

        bg_texture = new Texture(Gdx.files.internal("demo_level.png"));
        background = new Sprite(bg_texture);
        bg_texture2 = new Texture(Gdx.files.internal("sky.png"));
        background2 = new Sprite(bg_texture2);

        // Set up camera
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        // Set up ShapeRenderer to display test circle
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        this.sb = new SpriteBatch();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Montserrat-Regular.ttf"));
        this.hello = createFont(generator, 32);
        generator.dispose();

        this.hello.setColor(Color.GREEN);
        this.world.setContactListener(new ContactListener(){
        public void beginContact(Contact c) {
            Fixture fa = c.getFixtureA();
            Fixture fb = c.getFixtureB();
            //System.out.println(fa.getUserData() + ", " + fb.getUserData());
            //Gdx.app.log("SlingshotSam", fa.getUserData().getClass().getName());
            //Gdx.app.log("SlingshotSam", fb.getUserData().getClass().getName());
            /*if(fb.getUserData().equals("foot") && fa.getUserData().equals("ground")){
                Play.this.sam.reset();
            }*/
        }

        public void endContact(Contact c) {
        }

        public void preSolve(Contact c, Manifold m) {
        }

        public void postSolve(Contact c, ContactImpulse ci) {
        }
        });
        this.b2dr = new Box2DDebugRenderer();

        BodyDef bdef = new BodyDef();
        //bdef.position.set(1.6F, 1.2F);
        bdef.position.set(1.6f, 0.05f);
        bdef.type = BodyType.StaticBody;

        Body body = this.world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1.5F, 0.05F);

        FixtureDef fdef = new FixtureDef();

        fdef.shape = shape;
        fdef.friction = 1.0f;
        fdef.filter.categoryBits = 2;
        fdef.filter.maskBits = 12;
        body.createFixture(fdef).setUserData("ground");

        /*bdef.position.set(1.53F, 2.2F);
        body.applyForceToCenter(10, 10, true);
        body = this.world.createBody(bdef);
        CircleShape cshape = new CircleShape();
        cshape.setRadius(0.05F);
        fdef.shape = cshape;
        fdef.filter.categoryBits = 8;
        fdef.filter.maskBits = 2;
        body.createFixture(fdef).setUserData("ball");*/

        // Create test circle
        //body = this.world.createBody(bdef);
        // testCircle = new Circle(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 50, this.world);
        // touchController.registerBoundedTouchListener(testCircle);

        // Create Sam
        sam = new Sam(world);

        this.b2dCam = new OrthographicCamera();
        this.b2dCam.setToOrtho(false, 4.2F, 2.4F);

        // Set Input Processor for app to use TouchController
        // as a source of keyboard input (in case) and as a gesture
        // detector
        touchController = new TouchController(this.b2dCam);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new GestureDetector(touchController));
        inputMultiplexer.addProcessor(touchController);
        Gdx.input.setInputProcessor(inputMultiplexer);

        // Register sam with touch controller
        touchController.registerBoundedTouchListener(sam);
    }

    public void handleInput() {
    }

    public void update(float dt) {
        this.world.step(dt, 6, 2);
        if(sam.body.getPosition().y < 0){
            sam.reset();
        }

        if(sam.respawn){
            sam.body.setTransform(sam.respawn_x, sam.respawn_y, 0);
            sam.body.setAwake(false);
            sam.respawn = false;
        }
        //timeElapsed += Gdx.graphics.getDeltaTime();
    }

    public void render() {
        if(!sam.gameOver) {
            Gdx.gl20.glClear(16384);
            timeElapsed = TimeUtils.timeSinceMillis(levelTime);
            double printable = (maxTime - timeElapsed) / 1000.0;

            String screenText = "Lives: " + String.valueOf(sam.Lives) + "   Shots: " + String.valueOf(sam.Shots);
            this.sb.begin();
            background2.scale(3);
            background2.setPosition(0, 400);
            background2.draw(this.sb);
            background.draw(this.sb);
            //hello.draw(this.sb, "Hello World!", 200,400);

            if (timeOut) {
                sam.gameOver = true;
            } else {
                hello.draw(this.sb, String.format("Time Remaining: %.2f", printable), 80, height - height / 5);
            }

            hello.draw(this.sb, screenText, 80, height - height / 10);

            this.sb.end();

            this.b2dr.render(this.world, this.b2dCam.combined);
        }else{
            this.sb.begin();
            hello.draw(this.sb, "Game Over", 80, height/2);
            this.sb.end();
        }
    }

    public void dispose() {
    }
}

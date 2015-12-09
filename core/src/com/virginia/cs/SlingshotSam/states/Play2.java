package com.virginia.cs.SlingshotSam.states;

/**
 * Created by Mac on 9/27/2015.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.virginia.cs.SlingshotSam.entities.GameObject;
import com.virginia.cs.SlingshotSam.entities.MovingPlatform;
import com.virginia.cs.SlingshotSam.entities.Sam;
import com.virginia.cs.SlingshotSam.handlers.GameStateManager;
import com.virginia.cs.SlingshotSam.main.TouchController;

import java.util.ArrayList;

public class Play2 extends GameState {
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
    private Texture sam_texture;
    private Sprite sam_sprite;
    private Texture bomb_texture;
    private Sprite bomb_sprite;
    public Sam sam;
    public Boolean ended = false;
    public ArrayList<GameObject> objects = new ArrayList<GameObject>();
    public Music m;

    public int height = Gdx.graphics.getHeight();
    public int width = Gdx.graphics.getWidth();
    public float samCamPosX;
    public MovingPlatform mplat, mplat2, mplat3;

    private ShapeRenderer shapeRenderer;
    private TouchController touchController;

    private long timeElapsed;
    private long levelTime;
    private long maxTime;
    private Timer time;
    private Timer resetTimer;
    private Boolean timeOut = false;

    private BitmapFont createFont(FreeTypeFontGenerator generator, float dp)
    {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        int fontSize = (int)(dp * Gdx.graphics.getDensity());
        parameter.size = fontSize;
        return generator.generateFont(parameter);
    }

    public Play2(GameStateManager gsm) {
        super(gsm);

        time = new Timer();

        maxTime = 60000;

        time.schedule(new Timer.Task() {
            public void run() {
                timeOut = true;
            }
        }, maxTime / 1000);

        m = Gdx.audio.newMusic(Gdx.files.internal("samSong2.ogg"));
        m.setLooping(true);
        m.play();

        levelTime  = TimeUtils.millis();

        bg_texture = new Texture(Gdx.files.internal("demo_level_scale.png"));
        background = new Sprite(bg_texture);
        // bg_texture2 = new Texture(Gdx.files.internal("sky.png"));
        //background2 = new Sprite(bg_texture2);
        sam_texture = new Texture(Gdx.files.internal("sam.png"));
        sam_sprite = new Sprite(sam_texture);
        sam_sprite.scale(2);


        // Set up camera
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        this.sb = new SpriteBatch();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Montserrat-Regular.ttf"));
        this.hello = createFont(generator, 32);
        generator.dispose();

        this.hello.setColor(Color.GREEN);
        this.world.setContactListener(new ContactListener() {
            public void beginContact(Contact c) {
                Fixture fa = c.getFixtureA();
                Fixture fb = c.getFixtureB();

                if (fb.getUserData().equals("foot") && fa.getUserData().equals("ground")) {
                    Play2.this.sam.respawn_x = (float) (Play2.this.sam.getPosition().x);
                    Play2.this.sam.respawn_y = (float) (Play2.this.sam.getPosition().y + .1);
                    Play2.this.sam.respawn = true;
                }
                if (fa.getUserData().equals("foot") && fb.getUserData().equals("ground")) {
                    Play2.this.sam.respawn_x = (float) (Play2.this.sam.getPosition().x);
                    Play2.this.sam.respawn_y = (float) (Play2.this.sam.getPosition().y + .1);
                    Play2.this.sam.respawn = true;
                }
                if (fb.getUserData().equals("foot") && fa.getUserData().equals("bomb")) {
                    //Win the game
                    Play2.this.sam.gameOver = true;
                    Play2.this.sam.won = true;
                }
                if (fb.getUserData().equals("bomb") && fa.getUserData().equals("foot")) {
                    //Win the game
                    Play2.this.sam.gameOver = true;
                    Play2.this.sam.won = true;
                }
                if (fb.getUserData().equals("foot") && fa.getUserData().equals("teleport")) {
                    Play2.this.sam.respawn_x = (float) (Play2.this.sam.getPosition().x + 1f);
                    Play2.this.sam.respawn_y = (float) (Play2.this.sam.getPosition().y + 1f);
                    Play2.this.sam.respawn = true;
                }

            }

            public void endContact(Contact c) {
            }

            public void preSolve(Contact c, Manifold m) {
            }

            public void postSolve(Contact c, ContactImpulse ci) {
            }
        });
        this.b2dr = new Box2DDebugRenderer();

        //platform code

        objects.add(new GameObject("dino_plat2.png",.3f,.77f,.1f,.05f,1.0f,BodyType.StaticBody, "ground", this.world, .01f,-100f,-10f));
        objects.add(new GameObject("building1.png",1.75f,.77f,.1f,.05f,1.0f,BodyType.StaticBody, "ground", this.world, .05f,-60.0f,-490.0f));
        objects.add(new GameObject("building1.png",3f,1.5f,.1f,.05f,1.0f,BodyType.StaticBody, "ground", this.world, .05f,-60.0f,-490.0f));
        objects.add(new GameObject("building1.png",5f,1f,.1f,.05f,1.0f,BodyType.StaticBody, "ground", this.world, .05f,-60.0f,-490.0f));

        objects.add(new GameObject("goal.png",8f, 1f,.1f,.1f,1.0f,BodyType.StaticBody, "bomb", this.world, .05f,-50f,-50f));


        //~~~~~~~~~~~~~~~~~~~~~    x  y   w     h   dx   dy max  world
        mplat = new MovingPlatform(1, 1f, .1f, .1f, .5f, 0f, 2f, this.world);
        objects.add(mplat);
        mplat2= new MovingPlatform(2.75f, 1f, .1f, .1f, 0f, .75f, 1.5f, this.world);
        objects.add(mplat2);
        mplat3= new MovingPlatform(4.2f, 1f, .1f, .1f, 1f, 1f, 1.5f, this.world);
        objects.add(mplat3);
//        mplat = new MovingPlatform(1, 1f, .2f, .1f, .5f, 0f, 2f, "teleport", this.world);
//
//        mplat2= new MovingPlatform(2.75f, 1f, .1f, .1f, 0f, .75f, 1.5f, "ground", this.world);
//
//        mplat3= new MovingPlatform(4.2f, 1f, .2f, .1f, 1f, 1f, 1f, "teleport", this.world);
//
        //End platform code

        BodyDef bdef = new BodyDef();
        bdef.position.set(8f, 1f);
        bdef.type = BodyType.StaticBody;

        Body body = this.world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.1F, 0.1F);

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
        sam = new Sam(this.world);

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

        // Set up ShapeRenderer to display test circle
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(this.b2dCam.combined);

        sam.setShots(20);
        sam.setLives(7);
    }

    /*public void registerPlatform(float x, float y){
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
        bdef.type = BodyType.StaticBody;

        Body body = this.world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.1F, 0.05F);

        FixtureDef fdef = new FixtureDef();

        fdef.shape = shape;
        fdef.friction = 1.0f;
        fdef.filter.categoryBits = 2;
        fdef.filter.maskBits = 12;
        body.createFixture(fdef).setUserData("ground");
    }*/


    public void handleInput() {
    }

    public void update(float dt) {
        this.world.step(dt, 6, 2);
        mplat.update(dt, cam);
        mplat2.update(dt, cam);
        mplat3.update(dt, cam);

        shapeRenderer.setProjectionMatrix(this.b2dCam.combined);

        sam_sprite.setPosition(this.b2dCam.project(new Vector3(sam.body.getPosition().x, sam.body.getPosition().y, 0)).x, this.b2dCam.project(new Vector3(sam.body.getPosition().x, sam.body.getPosition().y, 0)).y);
        if(sam.body.getPosition().y < 0){
            sam.reset();
        }

        if(sam.respawn){
            if(sam.Shots < 1){
                sam.gameOver= true;
            }
            sam.isFlying = false;
            sam.body.setTransform(sam.respawn_x, sam.respawn_y, 0);
            sam.body.setAwake(false);
            sam.respawn = false;
        }

        for(GameObject obj: objects){
            obj.update(dt,this.b2dCam);
        }
        //timeElapsed += Gdx.graphics.getDeltaTime();
    }

    public void render() {
        if(ended) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
            m.stop();
            if(sam.won){
                gsm.start3();
            }else {
                gsm.reset();
            }
        }
        Gdx.gl.glClear(16384);
        Gdx.gl.glClearColor(255 / 255f, 204 / 255f, 255 / 255f, 1);
        //Gdx.gl20.glClear(16384);
        samCamPosX = sam.getPosition().x+1;
        if(samCamPosX < 1.9F) {
            samCamPosX = 1.9F;
        }
        if(samCamPosX > 6.3) {
            samCamPosX = 6.3F;
        }
        this.b2dCam.position.set(samCamPosX, 1.27F, 0);
        this.b2dCam.update();
        if(!sam.gameOver) {
            timeElapsed = TimeUtils.timeSinceMillis(levelTime);
            double printable = (maxTime - timeElapsed) / 1000.0;

            String screenText = "Lives: " + String.valueOf(sam.Lives) + "   Shots: " + String.valueOf(sam.Shots);
            this.sb.begin();
            //background2.scale(3);
            //background2.setPosition(this.b2dCam.project(new Vector3(0.0f, 0f, 0)).x, this.b2dCam.project(new Vector3(0f, 0f, 0)).y);
            //background2.draw(this.sb);
            background.setPosition(this.b2dCam.project(new Vector3(0.0f, 0f, 0)).x, this.b2dCam.project(new Vector3(0f, 0f, 0)).y);
            background.scale(3);
            background.draw(this.sb);
            //hello.draw(this.sb, "Hello World!", 200,400);

            if (timeOut) {
                sam.gameOver = true;
            } else {
                hello.draw(this.sb, String.format("Time Remaining: %.2f", printable), 80, height - height / 5);
            }

            hello.draw(this.sb, screenText, 80, height - height / 10);

            mplat2.render(sb);
            mplat3.render(sb);

            sam_sprite.draw(this.sb);
            for(GameObject obj: objects){
                obj.render(this.sb);
            }
            this.sb.end();
            this.b2dr.render(this.world, this.b2dCam.combined);
            this.sam.drawTouchIndicator(shapeRenderer);
        }else {
            Gdx.gl.glClear(16384);
            Gdx.gl.glClearColor(0 / 255f, 0 / 255f, 0 / 255f, 1);
            this.sb.begin();
            if(this.sam.won){
                hello.draw(this.sb, "You win!", 80, height / 2);
                ended = true;
            }else {
                hello.draw(this.sb, "Boom", 80, height / 2);
                ended = true;
            }
            this.sb.end();
        }
    }

    public void dispose() {
    }
}

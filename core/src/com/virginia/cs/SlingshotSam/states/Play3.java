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
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.virginia.cs.SlingshotSam.entities.GameObject;
import com.virginia.cs.SlingshotSam.entities.MovingPlatform;
import com.virginia.cs.SlingshotSam.entities.Sam;
import com.virginia.cs.SlingshotSam.entities.TextScreen;
import com.virginia.cs.SlingshotSam.handlers.GameStateManager;
import com.virginia.cs.SlingshotSam.main.TouchController;

import java.util.ArrayList;

public class Play3 extends GameState {
    private World world = new World(new Vector2(0.0F, -2.81F), true);
    public OrthographicCamera b2dCam;
    private OrthographicCamera camera;
    private SpriteBatch sb;
    private BitmapFont hello;
    private Sprite background;
    private Texture bg_texture;
    private Texture sam_texture;
    private Sprite sam_sprite;

    private Texture bomb_texture;
    private Sprite bomb_sprite;
    private Texture slingshotTexture;
    private Sprite slingshotSprite;

    public Sam sam;
    public Boolean ended = false;
    public ArrayList<GameObject> objects = new ArrayList<GameObject>();
    public Music m;

    public int height = Gdx.graphics.getHeight();
    public int width = Gdx.graphics.getWidth();
    public float samCamPosX;
    public MovingPlatform mplat;

    private ShapeRenderer shapeRenderer;
    private TouchController touchController;

    private long timeElapsed;
    private long levelTime;
    private long maxTime;
    private Timer time;
    private Boolean timeOut = false;
    private Boolean Trolled = false;

    private float shot_offset = 0.3f;

    private BitmapFont createFont(FreeTypeFontGenerator generator, float dp)
    {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        int fontSize = (int)(dp * Gdx.graphics.getDensity());
        parameter.size = fontSize;
        return generator.generateFont(parameter);
    }

    public Play3 (GameStateManager gsm) {
        super(gsm);

        time = new Timer();

        maxTime = 20000;

        time.schedule(new Timer.Task() {
            public void run() {
                timeOut = true;
            }
        }, maxTime / 1000);

        m = Gdx.audio.newMusic(Gdx.files.internal("samSong1.ogg"));
        m.setLooping(true);
        m.play();

        levelTime  = TimeUtils.millis();

        bg_texture = new Texture(Gdx.files.internal("demo_level_scale.png"));
        background = new Sprite(bg_texture);
        sam_texture = new Texture(Gdx.files.internal("sam.png"));
        sam_sprite = new Sprite(sam_texture);
        sam_sprite.scale(2);
        slingshotTexture = new Texture(Gdx.files.internal("Slingshot.png"));
        slingshotSprite = new Sprite(slingshotTexture);
        slingshotSprite.scale((float) 0.0001);

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

                if (fb.getUserData().equals("foot") && fa.getUserData().equals("ground1")) {
                    Play3.this.sam.respawn_x = (float) (Play3.this.sam.getPosition().x);
                    Play3.this.sam.respawn_y = (float) (Play3.this.sam.getPosition().y + shot_offset);
                    Play3.this.sam.respawn = true;
                }
                if (fb.getUserData().equals("foot") && fa.getUserData().equals("ground2")) {
                    Play3.this.sam.respawn_x = (float) (Play3.this.sam.getPosition().x);
                    Play3.this.sam.respawn_y = (float) (Play3.this.sam.getPosition().y + shot_offset);
                    Play3.this.sam.respawn = true;
                    sam.setShots(sam.Shots + 2);
                }
                if (fb.getUserData().equals("foot") && fa.getUserData().equals("ground3")) {
                    Play3.this.sam.respawn_x = (float) (Play3.this.sam.getPosition().x);
                    Play3.this.sam.respawn_y = (float) (Play3.this.sam.getPosition().y + shot_offset);
                    Play3.this.sam.respawn = true;
                    sam.setShots(0);
                    Trolled = true;
                }
                if (fb.getUserData().equals("foot") && fa.getUserData().equals("object1")) {
                    sam.setShots(sam.Shots + 1);
                }
                if (fb.getUserData().equals("foot") && fa.getUserData().equals("bomb")) {
                    //Win the game
                    Play3.this.sam.gameOver = true;
                    Play3.this.sam.won = true;
                }
                if (fb.getUserData().equals("foot") && fa.getUserData().equals("ground")) {
                    Play3.this.sam.respawn_x = (float) (Play3.this.sam.getPosition().x);
                    Play3.this.sam.respawn_y = (float) (Play3.this.sam.getPosition().y + shot_offset);
                    Play3.this.sam.respawn = true;
                }

            }

            public void endContact(Contact c) {
            }

            public void preSolve(Contact c, Manifold m) {
            }

            public void postSolve(Contact c, ContactImpulse ci) {
            }
        });

        //this.b2dr = new Box2DDebugRenderer();

        objects.add(new GameObject("building1.png",.3f,.77f,.1f,.05f,1.0f,BodyType.StaticBody, "ground", this.world, .05f,-60.0f,-490.0f));
        objects.add(new GameObject("building1.png",1.04f,.57f,.1f,.05f,1.0f,BodyType.StaticBody, "ground2", this.world, .05f,-60.0f,-490.0f));
        objects.add(new GameObject("building1.png",1.67f,.3f,.1f,.05f,1.0f,BodyType.StaticBody, "ground", this.world, .05f,-60.0f,-490.0f));
        objects.add(new GameObject("building1.png",2.35f,.46f,.1f,.05f,1.0f,BodyType.StaticBody, "ground2", this.world, .05f,-60.0f,-490.0f));
        objects.add(new GameObject("building1.png",2.55f,.31f,.1f,.05f,1.0f,BodyType.StaticBody, "ground", this.world, .05f,-60.0f,-490.0f));
        objects.add(new GameObject("building1.png", 2.97f, .57f, .1f, .05f, 1.0f, BodyType.StaticBody, "ground2", this.world, .05f, -60.0f,-490.0f));
        objects.add(new GameObject("building1.png",3.53f,.34f,.1f,.05f,1.0f,BodyType.StaticBody, "ground", this.world, .05f,-60.0f,-490.0f));
        objects.add(new GameObject("building1.png",4.1f,.07f,.1f,.05f,1.0f,BodyType.StaticBody, "ground2", this.world, .05f,-60.0f,-490.0f));
        objects.add(new GameObject("94-the-golden-snitch.png", 1.67f, 1.0f, .1f, .05f, 1.0f, BodyType.StaticBody, "ground3", this.world, .05f,-.0f,-.0f));
        //objects.add(new TextScreen("Text Screen.png", ))

        //posx posy width height dx dy maxdist world
        //mplat.pos.set(1F,1F);

        objects.add(new GameObject("bomb.png",4.1f,.27f,.1f,.1f,1.0f,BodyType.StaticBody, "bomb", this.world, 1.5f,-30f,-15f));

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

        // Set up ShapeRenderer to display test circle
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(this.b2dCam.combined);
    }

    /*normal platform = ground
    public void registerObstacle(float x, float y){
        BodyDef bdef=new BodyDef();
        bdef.position.set(x,y);
        bdef.type=BodyType.StaticBody;

        Body body = this.world.createBody(bdef);

        CircleShape shape=new CircleShape();
        shape.setRadius(.1F);

        FixtureDef fdef=new FixtureDef();
        fdef.shape=shape;
        fdef.friction=0.5f;
        fdef.filter.categoryBits = 2;
        fdef.filter.maskBits = 12;
        body.createFixture(fdef).setUserData("obstacle");
    }
    public void registeraddonPlatform(float x, float y){
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
        body.createFixture(fdef).setUserData("ground2");

        ShapeRenderer shaper = new ShapeRenderer();
        shaper.setColor(Color.RED);
    }

    public void registerzeroPlatform(float x, float y){
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
        body.createFixture(fdef).setUserData("ground3");

        ShapeRenderer shaper = new ShapeRenderer();
        shaper.setColor(Color.RED);
    }

    public void registeradd1object(float x, float y){
        BodyDef bdef = new BodyDef();
        bdef.position.set(x,y);
        bdef.type = BodyType.KinematicBody;

        Body body = this.world.createBody(bdef);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.1F);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.friction = 0.0f;
        fdef.filter.categoryBits = 3;
        fdef.filter.maskBits = 13;
        body.createFixture(fdef).setUserData("object1");
    }
*/
    public void handleInput() {
    }

    public void update(float dt) {
        this.world.step(dt, 6, 2);
        //mplat.update(dt, cam);
        shapeRenderer.setProjectionMatrix(this.b2dCam.combined);
        sam_sprite.setPosition(this.b2dCam.project(new Vector3(sam.body.getPosition().x, sam.body.getPosition().y, 0)).x - 25f, this.b2dCam.project(new Vector3(sam.body.getPosition().x, sam.body.getPosition().y, 0)).y -25f);
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
            slingshotSprite.setPosition(this.b2dCam.project(new Vector3(sam.body.getPosition().x, sam.body.getPosition().y, 0)).x - 200, this.b2dCam.project(new Vector3(sam.body.getPosition().x, sam.body.getPosition().y, 0)).y);
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
            gsm.reset();
        }
        Gdx.gl.glClear(16384);
        Gdx.gl.glClearColor(135 / 255f, 206 / 255f, 235 / 255f, 1);
        //Gdx.gl20.glClear(16384);
        samCamPosX = sam.getPosition().x;
        if(samCamPosX < 1.9F) {
            samCamPosX = 1.9F;
        }
        if(samCamPosX > 2.3) {
            samCamPosX = 2.3F;
        }
        this.b2dCam.position.set(samCamPosX, 1.27F, 0);
        this.b2dCam.update();
        if(!sam.gameOver) {
            timeElapsed = TimeUtils.timeSinceMillis(levelTime);
            double printable = (maxTime - timeElapsed) / 1000.0;

            String screenText = "Lives: " + String.valueOf(sam.Lives) + "   Shots: " + String.valueOf(sam.Shots);
            this.sb.begin();
            background.setPosition(this.b2dCam.project(new Vector3(0.0f, 0f, 0)).x, this.b2dCam.project(new Vector3(0f, 0f, 0)).y);
            background.scale(3);
            background.draw(this.sb);

            slingshotSprite.draw(this.sb);
            //bomb_sprite.draw(this.sb);
            //hello.draw(this.sb, "Hello World!", 200,400);

            if (timeOut) {
                sam.gameOver = true;
            } else {
                hello.draw(this.sb, String.format("Time Remaining: %.2f", printable), 80, height - height / 5);
            }

            if(Trolled){
                this.hello.setColor(Color.RED);
                hello.draw(this.sb, String.format("HAHAHAHAHAHAHA", printable), 150, height - height / 5);
                this.hello.setColor(Color.GREEN);
            }
            hello.draw(this.sb, screenText, 80, height - height / 10);

            sam_sprite.draw(this.sb);
            for(GameObject obj: objects){
                obj.render(this.sb);
            }
            this.sb.end();
            //this.b2dr.render(this.world, this.b2dCam.combined);
            this.sam.drawTouchIndicator(shapeRenderer);
        }else{
            Gdx.gl.glClear(16384);
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


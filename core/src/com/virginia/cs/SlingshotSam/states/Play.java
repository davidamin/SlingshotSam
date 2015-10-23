package com.virginia.cs.SlingshotSam.states;

/**
 * Created by Mac on 9/27/2015.
 */

    import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.virginia.cs.SlingshotSam.handlers.GameStateManager;
import com.virginia.cs.SlingshotSam.handlers.MyContactListener;
    import com.badlogic.gdx.audio.Music;
    import com.badlogic.gdx.InputMultiplexer;
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
    import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
    import com.badlogic.gdx.physics.box2d.CircleShape;
    import com.badlogic.gdx.physics.box2d.FixtureDef;
    import com.badlogic.gdx.physics.box2d.PolygonShape;
    import com.badlogic.gdx.physics.box2d.World;
    import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
    import com.badlogic.gdx.utils.TimeUtils;
    import com.badlogic.gdx.utils.Timer;
    import com.sun.org.apache.xpath.internal.operations.Bool;
    import com.virginia.cs.SlingshotSam.handlers.GameStateManager;
    import com.virginia.cs.SlingshotSam.handlers.MyContactListener;
    import com.virginia.cs.SlingshotSam.main.Game;
    import com.virginia.cs.SlingshotSam.main.TouchController;
    import com.virginia.cs.SlingshotSam.states.GameState;

public class Play extends GameState {
    private World world = new World(new Vector2(0.0F, -2.81F), true);
    //private World world = new World(new Vector2(0.0F, 0.0F), true);
    private Box2DDebugRenderer b2dr;
    public OrthographicCamera b2dCam;
    private OrthographicCamera camera;
    private SpriteBatch sb;
    private BitmapFont hello;

    public int height = Gdx.graphics.getHeight();
    public int width = Gdx.graphics.getWidth();

    private ShapeRenderer shapeRenderer;
    private Circle testCircle;
    private TouchController touchController;

    private long timeElapsed;
    private long levelTime;
    private long maxTime;
    private Timer time;
    private Boolean timeOut = false;
    int Lives = 3;
    int Shots = 5;

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

        maxTime = 30000;

        time.schedule(new Timer.Task(){
            public void run(){
                timeOut = true;
            }
        }, maxTime / 1000);

        Music m = Gdx.audio.newMusic(Gdx.files.internal("samSong1.ogg"));
        m.setLooping(true);
        m.play();

        levelTime  = TimeUtils.millis();

        // Set up camera
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        // Set up ShapeRenderer to display test circle
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        // Set Input Processor for app to use TouchController
        // as a source of keyboard input (in case) and as a gesture
        // detector
        touchController = new TouchController();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new GestureDetector(touchController));
        inputMultiplexer.addProcessor(touchController);
        Gdx.input.setInputProcessor(inputMultiplexer);

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
        testCircle = new Circle(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 50, this.world);
        touchController.registerBoundedTouchListener(testCircle);

        this.b2dCam = new OrthographicCamera();
        this.b2dCam.setToOrtho(false, 3.2F, 2.4F);

    }

    public void handleInput() {
    }

    public void update(float dt) {
        this.world.step(dt, 6, 2);
        //timeElapsed += Gdx.graphics.getDeltaTime();
    }

    public void render() {
        Gdx.gl20.glClear(16384);
        timeElapsed = TimeUtils.timeSinceMillis(levelTime);
        double printable = (maxTime - timeElapsed)/ 1000.0;

        String screenText = "Lives: " + String.valueOf(Lives) + "   Shots: " + String.valueOf(Shots);
        this.sb.begin();
        //hello.draw(this.sb, "Hello World!", 200,400);
        hello.draw(this.sb, String.format("Time Remaining: %.2f", printable) , 80,height - height/5);

        if(timeOut){
            hello.draw(this.sb, "Time Over", 500,400);
        }

        hello.draw(this.sb, screenText, 80, height - height/10);
        this.sb.end();

        this.b2dr.render(this.world, this.b2dCam.combined);

        testCircle.draw(shapeRenderer);
    }

    public void dispose() {
    }

    /**
     * Test circle to show the use of the TouchController, specifically
     * a BoundedTouchListener
     */
    public class Circle implements TouchController.BoundedTouchListener {
        private float centerX, centerY;
        private float currentX, currentY;
        private float radius;

        private float velocityX, velocityY;
        private float flingVelocity;
        private float accelX, accelY;
        private float flingAccel;
        private static final float K = 0.00003f;
        private static final float FK = 0.001f;
        private static final float ACCEL_DEC = 0.01f;
        private long lastUpdate;
        private boolean updating = false;

        private Texture text;
        private Sprite sprite;

        private Body body;
        private BodyDef b;
        private FixtureDef f;
        private CircleShape cshape;
        private World w;

        private Vector3 testPoint = new Vector3();

        public Circle(int x, int y, float r, World wrld) {
            text = new Texture(Gdx.files.internal("badlogic.jpg"));
            sprite = new Sprite(text);
            w= wrld;
            cshape = new CircleShape();
            b = new BodyDef();
            f = new FixtureDef();
            cshape.setRadius(.1F);
            b.position.set(1.53F, 2.2F);
            b.type = BodyType.DynamicBody;
            body = w.createBody(b);
            f.shape = cshape;
            f.filter.categoryBits = 8;
            f.filter.maskBits = 2;
            body.createFixture(f).setUserData("touchCircle");
            body.setUserData(sprite);
            setCenter(x, y);
            setCurrent(x, y);
            setRadius(r);
        }

        public void draw(ShapeRenderer renderer) {
            // update current
            updateCurrent();

            // line
            //renderer.setColor(new Color(0.8196f, 0.4121f, 0.5098f, 1.0f));
            //renderer.begin(ShapeRenderer.ShapeType.Line);
            //renderer.line(this.currentX, this.currentY, 0, this.centerX, this.centerY, 0);
            //renderer.end();

            // circle
            //renderer.setColor(new Color(0.4549f, 0.8196f, 0.5098f, 1.0f));
            //renderer.begin(ShapeRenderer.ShapeType.Filled);
            //renderer.circle(this.currentX, this.currentY, this.radius);
            //renderer.end();
        }

        private void updateCurrent() {
            if (!updating) {
                return;
            }
            testPoint.set(body.getPosition().x, body.getPosition().y, 0);
            b2dCam.unproject(testPoint);

            setCurrent(testPoint.x,testPoint.y);

            // Time difference
            //long currentTime = TimeUtils.millis();
            //long timeDiff = currentTime - lastUpdate;

            // Update velocity
            /*this.velocityX += this.accelX * timeDiff;
            this.velocityX *= (1 - ACCEL_DEC);

            this.velocityY += this.accelY * timeDiff;
            this.velocityY *= (1 - ACCEL_DEC);*/

            // Normal vector to center
            /*float perpVectorX = -(centerY - currentY);
            float perpVectorY = centerX - currentX;
            float perpLength = (float) Math.sqrt(Math.pow(perpVectorX, 2) + Math.pow(perpVectorY, 2));
            perpVectorX /= perpLength;
            perpVectorY /= perpLength;*/

            // Projection along normal vector
            /*float flingNormalX = this.flingVelocity * perpVectorX * (1 - ACCEL_DEC);
            float flingNormalY = this.flingVelocity * perpVectorY * (1 - ACCEL_DEC);*/

            // Update current
            /*setCurrent(this.currentX + this.velocityX * timeDiff + flingNormalX * timeDiff,
                    this.currentY + this.velocityY * timeDiff + flingNormalY * timeDiff);*/

            // Update acceleration
            /*accelX = K * (centerX - currentX);
            accelY = K * (centerY - currentY);*/

            // Update time
            //lastUpdate = currentTime;
        }

        private void setCenter(float x, float y) {
            this.centerX = x;
            this.centerY = y;
        }

        private void setCurrent(float x, float y) {
            this.currentX = x;
            this.currentY = y;
        }

        private void setRadius(float r) {
            this.radius = r;
        }

        public boolean isInBounds(int screenX, int screenY) {
            double dist = Math.sqrt(Math.pow(Math.abs(screenX - currentX), 2) +
                    Math.pow(Math.abs(screenX - currentX), 2));
            return (dist <= this.radius);
        }

        public void handleTouchDown(int screenX, int screenY) {
            Gdx.app.log("SlingshotSam", String.format("Bounded Touch Down!\t\t%d, %d", screenX, screenY));
            //Gdx.app.log("SlingshotSam",testPoint.toString());
            updating = false;
            flingVelocity = 0;
            body.applyLinearImpulse(0f,1.3f,body.getWorldCenter().x,body.getWorldCenter().y,true);
        }

        public void handleTouchDragged(int screenX, int screenY) {
            Gdx.app.log("SlingshotSam", String.format("Bounded Touch Dragged!\t\t%d, %d", screenX, screenY));
            setCurrent(screenX, screenY);
        }

        public void handleTouchUp(int screenX, int screenY) {
            Gdx.app.log("SlingshotSam", "Bounded Touch Up!");
            velocityX = 0;
            velocityY = 0;
            accelX = K * (centerX - currentX);
            accelY = K * (centerY - currentY);
            lastUpdate = TimeUtils.millis();
            updating = true;
        }

        public void handleFling(float velocityX, float velocityY) {
            Gdx.app.log("SlingshotSam", String.format("Bounded Fling!\t\t%.2f, %.2f", velocityX, velocityY));

            // Normal vector to center
            float perpVectorX = -(centerY - currentY);
            float perpVectorY = centerX - currentX;
            float perpLength = (float) Math.sqrt(Math.pow(perpVectorX, 2) + Math.pow(perpVectorY, 2));
            perpVectorX /= perpLength;
            perpVectorY /= perpLength;

            // Scalar projection along normal
            this.flingVelocity = (FK * velocityX) * perpVectorX + (FK * velocityY) * perpVectorY;

            this.flingAccel = (float) Math.pow(this.flingVelocity, 2) / this.radius;
        }
    }
}

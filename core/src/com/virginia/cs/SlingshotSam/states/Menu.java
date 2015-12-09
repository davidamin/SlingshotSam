package com.virginia.cs.SlingshotSam.states;

/**
 * Created by Mac on 9/27/2015.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.virginia.cs.SlingshotSam.entities.MovingPlatform;
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

import javax.xml.soap.Text;

public class Menu extends GameState {
    private World world = new World(new Vector2(0.0F, -2.81F), true);
    private Box2DDebugRenderer b2dr;
    public OrthographicCamera b2dCam;
    private Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch sb;
    private BitmapFont hello;
    private Texture texture;
    private Stage stage;
    private TextButton button;
    private TextButton button2;
    private TextButton button3;
    private TextButton.TextButtonStyle textButtonStyle;
    private Skin skin;
    private TextureAtlas buttonAtlas;
    private Texture menu_texture;
    private Sprite menu_sprite;

    private ShapeRenderer shapeRenderer;
    private TouchController touchController;

    private BitmapFont createFont(FreeTypeFontGenerator generator, float dp)
    {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        int fontSize = (int)(dp * Gdx.graphics.getDensity());
        parameter.size = fontSize;
        return generator.generateFont(parameter);
    }

    public Menu(GameStateManager gsm) {
        super(gsm);
        texture = new Texture(Gdx.files.internal("bomb.png"));
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Montserrat-Regular.ttf"));
        this.hello = createFont(generator, 32);
        generator.dispose();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();
        //buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.pack"));
        //skin.addRegions(buttonAtlas);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = this.hello;
        //textButtonStyle.up = new Image(texture).getDrawable();
        //textButtonStyle.down  = new Image(texture).getDrawable();
        //textButtonStyle.checked  = new Image(texture).getDrawable();


        button = new TextButton("Level 1!", textButtonStyle);
        button2 = new TextButton("Level 2!", textButtonStyle);
        button3 = new TextButton("Level 3!", textButtonStyle);
        button.setPosition(0.0f,1000.0f);
        button2.setPosition(0.0f,500.0f);

        stage.addActor(button);
        stage.addActor(button2);
        stage.addActor(button3);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Menu.this.gsm.start1();
            }
        });
        button2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Menu.this.gsm.start2();
            }
        });
        button3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Menu.this.gsm.start3();
            }
        });

        /*Music m = Gdx.audio.newMusic(Gdx.files.internal("samSong2.ogg"));
        m.setLooping(true);
        m.play();*/
        menu_texture = new Texture(Gdx.files.internal("logo.png"));
        menu_sprite = new Sprite(menu_texture);
        menu_sprite.scale((float) 0.0001);


        // Set up camera
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        this.sb = new SpriteBatch();

        this.hello.setColor(Color.GREEN);
        this.world.setContactListener(new ContactListener(){
            public void beginContact(Contact c) {
                Fixture fa = c.getFixtureA();
                Fixture fb = c.getFixtureB();

            }

            public void endContact(Contact c) {
            }

            public void preSolve(Contact c, Manifold m) {
            }

            public void postSolve(Contact c, ContactImpulse ci) {
            }
        });
        this.b2dr = new Box2DDebugRenderer();
        this.menu_sprite.scale(2.0f);

        this.b2dCam = new OrthographicCamera();
        this.b2dCam.setToOrtho(false, 4.2F, 2.4F);

        // Set Input Processor for app to use TouchController
        // as a source of keyboard input (in case) and as a gesture
        // detector
        /*touchController = new TouchController(this.b2dCam);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new GestureDetector(touchController));
        inputMultiplexer.addProcessor(touchController);
        Gdx.input.setInputProcessor(inputMultiplexer);
*/
        // Register sam with touch controller
        //touchController.registerBoundedTouchListener(sam);

        // Set up ShapeRenderer to display test circle
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(this.b2dCam.combined);
    }

    public void handleInput() {
    }

    public void update(float dt) {
        this.world.step(dt, 6, 2);
        //timeElapsed += Gdx.graphics.getDeltaTime();
    }

    public void render() {
        Gdx.gl.glClear(16384);
        Gdx.gl.glClearColor(0 / 255f, 0 / 255f, 0 / 255f, 1);
        //Gdx.gl20.glClear(16384);
        stage.draw();
        this.b2dCam.update();
        this.menu_sprite.setPosition(1000f,500f);
        this.sb.begin();
            this.menu_sprite.draw(this.sb);
        this.sb.end();
    }

    public void dispose() {
    }
}

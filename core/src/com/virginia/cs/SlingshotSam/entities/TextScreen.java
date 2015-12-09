package com.virginia.cs.SlingshotSam.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Kwon on 12/8/2015.
 */
public class TextScreen {
    private Texture t;
    private BodyDef bDef;
    private Body b;
    private FixtureDef fDef;
    private float x_off;
    private float y_off;
    private BitmapFont hi;
    private SpriteBatch sb;
    private String text;

    public TextScreen(String textureFile, float x, float y, float length, float height, String userdata, World w, float scale, float x_offset, float y_offset, String text){
        bDef = new BodyDef();
        bDef.position.set(x, y);
        bDef.type = BodyDef.BodyType.StaticBody;

        this.text=text;
        b = w.createBody(bDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(length, height);

        fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.filter.categoryBits = 2;
        fDef.filter.maskBits = 12;
        b.createFixture(fDef).setUserData(userdata);

        t = new Texture(Gdx.files.internal(textureFile));
        x_off = x_offset;
        y_off = y_offset;
    }

    public void setText(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Montserrat-Regular.ttf"));
        this.hi=createFont(generator, 16);
        generator.dispose();
        this.hi.setColor(Color.GREEN);
        this.sb=new SpriteBatch();
        this.sb.begin();
        hi.draw(this.sb,String.format(text), 0.8f, 0.8f);
    }

    private BitmapFont createFont(FreeTypeFontGenerator generator, float dp){
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        int fontSize = (int)(dp * Gdx.graphics.getDensity());
        parameter.size = fontSize;
        return generator.generateFont(parameter);
    }

}

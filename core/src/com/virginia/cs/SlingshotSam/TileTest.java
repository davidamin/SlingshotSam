package com.virginia.cs.SlingshotSam;

//import android.util.Log;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;


public class TileTest extends ApplicationAdapter implements InputProcessor {
    Texture img;
    TiledMap tiledMap;
    OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;
    SpriteBatch sb;
    Texture texture;
    Sprite sprite;

    @Override
    public void create () {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false,w,h);
        camera.update();
        tiledMap = new TmxMapLoader().load("WesternKeep.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        Gdx.input.setInputProcessor(this);
        sb = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("sam.png"));
        sprite = new Sprite(texture);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sprite.draw(sb);
        sb.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {return false;}

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {return false;}

    private Vector2 lastTouch = new Vector2();

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        lastTouch.set(screenX, screenY);
        Vector3 clickCoordinates = new Vector3(screenX,screenY,0);
        Vector3 position = camera.unproject(clickCoordinates);
        if(!hasProperty("Unpassable",position))
            sprite.setPosition(position.x, position.y);
        if(hasProperty("Fire",position))
        {
            texture = new Texture(Gdx.files.internal("bomb.png"));
            sprite.setTexture(texture);
        }
        return false;
    }
    public boolean hasProperty(String propertyName, Vector3 position){
        boolean result=false;
        for(int height=tiledMap.getLayers().getCount()-1;height>=0;height--){
            TiledMapTileLayer.Cell cell = ((TiledMapTileLayer)tiledMap.getLayers().get(height)).getCell((int) Math.floor(position.x / 32), (int) Math.floor(position.y / 32));
            if(cell!=null) {
                Object property = cell.getTile().getProperties().get(propertyName);
                if (property != null && Boolean.parseBoolean((String) property))
                    result = true;
            }
        }
        return result;
    }

    public ArrayList<TiledMapTileLayer.Cell> getCells(String propertyName) {
        ArrayList<TiledMapTileLayer.Cell> result = new ArrayList<TiledMapTileLayer.Cell>();
        for (int height = tiledMap.getLayers().getCount() - 1; height >= 0; height--) {
            TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(height);
            for (int x = 0; x < layer.getWidth(); x++) {
                for (int y = 0; y < layer.getHeight(); y++) {
                    TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                    if (cell != null) {
                        Object property = cell.getTile().getProperties().get(propertyName);
                        if (property != null && Boolean.parseBoolean((String) property))
                            result.add(cell);
                    }
                }
            }
        }
        return result;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 newTouch = new Vector2(screenX, screenY);
        // delta will now hold the difference between the last and the current touch positions
        // delta.x > 0 means the touch moved to the right, delta.x < 0 means a move to the left
        Vector2 delta = newTouch.cpy().sub(lastTouch);
        delta.x=-1*delta.x;
        camera.translate(delta);
        lastTouch = newTouch;
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        log("mouseMoved");
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        log("scrolled");
        return false;
    }

    public void log(String str){
//        Log.i("TouchTest", str);
    }
}
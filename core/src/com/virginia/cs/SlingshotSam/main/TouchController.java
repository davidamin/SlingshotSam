package com.virginia.cs.SlingshotSam.main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for Touch-based input and on-screen Gestures
 */
public class TouchController implements InputProcessor, GestureListener {

    private static final boolean LOG_INFO = false;
    private final Map<Integer, SingleTouchEventInfo> buttonTouchMap = new HashMap<Integer, SingleTouchEventInfo>();
    private final Map<Integer, SingleTouchEventInfo> pointerTouchMap = new HashMap<Integer, SingleTouchEventInfo>();

    private final List<BoundedTouchListener> boundedTouchListeners = new ArrayList<BoundedTouchListener>();
    private final List<BoundedTouchListener> touchDownBoundedListeners = new ArrayList<BoundedTouchListener>();


    /**
     * Constructor to create an instance of the TouchController
     */
    public TouchController() {
        super();
        this.buttonTouchMap.clear();
        this.pointerTouchMap.clear();
    }

    /**
     * Helper method so that logging can be easily turned on/off
     * @param tag Tag for the logging (can be filtered by tag)
     * @param message Message to be logged
     */
    private void log(String tag, String message) {
        if (LOG_INFO) {
            Gdx.app.log(tag, message);
        }
    }

    /**
     * Add bounded touch listener to list (adds regardless of duplicates)
     * @param listener BoundedTouchListener to be added
     * @return success
     */
    public boolean registerBoundedTouchListener(BoundedTouchListener listener) {
        return this.boundedTouchListeners.add(listener);
    }

    /**
     * Remove bounded touch listener (make sure you have an equals() override)
     * @param listener BoundedTouchListener to be removed
     * @return success
     */
    public boolean removeBoundedTouchListener(BoundedTouchListener listener) {
        return this.boundedTouchListeners.remove(listener);
    }

    private void touchDownForBoundedTouchListeners(int screenX, int screenY) {
        this.touchDownBoundedListeners.clear();
        for (BoundedTouchListener touchListener : boundedTouchListeners) {
            if (touchListener.isInBounds(screenX, screenY)) {
                this.touchDownBoundedListeners.add(touchListener);
                touchListener.handleTouchDown(screenX, screenY);
            }
        }
    }

    private void touchDraggedForBoundedTouchListeners(int screenX, int screenY) {
        for (BoundedTouchListener touchListener : this.touchDownBoundedListeners) {
            touchListener.handleTouchDragged(screenX, screenY);
        }
    }

    private void touchUpForBoundedTouchListeners(int screenX, int screenY) {
        for (BoundedTouchListener touchListener : this.touchDownBoundedListeners) {
            touchListener.handleTouchUp(screenX, screenY);
        }
    }

    private void flingForBoundedTouchListeners(float velocityX, float velocityY) {
        for (BoundedTouchListener touchListener : this.touchDownBoundedListeners) {
            touchListener.handleFling(velocityX, velocityY);
        }
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        log("SlingshotSam/TouchController", String.format("touchDown() x: %.3f y: %.3f pointer: %d button: %d",
                x, y, pointer, button));
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        log("SlingshotSam/TouchController", String.format("touchDown() screenX: %d screenY: %d " +
                "pointer: %d button: %d", screenX, screenY, pointer, button));

        // Create a new SingleTouchEventInfo
        SingleTouchEventInfo touchEvent = new SingleTouchEventInfo(pointer, button);
        touchEvent.addTouchDown(screenX, screenY);

        // Add to associated maps
        this.buttonTouchMap.put(button, touchEvent);
        this.pointerTouchMap.put(pointer, touchEvent);

        // Could call function in Game or something...

        // BoundedTouchListeners
        touchDownForBoundedTouchListeners(screenX, screenY);

        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        log("SlingshotSam/TouchController", String.format("tap() x: %.3f y: %.3f count: %d button: %d",
                x, y, count, button));
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        log("SlingshotSam/TouchController", String.format("longPress() x: %.3f y: %.3f",
                x, y));
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        log("SlingshotSam/TouchController", String.format("fling() velocityX: %.3f velocityY: %.3f button: %d",
                velocityX, velocityY, button));

        // Get SingleTouchEventInfo
        SingleTouchEventInfo touchEvent = buttonTouchMap.get(button);
        if (touchEvent != null) {
            touchEvent.addFling(velocityX, velocityY);
        }

        // BoundedTouchListeners
        flingForBoundedTouchListeners(velocityX, velocityY);

        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        log("SlingshotSam/TouchController", String.format("pan() x: %.3f y: %.3f deltaX: %.3f deltaY: %.3f",
                x, y, deltaX, deltaY));
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        log("SlingshotSam/TouchController", String.format("panStop() x: %.3f y: %.3f pointer: %d button: %d",
                x, y, pointer, button));
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        log("SlingshotSam/TouchController", String.format("zoom() initialDistance: %.3f distance: %.3f",
                initialDistance, distance));
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        log("SlingshotSam/TouchController", String.format("pinch() initialPointer1: %s initialPointer2 %s " +
                        "pointer1: %s pointer2: %s", initialPointer1.toString(), initialPointer2.toString(),
                pointer1.toString(), pointer2.toString()));
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        log("SlingshotSam/TouchController", String.format("keyDown() keycode: %d",
                keycode));
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        log("SlingshotSam/TouchController", String.format("keyUp() keycode: %d",
                keycode));
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        log("SlingshotSam/TouchController", String.format("keyTyped() keycode: %c",
                character));
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        log("SlingshotSam/TouchController", String.format("touchUp() screenX: %d screenY: %d " +
                "pointer: %d button: %d", screenX, screenY, pointer, button));

        // Get SingleTouchEventInfo
        SingleTouchEventInfo touchEvent = this.pointerTouchMap.get(pointer);

        // If not in pointer map, try button map
        if (touchEvent == null) {
            touchEvent = this.buttonTouchMap.get(button);
        }

        // If not null, add the touch up
        if (touchEvent != null) {
            touchEvent.addTouchUp(screenX, screenY);
        }

        // BoundedTouchListeners
        touchUpForBoundedTouchListeners(screenX, screenY);

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        log("SlingshotSam/TouchController", String.format("touchDragged() screenX: %d screenY: %d " +
                "pointer: %d", screenX, screenY, pointer));

        // Get SingleTouchEventInfo
        SingleTouchEventInfo touchEvent = this.pointerTouchMap.get(pointer);

        // If not null, add the touch up
        if (touchEvent != null) {
            touchEvent.addTouchDragged(screenX, screenY);
        }

        // BoundedTouchListeners
        touchDraggedForBoundedTouchListeners(screenX, screenY);

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        log("SlingshotSam/TouchController", String.format("mouseMoved() screenX: %d screenY: %d ",
                screenX, screenY));
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        log("SlingshotSam/TouchController", String.format("scrolled() amount: %d", amount));
        return false;
    }

    /**
     * Structure used to collect information on a single touch event
     * following the control flow:
     *      TouchDown -> TouchDragged -> Fling -> TouchUp
     *
     * Pass the SingleTouchEventInfo to the Game from the TouchController's
     * touchDown() method and then the TouchController will update the available
     * information for it.
     */
    public class SingleTouchEventInfo {
        private int pointer;
        private int button;

        private boolean touchDown = false;
        private ScreenXY touchDownScreenXY;

        private boolean touchDragged = false;
        private ScreenXY touchDraggedScreenXY;

        private boolean fling = false;
        private VelocityXY flingVelocityXY;

        private boolean touchUp = false;
        private ScreenXY touchUpScreenXY;

        public SingleTouchEventInfo(int pointer, int button) {
            this.pointer = pointer;
            this.button = button;
        }

        public void addTouchDown(int screenX, int screenY) {
            this.touchDown = true;
            this.touchDownScreenXY = new ScreenXY(screenX, screenY);
        }

        public boolean hasTouchDown() {
            return this.touchDown;
        }

        public ScreenXY getTouchDown() {
            if (!this.touchDown) {
                return null;
            }

            this.touchDown = false;
            return this.touchDownScreenXY;
        }

        public void addTouchDragged(int screenX, int screenY) {
            this.touchDragged = true;
            this.touchDraggedScreenXY = new ScreenXY(screenX, screenY);
        }

        public boolean hasTouchDragged() {
            return this.touchDragged;
        }

        public ScreenXY getTouchDragged() {
            if (!this.touchDragged) {
                return null;
            }

            this.touchDragged = false;
            return this.touchDraggedScreenXY;
        }

        public void addFling(float velocityX, float velocityY) {
            this.fling = true;
            this.flingVelocityXY = new VelocityXY(velocityX, velocityY);
        }

        public boolean hasFling() {
            return this.fling;
        }

        public VelocityXY getFling() {
            if (!this.fling) {
                return null;
            }

            this.fling = false;
            return this.flingVelocityXY;
        }

        public void addTouchUp(int screenX, int screenY) {
            this.touchUp = true;
            this.touchUpScreenXY = new ScreenXY(screenX, screenY);
        }

        public boolean hasTouchUp() {
            return this.touchUp;
        }

        public ScreenXY getTouchUp() {
            if (!this.touchUp) {
                return null;
            }

            this.touchUp = false;
            return this.touchUpScreenXY;
        }
    }

    /**
     * 2D vector for Screen positions
     */
    public class ScreenXY {
        private int screenX;
        private int screenY;

        public ScreenXY(int screenX, int screenY) {
            this.screenX = screenX;
            this.screenY = screenY;
        }

        public int getScreenX() {
            return this.screenX;
        }

        public int getScreenY() {
            return this.screenY;
        }
    }

    /**
     * 2D vector for Velocity
     */
    public class VelocityXY {
        private float velocityX;
        private float velocityY;

        public VelocityXY(float velocityX, float velocityY) {
            this.velocityX = velocityX;
            this.velocityY = velocityY;
        }

        public float getVelocityX() {
            return this.velocityX;
        }

        public float getVelocityY() {
            return this.velocityY;
        }
    }

    /**
     * A listener for bounded (in terms of screen position) touch
     * events. The TouchController will allow these listeners to
     * register and receive updates if touch event is within screen
     * bounds.
     */
    public interface BoundedTouchListener {
        boolean isInBounds(int screenX, int screenY);
        void handleTouchDown(int screenX, int screenY);
        void handleTouchDragged(int screenX, int screenY);
        void handleTouchUp(int screenX, int screenY);
        void handleFling(float velocitX, float velocityY);
    }
}

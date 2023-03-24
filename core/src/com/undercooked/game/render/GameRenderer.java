package com.undercooked.game.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.sun.org.apache.bcel.internal.Const;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.Cook;
import com.undercooked.game.entity.CookController;
import com.undercooked.game.entity.Entity;
import com.undercooked.game.logic.GameLogic;
import com.undercooked.game.map.MapCell;
import com.undercooked.game.map.MapEntity;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.StringUtil;

import java.util.Comparator;

public class GameRenderer {
    private GameLogic logic;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Array<Entity> renderEntities;

    private OrthographicCamera worldCamera;

    private OrthographicCamera uiCamera;
    private Sprite interactSprite;
    private Comparator<Entity> entityCompare = new Comparator<Entity>() {
        @Override
        public int compare(Entity o1, Entity o2) {
            if (o1.getY() > o2.getY()) {
                return -1;
            }
            if (o2.getY() > o1.getY()) {
                return 1;
            }
            return 0;
        }
    };

    public GameRenderer(GameLogic logic, SpriteBatch batch, ShapeRenderer shape, BitmapFont font, Array renderEntities) {
        this.logic = logic;
        this.batch = batch;
        this.shape = shape;
        this.font = font;
        this.renderEntities = renderEntities;
        this.interactSprite = null;

        this.worldCamera = CameraController.getCamera(Constants.WORLD_CAMERA_ID);
        this.uiCamera = CameraController.getCamera(Constants.UI_CAMERA_ID);
    }

    public GameRenderer() {
        this(null, MainGameClass.batch, MainGameClass.shapeRenderer, MainGameClass.font);
    }

    public GameRenderer(GameLogic logic, SpriteBatch batch, ShapeRenderer shape, BitmapFont font) {
        this(logic, batch, shape, font, new Array<Entity>());
    }

    public void setLogic(GameLogic logic) {
        this.logic = logic;
    }

    public void setSpriteBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public void setShapeRenderer(ShapeRenderer shape) {
        this.shape = shape;
    }

    public void moveCamera(float delta) {
        CookController cookController = logic.getCookController();
        if (cookController.getCurrentCook() != null) {
            if (Math.abs(worldCamera.position.x - cookController.getCurrentCook().collision.x) < 2
                    && Math.abs(worldCamera.position.y - cookController.getCurrentCook().collision.y) < 2) {
                worldCamera.position.x = cookController.getCurrentCook().collision.x;
                worldCamera.position.y = cookController.getCurrentCook().collision.y;
            } else {
                Vector3 target = new Vector3(cookController.getCurrentCook().collision.x, cookController.getCurrentCook().collision.y, 0);
                worldCamera.position.lerp(target, .9f * delta * 2);
            }
        }
    }

    public void render(float delta) {
        // Clear the Screen
        ScreenUtils.clear(0, 0, 0, 0);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // First set up the camera for the world
        MainGameClass.shapeRenderer.setProjectionMatrix(worldCamera.combined);
        MainGameClass.batch.setProjectionMatrix(worldCamera.combined);

        // Move the world camera
        worldCamera.update();

        // Render the floor of the map




        // Get the interaction target
        Cook currentCook = logic.getCookController().getCurrentCook();
        MapCell interactTarget = null;
        if (currentCook != null) {
            interactTarget = currentCook.getInteractTarget();
        }

        // Render the entities in order, highest Y to lowest Y
        renderEntities.sort(entityCompare);
        batch.begin();
        for (Entity renderEntity : renderEntities) {
            renderEntity.draw(batch);

            // Draw the selected cook's interact target. It draws on
            // the same order as the station, so that the cooks can
            // be rendered over it.
            // Check if the current entity is the interactTarget
            if (interactTarget != null && interactTarget.getMapEntity() == renderEntity) {
                // If it is, then draw it.
                MapEntity interactEntity = interactTarget.getMapEntity();
                Rectangle interactBox = interactEntity.collision;
                interactSprite.setSize(interactBox.width,interactBox.height);
                interactSprite.setPosition(interactBox.x, interactBox.y);
                interactSprite.draw(batch);
            }
        }
        batch.end();

        // Draw debug
        //shape.begin();
        //logic.getMap().drawDebug(shape);

        // Render all the entities' debug
        /*for (Entity renderEntity : renderEntities) {
            renderEntity.drawDebug(shape);
        }*/
        //shape.end();






        // Following that, render the UI
        shape.setProjectionMatrix(uiCamera.combined);
        batch.setProjectionMatrix(uiCamera.combined);

        CookController cookController = logic.getCookController();
        for (int i = 0; i < cookController.getCooks().size; i++) {
            if (i == cookController.getCurrentCookIndex()) {
                shape.setAutoShapeType(true);
                shape.begin(ShapeRenderer.ShapeType.Line);

                shape.setColor(Color.GREEN);
                shape.rect(Constants.V_WIDTH - (i+1) * 128,
                        Constants.V_HEIGHT - 128 - 8, 128, 128);
                shape.end();
            }
            batch.begin();
            cookController.getCooks().get(i).draw_top(batch, Constants.V_WIDTH-128*(i+1), Constants.V_HEIGHT-128);
            batch.end();
        }

        batch.begin();
        font.getData().setScale(1F);
        font.draw(batch, "Time in s: " + StringUtil.formatSeconds(logic.getElapsedTime()),
                500, 500);
        batch.end();
    }

    public void load(TextureManager textureManager) {
        textureManager.load("interactions/select_box.png");
    }

    public void postLoad(TextureManager textureManager) {
        interactSprite = new Sprite(textureManager.get("interactions/select_box.png"));
    }

    public void unload(TextureManager textureManager) {
        textureManager.unload("interactions/select_box.png");
    }

    public void addEntity(Entity entity) {
        // Only add the entity if it's not there already
        if (!renderEntities.contains(entity, true)) {
            renderEntities.add(entity);
        }
    }

    public void removeEntity(Entity entity) {
        renderEntities.removeValue(entity,true);
    }

    public Array<Entity> getEntities() {
        return renderEntities;
    }
}

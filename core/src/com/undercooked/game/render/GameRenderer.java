package com.undercooked.game.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.entity.cook.CookController;
import com.undercooked.game.entity.Entity;
import com.undercooked.game.food.Instruction;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.Request;
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
    private GlyphLayout text;
    private Sprite interactSprite;
    private Texture moneyTex;
    private Texture reputationTex;

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
        batch.begin();
        logic.getMap().drawGround(batch);
        batch.end();



        // Get the interaction target
        Cook currentCook = logic.getCookController().getCurrentCook();
        MapCell interactTarget = null;
        if (currentCook != null) {
            interactTarget = currentCook.getInteractTarget();
        }

        // Render the entities in order, highest Y to lowest Y
        renderEntities.sort(entityCompare);
        for (Entity renderEntity : renderEntities) {
            batch.begin();
            renderEntity.draw(batch);
            batch.end();

            // Draw the selected cook's interact target. It draws on
            // the same order as the station, so that the cooks can
            // be rendered over it.
            // Check if the current entity is the interactTarget
            if (interactTarget != null && interactTarget.getMapEntity() == renderEntity) {
                // If it is, then draw it.
                MapEntity interactEntity = interactTarget.getMapEntity();
                Rectangle interactBox = interactEntity.getInteractBox();
                // If the station target is null, then draw it red.
                // if it's disabled or a phone, draw it green to show it can be bought.
                // Otherwise, draw it yellow.
                if (currentCook.getStationTarget() == null) {
                    interactSprite.setColor(Color.RED);
                } else if (currentCook.getStationTarget().isDisabled() || currentCook.getStationTarget().getID().equals(Constants.PHONE_ID)) {
                    interactSprite.setColor(Color.GREEN);
                    // If this is the case, also draw the price of the station.
                    font.getData().setScale(0.6f);
                    int price = -1;
                    // Depending on if it's disabled, or a phone, change which price to use
                    if (currentCook.getStationTarget().isDisabled()) {
                        price = currentCook.getStationTarget().getPrice();
                    } else if (currentCook.getStationTarget().getID().equals(Constants.PHONE_ID)) {
                        price = logic.getCookCost();
                    }
                    // Only draw the price if it's >= 0
                    if (price >= 0) {
                        text.setText(font, String.format("%.2f", price / 100f));
                        float drawX = interactBox.x + interactBox.width / 2f - text.width / 2f,
                                drawY = interactBox.y + interactBox.height / 2f;
                        shape.begin(ShapeRenderer.ShapeType.Filled);
                        shape.rect(drawX - 5, drawY - text.height - 5, text.width + 10, text.height + 10);
                        shape.end();
                        batch.begin();
                        font.draw(batch, text, drawX, drawY);
                        batch.end();
                    }
                }
                else {
                    interactSprite.setColor(Color.YELLOW);
                }
                interactSprite.setSize(interactBox.width,interactBox.height);
                interactSprite.setPosition(interactBox.x, interactBox.y);
                batch.begin();
                interactSprite.draw(batch);
                batch.end();
            }

            shape.begin(ShapeRenderer.ShapeType.Filled);
            renderEntity.draw(shape);
            shape.end();
            batch.begin();
            renderEntity.drawPost(batch);
            batch.end();
        }

        batch.begin();
        // Draw the select_box for displayCustomer
        if (logic.getDisplayCustomer() != null) {
            MapCell registerCell = logic.getDisplayCustomer().getRegister().getRegisterCell();
            interactSprite.setColor(Color.PURPLE);
            Rectangle registerBox = registerCell.getMapEntity().getInteractBox();
            interactSprite.setSize(registerBox.width, registerBox.height);
            interactSprite.setPosition(registerBox.x, registerBox.y);
            interactSprite.draw(batch);
        }

        // Render the Customers
        logic.getCustomerController().draw(batch);
        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        logic.getCustomerController().draw(shape);
        shape.end();

        // Draw debug
        // renderDebug(delta);






        // Following that, render the UI
        shape.setProjectionMatrix(uiCamera.combined);
        batch.setProjectionMatrix(uiCamera.combined);

        //// Render the Cook's heads in the top right of the screen
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






        //// Render the money and reputation in the bottom right
        font.getData().setScale(1f);
        final float texSize = 64f;
        // Draw the background
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.DARK_GRAY);
        shape.rect(Constants.V_WIDTH-350-texSize,0,350,100);
        shape.end();

        // Draw the reputation
        batch.begin();

        text.setText(font, Integer.toString(logic.getReputation()));
        batch.draw(reputationTex, Constants.V_WIDTH-360-texSize/2f, 50-texSize/2f, texSize, texSize);
        font.draw(batch, text, Constants.V_WIDTH-310, 64);

        // Draw the money
        String money = String.format("%.2f", logic.getMoney()/100f);;
        text.setText(font, money);
        batch.draw(moneyTex, Constants.V_WIDTH-200-texSize/2f, 50-texSize/2f, texSize, texSize);
        font.draw(batch, text, Constants.V_WIDTH-150, 64);
        batch.end();






        //// Render the recipe instructions in the bottom left.
        if (logic.getDisplayCustomer() != null) {
            Request request = logic.getDisplayCustomer().getRequest();
            Array<Instruction> instructions = request.getInstructions();


            float size = 64;
            float startY = size * instructions.size;

            // First, display the request item at the top of them all
            Item requestItem = logic.getItems().getItem(request.itemID);

            // Draw a box underneath the request, to make it more obvious that
            // it's not a part of the request.
            text.setText(font, requestItem.name);
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(Color.DARK_GRAY);
            shape.rect(0, startY, 84 + text.width, 64);
            shape.end();

            // Draw the request
            batch.begin();
            drawInstruction(requestItem.sprite.getTexture(), text, startY, size);

            // Then display the instructions underneath.
            for (int i = 0 ; i < instructions.size ; i++) {
                Instruction instruction = instructions.get(i);
                text.setText(font, instruction.text);
                drawInstruction(instruction.getTexture(), text, startY-size*(i+1), size);
            }

            batch.end();
        }

        String timerText = "Time: " + StringUtil.formatSeconds(logic.getElapsedTime());
        font.getData().setScale(1F);
        text.setText(font, timerText);
        float timerX = Constants.V_WIDTH/2;
        float textStart = timerX - text.width/2;

        shape.setColor(Color.DARK_GRAY);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.rect(textStart-10,Constants.V_HEIGHT-60, text.width+20,60);
        shape.end();

        batch.begin();
        font.draw(batch, text,
                textStart, Constants.V_HEIGHT-20);
        batch.end();
    }

    public void drawInstruction(Texture texture, GlyphLayout text, float y, float size) {
        font.getData().setScale(0.8F);
        font.draw(batch, text, size + 16, y + 42);
        if (texture != null) {
            batch.draw(texture, 8, y, size, size);
        }
    }

    public void renderDebug(float delta) {
        shape.begin();
        logic.getMap().drawDebug(shape);

        // Render all the entities' debug
        for (Entity renderEntity : renderEntities) {
            renderEntity.drawDebug(shape);
        }
        shape.end();
    }

    public void load(TextureManager textureManager) {
        textureManager.load("interactions/select_box.png");
        textureManager.load("uielements/reputation.png");
        textureManager.load("uielements/money.png");
    }

    public void postLoad(TextureManager textureManager) {
        interactSprite = new Sprite(textureManager.get("interactions/select_box.png"));
        moneyTex = textureManager.get("uielements/money.png");
        reputationTex = textureManager.get("uielements/reputation.png");
        text = new GlyphLayout();
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

    public void addEntities(Array<?> entities) {
        for (Object entityToRemove : entities) {
            addEntity((Entity) entityToRemove);
        }
    }

    public void removeEntity(Entity entity) {
        if (entity == null) return;
        renderEntities.removeValue(entity,true);
    }

    public void removeEntities(Array<Entity> removedEntities) {
        for (Entity entityToRemove : removedEntities) {
            removeEntity(entityToRemove);
        }
    }

    public Array<Entity> getEntities() {
        return renderEntities;
    }
}

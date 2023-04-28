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

/**
 * The class used to render the game visually.
 */
public class GameRenderer {
    /** The {@link GameLogic} to render. */
    protected GameLogic logic;

    /** The {@link SpriteBatch} to use for {@link Texture} rendering. */
    protected SpriteBatch batch;

    /** The {@link ShapeRenderer} to use for shape rendering. */
    protected ShapeRenderer shape;

    /** The {@link BitmapFont} to use for font rendering. */
    protected BitmapFont font;

    /** An {@link Array} of all {@link Entity}s to render in the game. */
    protected Array<Entity> renderEntities;

    /** The {@link OrthographicCamera} to draw to the world. */
    protected OrthographicCamera worldCamera;

    /** The {@link OrthographicCamera} to draw to the UI.  */
    protected OrthographicCamera uiCamera;

    /** The {@link GlyphLayout} for the text display. */
    protected GlyphLayout text;

    /** The {@link Sprite} for the select box image. */
    protected Sprite interactSprite;

    /** The {@link Texture} for the money icon in the bottom right. */
    protected Texture moneyTex;

    /** The {@link Texture} for the reputation icon in the bottom right. */
    protected Texture reputationTex;

    /** Camera {@link Vector3} for calculations, to avoid creating an instance every frame. */
    protected Vector3 cameraVector;

    /**
     * The comparator for drawing the {@link Entity}s in the {@link #renderEntities}
     * earlier if they're higher up so they visually appear lower.
     */
    private final Comparator<Entity> entityCompare = new Comparator<Entity>() {
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

    /**
     * Constructor for the {@link GameRenderer} that sets up
     * the variables, and stores the provided arguments.
     * @param logic {@link GameLogic} : The {@link GameLogic} to render.
     * @param batch {@link SpriteBatch} : The {@link SpriteBatch} to use.
     * @param shape {@link ShapeRenderer} : The {@link ShapeRenderer} to use.
     * @param font {@link BitmapFont} : The {@link BitmapFont} to use.
     * @param renderEntities {@link Array<Entity>} : An {@link Array} of the {@link Entity}s
     *                                               to have as the {@link #renderEntities}
     *                                               at the start.
     */
    public GameRenderer(GameLogic logic, SpriteBatch batch, ShapeRenderer shape, BitmapFont font, Array<Entity> renderEntities) {
        this.logic = logic;
        this.batch = batch;
        this.shape = shape;
        this.font = font;
        this.renderEntities = renderEntities;
        this.interactSprite = null;
        this.cameraVector = new Vector3();

        this.worldCamera = CameraController.getCamera(Constants.WORLD_CAMERA_ID);
        this.uiCamera = CameraController.getCamera(Constants.UI_CAMERA_ID);
    }

    /**
     * Constructor for the {@link GameRenderer} with no arguments.
     * <br>Uses the default {@link MainGameClass#batch}, {@link MainGameClass#shapeRenderer}
     * and {@link MainGameClass#font}.
     */
    public GameRenderer() {
        this(null, MainGameClass.batch, MainGameClass.shapeRenderer, MainGameClass.font);
    }

    /**
     * Constructor for the {@link GameRenderer} with no {@code renderEntities} parameter.
     * @param logic {@link GameLogic} : The {@link GameLogic} to render.
     * @param batch {@link SpriteBatch} : The {@link SpriteBatch} to use.
     * @param shape {@link ShapeRenderer} : The {@link ShapeRenderer} to use.
     * @param font {@link BitmapFont} : The {@link BitmapFont} to use.
     */
    public GameRenderer(GameLogic logic, SpriteBatch batch, ShapeRenderer shape, BitmapFont font) {
        this(logic, batch, shape, font, new Array<Entity>());
    }

    /**
     * Set the {@link GameLogic} that the {@link GameRenderer} will draw.
     * @param logic {@link GameLogic} : The {@link GameLogic} to render.
     */
    public void setLogic(GameLogic logic) {
        this.logic = logic;
    }

    /**
     * Set the {@link SpriteBatch} that the {@link GameRenderer} will use
     * to draw {@link Texture}s.
     * @param batch {@link SpriteBatch} : The {@link SpriteBatch} to use.
     */
    public void setSpriteBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    /**
     * Set the {@link ShapeRenderer} that the {@link GameRenderer} will use
     * to draw shapes.
     * @param shape {@link ShapeRenderer} : The {@link ShapeRenderer} to use.
     */
    public void setShapeRenderer(ShapeRenderer shape) {
        this.shape = shape;
    }

    /**
     * Move the {@link #worldCamera} towards the target
     * {@code x} and {@code y}.
     * @param delta {@code float} : The time since the last frame.
     * @param x {@code x} : The {@code x} target of the camera.
     * @param y {@code y} : The {@code y} target of the camera.
     */
    public void moveCamera(float delta, float x, float y) {
        if (Math.abs(worldCamera.position.x - x) < 2
                && Math.abs(worldCamera.position.y - y) < 2) {
            worldCamera.position.x = x;
            worldCamera.position.y = y;
        } else {
            cameraVector.x = x;
            cameraVector.y = y;
            float startX = worldCamera.position.x;
            float startY = worldCamera.position.y;
            worldCamera.position.lerp(cameraVector, .9f * delta * 2);

            //// Check if the camera overshot
            // X
            if (Math.abs(worldCamera.position.x - startX) >= Math.abs(cameraVector.x - startX)) {
                worldCamera.position.x = x;
            }
            // Y
            if (Math.abs(worldCamera.position.y - startY) >= Math.abs(cameraVector.y - startY)) {
                worldCamera.position.y = y;
            }
        }

    }

    /**
     * Move the {@link #worldCamera} towards the target
     * {@link Entity}.
     * @param delta {@code float} : The time since the last frame.
     * @param targetEntity {@link Entity} : The {@link Entity} to target.
     */
    public void moveCamera(float delta, Entity targetEntity) {
        if (targetEntity == null) return;
        // If the target is null, just ignore
        moveCamera(delta, targetEntity.collision.x, targetEntity.collision.y);
    }

    /**
     * A function to be overridden by children
     * @param entity {@link Entity} : The {@link Entity} to render.
     */
    public void renderEntity(Entity entity) {

    }

    /**
     * Function called to render the world.
     * @param delta {@code float} : The time since the last frame
     */
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

        MapCell registerCell = null;
        if (logic.getDisplayCustomer() != null) {
            registerCell = logic.getDisplayCustomer().getRegister().getRegisterCell();
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
                } else {
                    interactSprite.setColor(Color.YELLOW);
                }
                interactSprite.setSize(interactBox.width, interactBox.height);
                interactSprite.setPosition(interactBox.x, interactBox.y);
                batch.begin();
                interactSprite.draw(batch);
                batch.end();
            }
            // If it's not that, then check if it's the displayCustomer register cell
            // Draw the select_box for displayCustomer
            if (registerCell != null && registerCell.getMapEntity() == renderEntity) {
                interactSprite.setColor(Color.PURPLE);
                Rectangle registerBox = registerCell.getMapEntity().getInteractBox();
                interactSprite.setSize(registerBox.width, registerBox.height);
                interactSprite.setPosition(registerBox.x, registerBox.y);
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

            // Call the render entity function
            renderEntity(renderEntity);
        }

        batch.begin();
        // Render the Customers
        logic.getCustomerController().draw(batch);
        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        logic.getCustomerController().draw(shape);
        shape.end();

        // Draw debug
        // renderDebug(delta);

    }

    /**
     * Function called to render the ui.
     * @param delta {@code float} : The time since the last frame
     */
    public void renderUI(float delta) {shape.setProjectionMatrix(uiCamera.combined);
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
        float timerX = Constants.V_WIDTH/2f;
        float textStart = timerX - text.width/2f;

        shape.setColor(Color.DARK_GRAY);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.rect(textStart-10,Constants.V_HEIGHT-60, text.width+20,60);
        shape.end();

        batch.begin();
        font.draw(batch, text,
                textStart, Constants.V_HEIGHT-20);
        batch.end();
    }

    /**
     * Function to draw an instruction on the bottom right of the screen.
     * @param texture {@link Texture} : The instruction's icon.
     * @param text {@link String} : The text to display.
     * @param y {@code float} : The {@code y} to draw at.
     * @param size {@link float} : The size of the sprite.
     */
    protected void drawInstruction(Texture texture, GlyphLayout text, float y, float size) {
        font.getData().setScale(0.8F);
        font.draw(batch, text, size + 16, y + 42);
        if (texture != null) {
            batch.draw(texture, 8, y, size, size);
        }
    }

    /**
     * Render the debug of the {@link GameLogic}.
     * @param delta {@code float} : The time since the last frame.
     */
    public void renderDebug(float delta) {
        shape.begin();
        logic.getMap().drawDebug(shape);

        // Render all the entities' debug
        for (Entity renderEntity : renderEntities) {
            renderEntity.drawDebug(shape);
        }
        shape.end();
    }

    /**
     * Load the {@link GameRenderer}'s {@link Texture}s.
     * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
     */
    public void load(String textureGroup, TextureManager textureManager) {
        textureManager.load(textureGroup, "interactions/select_box.png");
        textureManager.load(textureGroup, "uielements/reputation.png");
        textureManager.load(textureGroup, "uielements/money.png");
    }

    /**
     * Post load the {@link GameRenderer}'s {@link Texture}s.
     * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
     */
    public void postLoad(TextureManager textureManager) {
        interactSprite = new Sprite(textureManager.get("interactions/select_box.png"));
        moneyTex = textureManager.get("uielements/money.png");
        reputationTex = textureManager.get("uielements/reputation.png");
        text = new GlyphLayout();
    }

    /**
     * Unload all of the {@link Texture}s of the {@link GameRenderer}.
     * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
     */
    public void unload(TextureManager textureManager) {
        textureManager.unloadTexture("interactions/select_box.png");
        textureManager.unloadTexture("uielements/reputation.png");
        textureManager.unloadTexture("uielements/money.png");
    }

    /**
     * Add an {@link Entity} to be drawn by the {@link GameRenderer}.
     * @param entity {@link Entity} : The {@link Entity} to add.
     */
    public void addEntity(Entity entity) {
        // Only add the entity if it's not there already
        if (!renderEntities.contains(entity, true)) {
            renderEntities.add(entity);
        }
    }

    /**
     * Add an {@link Array} of {@link Entity}s to be drawn by the {@link GameRenderer}.
     * @param entities {@link Array} : An {@link Array} of {@link Entity}s to add.
     */
    public void addEntities(Array<?> entities) {
        for (Object entityToRemove : entities) {
            addEntity((Entity) entityToRemove);
        }
    }

    /**
     * Remove an {@link Entity} from the {@link #renderEntities} {@link Array}.
     * @param entity {@link Entity} : The {@link Entity} to remove.
     */
    public void removeEntity(Entity entity) {
        if (entity == null) return;
        renderEntities.removeValue(entity,true);
    }

    /**
     * Remove all {@link Entity} in an {@link Array} from the {@link #renderEntities}.
     * @param removeEntities {@link Array} : An {@link Array} of {@link Entity}s to remove.
     */
    public void removeEntities(Array<Entity> removeEntities) {
        for (Entity entityToRemove : removeEntities) {
            removeEntity(entityToRemove);
        }
    }

    /**
     * Get all of the {@link Entity}s in the {@link #renderEntities}
     * {@link Array}.
     * @return {@link Array<Entity>} : An array of all {@link Entity}s being rendered.
     */
    public Array<Entity> getEntities() {
        return renderEntities;
    }
}

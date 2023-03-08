package com.undercooked.game.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.Cook;
import com.undercooked.game.entity.Entity;
import com.undercooked.game.station.Station;

public class Map {

    Array<Array<MapCell>> map;
    int width;
    int height;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        // Initialise the columns
        map = new Array<>(width);
        // Then initialise the rows
        for (int i = 0 ; i < map.size ; i++) {
            map.add(new Array<MapCell>(height));
        }
        // Then init the map (so that it's empty)
        init();
    }

    private void init() {
        // Clear the array
        map.clear();
        // Load all of the values
        for (int x = 0 ; x < width ; x++) {
            Array thisCol = new Array<MapCell>();
            map.add(thisCol);
            for (int y = 0 ; y < width ; y++) {
                thisCol.add(null);
            }
        }
    }

    public boolean validCell(int x, int y) {
        if (x < 0 || x >= width) {
            return false;
        }
        if (y < 0 || y >= height) {
            return false;
        }
        return true;
    }

    public MapCell getCell(int x, int y) {
        // Make sure it's a valid cell position
        // If not, then return null
        if (!validCell(x,y)) {
            return null;
        }

        // If it's a valid cell, then return the cell
        return map.get(x).get(y);
    }

    /**
     * Removes all instances of a {@link MapEntity} from
     * the {@link Map}.
     * @param entity The {@link MapEntity} to remove.
     * @return {@code boolean}: {@code True} if the entity was on the map,
     *                          {@code False} if it was not.
     */
    public boolean removeEntity(MapEntity entity) {
        boolean found = false;
        MapCell cornerCell = null;
        // Loop through all cells, left to right, bottom to top
        for (int x = 0 ; x < width ; x++) {
            for (int y = 0 ; y < height ; y++) {
                MapCell thisCell = getCell(x,y);
                // If the current cell isn't empty.
                if (thisCell != null) {
                    // Check if the entity in this cell == entity.
                    // If it does, then set that cell to null
                    if (thisCell.mapEntity == entity){
                        map.get(x).set(y, null);
                    }
                }
            }
        }
        return found;
    }

    /**
     * Removes all instances of a {@link MapCell} from
     * the {@link Map}.
     * @param mapCell The {@link MapCell} to remove.
     * @return {@code boolean}: {@code True} if the entity was on the map,
     *                          {@code False} if it was not.
     */
    private boolean removeEntity(MapCell mapCell) {
        boolean found = false;
        MapCell cornerCell = null;
        // Loop through all cells, left to right, bottom to top
        for (int x = 0 ; x < width ; x++) {
            for (int y = 0 ; y < height ; y++) {
                // Check if the entity in this cell == entity.
                // If it does, then set that cell to null
                if (getCell(x,y) == mapCell) {
                    map.get(x).set(y, null);
                }
            }
        }
        return found;
    }

    /**
     * Adds an entity to the map at the specified grid coordinates x and y.
     * @param entity The {@link MapEntity} to add.
     * @param x The {@code x} position of the {@link MapEntity}.
     * @param y The {@code y} position of the {@link MapEntity}.
     */
    public void addMapEntity(MapEntity entity, int x, int y) {
        // Make sure the range is valid
        // (x, x+entity.width, y, y+entity.height all in range)
        // Make sure that width and height are > 0
        if (entity.width <= 0 || entity.height <= 0) {
            return;
        }
        // Make sure that the cell is valid
        if (!(validCell(x,y) && validCell(x+entity.width,y+entity.height))) {
            // If it's not, then return as it can't be added.
            return;
        }

        // Create a new map cell for this entity. It only needs the one.
        MapCell newCell = new MapCell(false);
        newCell.mapEntity = entity;

        // And now add the entity
        for (int i = x ; x < width ; x++) {
            for (int j = y ; y < height ; y++) {
                // If there's already a station here, remove it completely.
                if (getCell(i, j) != null) {
                    removeEntity(getCell(i, j).mapEntity);
                }
                map.get(i).set(j, newCell);
            }
        }

        // Below it, if there isn't a MapCell already, place a cupboard
        MapCell cellBelow = getCell(x, y-1);
        if (cellBelow == null) {
            cellBelow = new MapCell(true);
            cellBelow.mapEntity = new MapEntity();
            cellBelow.mapEntity.setTexture("game/textures/cupboard.png");
        }

    }

    /**
     * Check if the entity is colliding with any tiles, using the entity's
     * x and y.
     * @param entity The {@link Entity} to check.
     * @return {@code True} if colliding, {@code False} if not.
     */
    public boolean checkCollision(Entity entity) {
        return checkCollision(entity, entity.pos.x, entity.pos.y);
    }

    public boolean checkCollision(Entity entity, float x, float y) {
        return checkCollision(new Rectangle(x, y, entity.collision.height, entity.collision.width));
    }

    public boolean checkCollision(Rectangle collision) {
        int cellX = MapManager.posToGrid(collision.x);
        int cellY = MapManager.posToGrid(collision.y);

        // Get the cell.
        MapCell cell = getCell(cellX, cellY);
        // If there isn't a cell here, return false
        if (cell == null) {
            return false;
        }
        // If it's collidable, then return false
        if (!cell.collidable) {
            return false;
        }
        // Return if the entity is colliding or not.
        return cell.mapEntity.isColliding(collision);
    }

    /**
     * Loads all the {@link com.badlogic.gdx.graphics.Texture}s of the
     * {@link MapEntity}s.
     * @param textureManager The {@link TextureManager} to load using.
     */
    public void loadAll(TextureManager textureManager, String textureID) {
        for (int x = 0 ; x < width ; x++) {
            for (int y = 0 ; y < height ; y++) {
                MapCell thisCell = getCell(x,y);
                if (thisCell != null) {
                    thisCell.mapEntity.load(textureManager, textureID);
                }
            }
        }
    }

    // Draw function
    public Array<MapEntity> getAllEntities() {
        Array<MapEntity> entities = new Array<>();
        // Loop through all the cells
        for (int x = 0 ; x < width ; x++) {
            for (int y = 0 ; y < height ; y++) {
                // Get the current cell
                MapCell thisCell = getCell(x,y);
                // Make sure it's not null
                if (thisCell != null) {
                    // Get the entity of the cell
                    MapEntity thisEntity = thisCell.mapEntity;
                    // If it's not null...
                    if (thisEntity != null) {
                        // and it's not already in the array...
                        if (!entities.contains(thisEntity, true)) {
                            // then add it to the array.
                            entities.add(thisEntity);
                        }
                    }
                }
            }
        }
        // Return all the entities.
        return entities;
    }

    /**
     * Unloads all of the {@link com.badlogic.gdx.graphics.Texture}s of the
     * {@link MapEntity}s, and then clears the map.
     */
    public void dispose() {
        // Loop through the cells
        for (int x = 0 ; x < width ; x++) {
            for (int y = 0 ; y < height ; y++) {
                // Get the current cell
                MapCell thisCell = getCell(x,y);
                // Make sure it's not nothing
                if (thisCell != null) {

                }
            }
        }
    }
}

package com.undercooked.game.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.Entity;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.MathUtil;

import java.awt.*;

public class Map {

    Array<Array<MapCell>> map;
    private TextureManager textureManager;
    private int width, fullWidth;
    private int height, fullHeight;
    private int offsetX;
    private int offsetY;
    public final MapCell outOfBounds = new MapCell(true, false, false);

    public Map(int width, int height, TextureManager textureManager) {
        this(width,height,width,height, textureManager);
    }

    public Map(int width, int height, int fullWidth, int fullHeight, TextureManager textureManager) {
        // Initialise the columns
        map = new Array<>();
        // Then initialise the rows
        /*for (int i = 0 ; i < map.size ; i++) {
            map.add(new Array<MapCell>(fullHeight));
        }*/
        this.width = width;
        this.height = height;
        this.fullWidth = fullWidth;
        this.fullHeight = fullHeight;// Then init the map (so that it's empty)
        this.textureManager = textureManager;
        this.outOfBounds.setX(-fullWidth);
        this.outOfBounds.setY(-fullHeight);
        init();
    }

    private void init() {
        // Clear the array
        map.clear();
        // Load all the values
        for (int x = 0; x < fullWidth; x++) {
            Array thisCol = new Array<MapCell>();
            map.add(thisCol);
            for (int y = 0; y < fullHeight; y++) {
                MapCell newCell = new MapCell();
                thisCol.add(newCell);
                resetCell(newCell);
                newCell.setX(x);
                newCell.setY(y);
            }
        }
    }

    public void resetCell(int x, int y) {
        MapCell cell = getCellFull(x,y);
        if (cell != null) {
            resetCell(cell);
        }
    }

    public void resetCell(MapCell cell) {
        cell.setInteractable(false);
        cell.setCollidable(false);
        cell.setBase(false);
        cell.setMapEntity(null);
        cell.setBelowTile(Constants.DEFAULT_FLOOR_TILE);
    }

    protected boolean validCellFull(int x, int y) {
        if (x < 0 || x >= fullWidth) {
            return false;
        }
        if (y < 0 || y >= fullHeight) {
            return false;
        }
        return true;
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

    protected MapCell getCellFull(int x, int y) {
        return getCellFull(x, y, true);
    }

    protected MapCell getCellFull(int x, int y, boolean allowOutOfBounds) {
        // Make sure it's a valid cell position
        // If not, then return null
        if (!validCellFull(x,y)) {
            if (allowOutOfBounds) {
                return outOfBounds;
            }
            return null;
        }

        // If it's a valid cell, then return the cell
        return map.get(x).get(y);
    }

    public MapCell getCell(int x, int y) {
        return getCell(x, y, true);
    }

    public MapCell getCell(int x, int y, boolean allowOutOfBounds) {
        return getCellFull(x+offsetX,y+offsetY, allowOutOfBounds);
        // Make sure it's a valid cell position
        // If not, then return null
        /*if (!validCell(x,y)) {
            return null;
        }

        // If it's a valid cell, then return the cell
        return map.get(x).get(y);*/
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = Math.max(Math.min(offsetX, fullWidth-width), 0);
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = Math.max(Math.min(offsetY, fullHeight-height), 0);
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
        for (int x = 0 ; x < fullWidth ; x++) {
            for (int y = 0 ; y < fullHeight ; y++) {
                MapCell thisCell = getCellFull(x,y);
                // If the current cell isn't empty.
                if (thisCell != null) {
                    // Check if the entity in this cell == entity.
                    // If it does, then set that cell to null
                    if (thisCell.mapEntity == entity){
                        resetCell(x, y);
                    }
                }
            }
        }
        return found;
    }

    protected void setFullMapCell(int x, int y, MapCell cell) {
        map.get(x).set(y, cell);
    }

    protected void setMapCell(int x, int y, MapCell cell) {
        setFullMapCell(x+offsetX, y+offsetY, cell);
    }

    protected void addFullMapEntity(MapEntity entity, int x, int y, String floorTile) {
        addFullMapEntity(entity, x, y, floorTile, false);
    }

    protected void addFullMapEntity(MapEntity entity, int x, int y, String floorTile, boolean hasCollision) {
        Array<MapEntity> entitiesToRemove = new Array<>();
        System.out.println(String.format("Placing tile at %d, %d", x, y));
        // And now add the entity
        for (int i = x + entity.getCellWidth()-1 ; i >= x ; i--) {
            for (int j = y + entity.getCellHeight()-1 ; j >= y ; j--) {
                // If it's a valid cell
                if (validCellFull(i,j)) {
                    MapCell cellToReplace = getCellFull(i, j, false);
                    // If there's already a station here, remove it completely.
                    boolean hadCollision = getCellFull(i, j).collidable;
                    if (cellToReplace.mapEntity != null) {
                        // Only add it for removal if it hasn't been already
                        MapEntity entityToRemove = getCellFull(i, j).mapEntity;
                        if (!entitiesToRemove.contains(entityToRemove, true)) {
                            entitiesToRemove.add(entityToRemove);
                        }
                    }
                    cellToReplace.setCollidable(hasCollision);
                    cellToReplace.setInteractable(true);
                    cellToReplace.setMapEntity(entity);
                    cellToReplace.setBelowTile(floorTile);
                    // If there is a station above this...
                    if (validCellFull(i,j+1)) {
                        MapCell aboveCell = getCellFull(i,j+1, false);
                        if (aboveCell != null) {
                            // And the previous cell had collision...
                            if (hadCollision) {
                                // Then set this one to collidable
                                cellToReplace.collidable = true;
                            }
                        }
                    }
                }
            }

            // Below it, if there isn't a MapCell already, place a cupboard
            // It is not in the loop above, as only the lowest y needs it below
            MapCell cellBelow = getCellFull(i, y-1, false);
            if (cellBelow != null) {
                if (cellBelow.mapEntity == null) {
                    // If y-1 is valid, and basePath is not null
                    if (entity.basePath != null && validCellFull(i, y - 1)) {
                        // Make a cupboard cell below and add it to the map
                        cellBelow.setCollidable(true);
                        cellBelow.setInteractable(false);
                        cellBelow.setBase(true);
                        cellBelow.mapEntity = new MapEntity();
                        cellBelow.mapEntity.setTexture(entity.basePath);
                        cellBelow.setBelowTile(null);
                        cellBelow.setWidth(1);
                        cellBelow.setHeight(1);
                        cellBelow.setX(MapManager.gridToPos(i));
                        cellBelow.setY(MapManager.gridToPos(y - 1));
                    }
                } else {

                    // If not...
                    // Update the cell below, if it is a base
                    if (cellBelow.isBase()) {
                        // If the entity has a base path, then replace it
                        if (entity.basePath != null) {
                            cellBelow.mapEntity.setTexture(entity.basePath);
                        } else {
                            // If it doesn't have a base path, remove the base
                            resetCell(cellBelow);
                        }
                    } else {
                        // If it's not a base below, then set if it has collision
                        // depending on if it has a base or not

                        // TODO: Fix below problem
                        // Note that this has a problem when a station with collision has a station placed above it
                        // that does not have a base, as it will remove the collision of the station below.
                        // E.g: Placing a bin directly above another bin.
                        cellBelow.setCollidable(entity.basePath != null);
                    }
                }
            }
        }

        // Remove all entities that need to be removed.
        for (MapEntity mapEntity : entitiesToRemove) {
            removeEntity(mapEntity);
        }
    }

    public void addMapEntity(MapEntity entity, int x, int y, String floorTile) {
        addMapEntity(entity, x, y, floorTile, false);
    }

    /**
     * Adds an entity to the map at the specified grid coordinates x and y.
     * @param entity The {@link MapEntity} to add.
     * @param x The {@code x} position of the {@link MapEntity}.
     * @param y The {@code y} position of the {@link MapEntity}.
     */
    public void addMapEntity(MapEntity entity, int x, int y, String floorTile, boolean hasCollision) {
        // Make sure the range is valid
        // (x, x+entity.width, y, y+entity.height all in range)
        // Make sure that width and height are > 0
        if (entity.getCellWidth() <= 0 || entity.getCellHeight() <= 0) {
            return;
        }
        // Make sure that the cell is valid
        if (!(validCell(x,y) && validCell(x+entity.getCellWidth()-1,y+entity.getCellHeight()-1))) {
            // If it's not, then return as it can't be added.
            return;
        }

        // Add it to the map
        addFullMapEntity(entity, x+offsetX, y+offsetY, floorTile, hasCollision);
    }

    /**
     * Check if the entity is colliding with any tiles, using the entity's
     * x and y.
     * @param entity The {@link Entity} to check.
     * @return {@code True} if colliding, {@code False} if not.
     */
    public MapCell getCollision(Entity entity) {
        return getCollision(entity.collision);
    }

    public MapCell getCollision(Entity entity, float x, float y) {
        return getCollision(new Rectangle(x, y, entity.collision.width, entity.collision.height));
    }

    public MapCell getCollision(Rectangle collision) {
        return getCollision(collision, CollisionType.COLLIDABLE);
    }

    public MapCell getCollision(Rectangle collision, boolean returnClosest) {
        return getCollision(collision, returnClosest, CollisionType.COLLIDABLE);
    }

    public MapCell getCollision(Rectangle collision, CollisionType collisionType) {
        return getCollision(collision, false, collisionType);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getFullWidth() {
        return fullWidth;
    }

    public float getFullHeight() {
        return fullHeight;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }


    public enum CollisionType {
        COLLIDABLE,
        INTERACTABLE
    }

    public MapCell getCollision(Rectangle collision, boolean returnClosest, CollisionType collisionType) {
        // Get the range of cell X to cellY
        int cellX    = MapManager.posToGridFloor(collision.x);
        int cellXMax = MapManager.posToGridFloor(collision.x + collision.width);

        int cellY    = MapManager.posToGridFloor(collision.y);
        int cellYMax = MapManager.posToGridFloor(collision.y + collision.height);

        //System.out.println("cellX: " + cellX + ", cellXMax: " + cellXMax);
        //System.out.println("cellY: " + cellY + ", cellYMax: " + cellYMax);

        Array<Point> validCells = null;
        if (returnClosest) {
            validCells = new Array();
        }

        // Loop through all the cells in the range
        for (int x = cellX ; x <= cellXMax ; x++) {
            for (int y = cellY ; y <= cellYMax ; y++) {
                // Check if it's a valid cell
                if (!validCellFull(x, y)) {
                    // If it's not valid, return that it's colliding
                    if (!returnClosest) {
                        return outOfBounds;
                    }
                    validCells.add(new Point(-width, -height));
                }

                // Get the cell.
                MapCell cell = getCellFull(x, y);
                if (cell == null) {
                    continue;
                }
                // Make sure it has a map entity
                if (cell.mapEntity == null) {
                    continue;
                }
                switch (collisionType) {
                    case COLLIDABLE:
                        // If it's not a collidable, then skip
                        if (!cell.isCollidable()) {
                            continue;
                        }
                        // If they're not colliding, then skip
                        if (!cell.mapEntity.isColliding(collision)) {
                            continue;
                        }
                        break;
                    case INTERACTABLE:
                        // If it's not an interactable, then skip.
                        if (!cell.isInteractable()) {
                            continue;
                        }
                        // If they're not interacting, then skip
                        if (!cell.mapEntity.isInteracting(collision)) {
                            continue;
                        }
                }
                // Otherwise, if the above succeeds, return the MapCell
                if (!returnClosest) {
                    return cell;
                }
                validCells.add(new Point(x,y));
            }
        }

        // If requested to return the closest...
        if (returnClosest) {
            // If there are none found, then return null.
            if (validCells.size == 0) {
                return null;
            }
            // If there's only one, return that
            if (validCells.size == 1) {
                return getCellFull(validCells.get(0).x, validCells.get(0).y);
            }

            // Go through the points in the array, and find the closest one.
            Point firstPoint = validCells.get(0);
            MapCell closestCell = getCellFull(firstPoint.x, firstPoint.y, true);
            double closestDist = MathUtil.distanceBetweenRectangles(collision, closestCell.getCollision());
            for (int i = 1 ; i < validCells.size ; i++) {
                Point currentPoint = validCells.get(i);
                MapCell currentCell = getCellFull(currentPoint.x, currentPoint.y, true);
                double currentDist = MathUtil.distanceBetweenRectangles(collision, currentCell.getCollision());
                if (currentDist < closestDist) {
                    closestDist = currentDist;
                    closestCell = currentCell;
                }
            }

            // Return the cell at that location
            return closestCell;
        }

        // If none of the above returns, then return false as they're not colliding
        return null;
    }


    public boolean checkCollision(Entity entity) {
        return checkCollision(entity.collision);
    }

    public boolean checkCollision(Entity entity, float x, float y) {
        return checkCollision(new Rectangle(x, y, entity.collision.width, entity.collision.height));
    }

    public boolean checkCollision(Rectangle collision) {
        return checkCollision(collision, CollisionType.COLLIDABLE);
    }

    public boolean checkCollision(Rectangle collision, boolean returnClosest) {
        return checkCollision(collision, returnClosest, CollisionType.COLLIDABLE);
    }

    public boolean checkCollision(Rectangle collision, CollisionType collisionType) {
        return checkCollision(collision, false, collisionType);
    }

    public boolean checkCollision(Rectangle collision, boolean returnClosest, CollisionType collisionType) {
        MapCell collidingCell = getCollision(collision, returnClosest, collisionType);

        // If it's null, then return false
        if (collidingCell == null) {
            return false;
        }

        // Custom checks
        if (collisionType != CollisionType.COLLIDABLE && collidingCell == outOfBounds) {
            return false;
        }
        // Return true
        return true;
    }

    public void drawGround (SpriteBatch batch) {
        // Draw a tile for every cell of the map.
        for (int x = 0 ; x < fullWidth ; x++) {
            for (int y = 0 ; y < fullHeight ; y++) {
                getCellFull(x,y).drawBelow(batch);
            }
        }
    }

    public void drawDebug(ShapeRenderer shape) {
        for (int x = 0 ; x < fullWidth ; x++) {
            for (int y = 0 ; y < fullHeight ; y++) {
                shape.setColor(Color.SKY);
                if ((x >= offsetX && x < width + offsetX) &&
                        (y >= offsetY && y < height + offsetY)) {
                    shape.setColor(Color.GREEN);
                }
                shape.rect(MapManager.gridToPos(x), MapManager.gridToPos(y), 64, 64);
            }
        }
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
                    thisCell.load(textureManager, textureID);
                }
            }
        }
    }

    /**
     * Loads just the floor tiles of the MapCells
     * @param textureManager {@link TextureManager} : The {@link TextureManager}
     *                                                to use.
     */
    public void loadFloor(TextureManager textureManager, String textureID) {
        for (int x = 0 ; x < width ; x++) {
            for (int y = 0 ; y < height ; y++) {
                MapCell thisCell = getCell(x,y);
                if (thisCell != null) {
                    thisCell.loadFloor(textureManager, textureID);
                }
            }
        }
    }

    /**
     * Update the floor sprites of all the {@link MapCell}s.
     * @param textureManager {@link TextureManager} : The {@link TextureManager}
     *                                                to use.
     */
    public void postLoad(TextureManager textureManager) {
        for (int x = 0 ; x < width ; x++) {
            for (int y = 0 ; y < height ; y++) {
                MapCell thisCell = getCell(x,y);
                if (thisCell != null) {
                    thisCell.updateBelowTile(textureManager);
                }
            }
        }
    }

    // Draw function
    public Array<MapEntity> getAllEntities() {
        Array<MapEntity> entities = new Array<>();
        // System.out.println(String.format("fullWidth: %d, fullHeight: %d", fullWidth, fullHeight));
        // Loop through all the cells
        for (int x = 0 ; x < fullWidth ; x++) {
            for (int y = 0 ; y < fullHeight ; y++) {
                // Get the current cell
                // System.out.println(String.format("x: %d, y: %d", x, y));
                MapCell thisCell = getCellFull(x,y);
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
                    // If it's not nothing, then unload
                    // thisCell.dispose();
                }
            }
        }
    }
}

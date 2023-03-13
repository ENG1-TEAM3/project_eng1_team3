package com.undercooked.game.map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.Entity;
import com.undercooked.game.station.Station;

public class Map {

    Array<Array<MapCell>> map;
    private int width, fullWidth;
    private int height, fullHeight;
    private int offsetX;
    private int offsetY;

    public Map(int width, int height) {
        this(width,height,width,height);
    }

    public Map(int width, int height, int fullWidth, int fullHeight) {
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
                thisCol.add(null);
            }
        }
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
        // Make sure it's a valid cell position
        // If not, then return null
        if (!validCellFull(x,y)) {
            return null;
        }

        // If it's a valid cell, then return the cell
        return map.get(x).get(y);
    }

    public MapCell getCell(int x, int y) {
        return getCellFull(x+offsetX,y+offsetY);
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

    protected void setFullMapCell(int x, int y, MapCell cell) {
        map.get(x).set(y, cell);
    }

    protected void setMapCell(int x, int y, MapCell cell) {
        setFullMapCell(x+offsetX, y+offsetY, cell);
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
        if (entity.getCellWidth() <= 0 || entity.getCellHeight() <= 0) {
            return;
        }
        // Make sure that the cell is valid
        if (!(validCell(x,y) && validCell(x+entity.getCellWidth()-1,y+entity.getCellHeight()-1))) {
            // If it's not, then return as it can't be added.
            return;
        }

        // And now add the entity
        for (int i = x ; i < x + entity.getCellWidth() ; i++) {
            for (int j = y ; j < y + entity.getCellHeight() ; j++) {
                MapCell newCell = new MapCell(false);
                newCell.mapEntity = entity;
                // If it's a valid cell
                if (validCell(i,j)) {
                    // If there's already a station here, remove it completely.
                    if (getCell(i, j) != null) {
                        removeEntity(getCell(i, j).mapEntity);
                    }
                    setMapCell(i,j,newCell);
                    // If there is a station above this...
                    if (validCell(i,j+1)) {
                        if (getCell(i,j+1) != null) {
                            // Then set this one to collidable
                            newCell.collidable = true;
                        }
                    }
                }
            }

            // Below it, if there isn't a MapCell already, place a cupboard
            // It is not in the loop above, as only the lowest y needs it below
            MapCell cellBelow = getCell(i, y-1);
            if (cellBelow == null) {
                // If y-1 is valid, and basePath is not null
                if (entity.basePath != null && validCell(i,y-1)) {
                    // Make a cupboard cell below and add it to the map
                    MapCell newBelow = new MapCell(true);
                    newBelow.mapEntity = new MapEntity();
                    newBelow.mapEntity.setTexture(entity.basePath);
                    newBelow.mapEntity.setWidth(1);
                    newBelow.mapEntity.setHeight(1);
                    newBelow.mapEntity.setX(MapManager.gridToPos(offsetX + i));
                    newBelow.mapEntity.setY(MapManager.gridToPos(offsetY + y-1));
                    setMapCell(i,y-1,newBelow);
                }
            } else {
                // If not, set the cell below to be collidable
                cellBelow.collidable = true;
            }
        }

        // Set the entity's position
        entity.setX(MapManager.gridToPos(offsetX + x));
        entity.setY(MapManager.gridToPos(offsetY + y));
    }

    /**
     * Check if the entity is colliding with any tiles, using the entity's
     * x and y.
     * @param entity The {@link Entity} to check.
     * @return {@code True} if colliding, {@code False} if not.
     */
    public boolean checkCollision(Entity entity) {
        return checkCollision(entity.collision);
    }

    public boolean checkCollision(Entity entity, float x, float y) {
        return checkCollision(new Rectangle(x, y, entity.collision.width, entity.collision.height));
    }

    public boolean checkCollision(Rectangle collision) {
        // Get the range of cell X to cellY
        int cellX    = MapManager.posToGridFloor(collision.x);
        int cellXMax = MapManager.posToGridFloor(collision.x + collision.width);

        int cellY    = MapManager.posToGridFloor(collision.y);
        int cellYMax = MapManager.posToGridFloor(collision.y + collision.height);

        System.out.println("cellX: " + cellX + ", cellXMax: " + cellXMax);
        System.out.println("cellY: " + cellY + ", cellYMax: " + cellYMax);

        // Loop through all the cells in the range
        for (int x = cellX ; x <= cellXMax ; x++) {
            for (int y = cellY ; y <= cellYMax ; y++) {
                // Check if it's a valid cell
                if (!validCellFull(x, y)) {
                    // If it's not valid, return that it's colliding
                    return true;
                }

                // Get the cell.
                MapCell cell = getCellFull(x, y);
                // If there isn't a cell here, skip
                if (cell == null) {
                    continue;
                }
                // If it's collidable, then skip
                if (!cell.collidable) {
                    continue;
                }
                // Otherwise, if they're colliding, return true
                if (cell.mapEntity.isColliding(collision)) {
                    return true;
                }
            }
        }
        // If none of the above returns, then return false as they're not colliding
        return false;
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

                }
            }
        }
    }
}

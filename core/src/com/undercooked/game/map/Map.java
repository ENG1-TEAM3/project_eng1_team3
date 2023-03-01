package com.undercooked.game.map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.entity.Cook;
import com.undercooked.game.entity.Entity;
import com.undercooked.game.station.Station;

public class Map {

    class MapCell {
        boolean collidable;
        MapEntity mapEntity;

        public MapCell(boolean collidable) {
            this.collidable = collidable;
        }
    }

    Array<Array<MapCell>> map;
    int width;
    int height;

    public Map(int width, int height) {

    }

    public void load(String path) {

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

    public boolean removeEntity() {

    }

    public void addMapEntity(MapEntity entity, int x, int y) {
        // Make sure the range is valid
        // (x, x+entity.width, y, y+entity.height all in range)
        // Make sure that the cell is valid
        if (!(validCell(x,y) && validCell(x+entity.width,y+entity.height))) {
            // If it's not, then return as it can't be added.
            return;
        }

        // And now add the entity

        //

        // Below it, if there isn't a MapCell already, place a cupboard
        MapCell
        if (getCell(x,y-1) != null) {

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

}

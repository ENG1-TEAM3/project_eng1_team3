package com.team3gdx.game;

import java.util.logging.FileHandler;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MainGameClass extends ApplicationAdapter {

	// Display Size
	private int displayW;
	private int displayH;

	public OrthographicCamera camera;
	public Control control;

	// For drawing entities.
	public SpriteBatch batch;
	
	// Audio Controller
	public AudioController sounds;
	

	// Map
	public TiledMap map;
	public TiledMapRenderer tiledMapRenderer;

	// Solid tiles
	MapObjects solidObjects;

	// Cook
	public Cook cook;

	// Elapsed time of cook animation (Not used currently)
	float elapsedTime;

	@Override
	public void create() {
		elapsedTime = 0;

		displayW = Gdx.graphics.getWidth();
		displayH = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(512, 512);
		camera.zoom = 1.1f;
		
		sounds = new AudioController();

		control = new Control();
		Gdx.input.setInputProcessor(control);

		batch = new SpriteBatch();

		map = new TmxMapLoader().load("map/untitled.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(map);

		// Get the 'Solid' object layer from map.
		solidObjects = map.getLayers().get("Solid").getObjects();

		cook = new Cook(new Vector2(64, 64));
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		elapsedTime += Gdx.graphics.getDeltaTime();

		camera.position.lerp(new Vector3(cook.pos.x, cook.pos.y, 0), .1f);
		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

//		checkCollision();
		cook.update(control);

		// TODO: Upper and lower body rendering of cook.

		tiledMapRenderer.setView(camera);
		// Render first layer (background).
		tiledMapRenderer.render(new int[] { 0 });
		// Render cook.
		batch.begin();
		cook.draw(batch, elapsedTime);
		batch.end();
		// Render foreground.
		tiledMapRenderer.render(new int[] { 3 });

	}

	// Basic collision detection (Doesn't work)!
	private void checkCollision() {
		// Loop over every solid object.
		for (RectangleMapObject rectangleObject : solidObjects.getByType(RectangleMapObject.class)) {
			Rectangle rectangle = rectangleObject.getRectangle();
			if (Intersector.overlaps(rectangle, cook.getBounds())) {
				// Get the rectangle's position.
				float cellX = rectangleObject.getRectangle().getX(), cellY = rectangleObject.getRectangle().getY();
				// Check where the cell is relative to the player and update position in
				// opposite direction.
				if (cellX < cook.pos.x)
					cook.pos.x += cook.speed;
				else if (cellX > cook.pos.x)
					cook.pos.x -= cook.speed;
				else if (cellY < cook.pos.y)
					cook.pos.y += cook.speed;
				else if (cellY > cook.pos.y)
					cook.pos.y -= cook.speed;
			}
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		map.dispose();
	}
}

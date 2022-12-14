package com.team3gdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class MainGameClass extends ApplicationAdapter {
	BitmapFont font;
	public OrthographicCamera camera; // Camera
	public Control control;

	// For drawing entities.
	public SpriteBatch batch;

	// Audio Controller
	public AudioController sounds;

	// Map
	public TiledMap map;
	public TiledMapRenderer tiledMapRenderer;
	// Cook
	public Cook cook;
	public static CollisionTile[][] CLTiles; // 2D array used to store all the collider tiles objects
	private ShapeRenderer shapeRenderer; // A shaperenderer jam-sut used to draw some debug shapes
	public FitViewport vp; // Viewport for viewing the game
	long startTime;
	public void resize(int width, int height) {
		vp.update(width, height);
	}
	@Override
	public void create() {
		font = new BitmapFont();
		font.getData().setScale(4);
		startTime = System.currentTimeMillis();
		camera = new OrthographicCamera();
		vp = new FitViewport(1920, 1080, camera);
		sounds = new AudioController();
		control = new Control();
		Gdx.input.setInputProcessor(control);
		batch = new SpriteBatch();
		map = new TmxMapLoader().load("map/art_map/prototype_map.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
		cook = new Cook(new Vector2(64, 64));
		shapeRenderer = new ShapeRenderer();
		constructCollisionData(map);
	}

	@Override
	public void render() {
		shapeRenderer.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



		batch.setProjectionMatrix(camera.combined);
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render(new int[] { 0 });

		batch.begin();
		cook.draw_bot(batch);
		batch.end();

		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(cook.getCollideBoxAsArray()[0],cook.getCollideBoxAsArray()[1],
				cook.getCollideBoxAsArray()[2],cook.getCollideBoxAsArray()[3]);
		shapeRenderer.end();

		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.rect(cook.pos.x, cook.pos.y, 1,1);
		shapeRenderer.end();


		tiledMapRenderer.render(new int[] { 1 });
		batch.begin();
		cook.draw_top(batch);
		batch.end();
		Matrix4 uiMatrix = camera.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, 1920, 1080);
		batch.setProjectionMatrix(uiMatrix);
		batch.begin();
		font.draw(batch, String.valueOf(cook.getDirection()) ,0,300);
		batch.draw(new Texture("entities/cook.png"), 0,0);
		batch.end();
		batch.setProjectionMatrix(camera.combined);


		//Uncomment if you want to draw some green squares over solid squares
		//Gdx.gl.glEnable(GL20.GL_BLEND);
		//shapeRenderer.begin(ShapeType.Filled);
		//int wid = CLTiles.length;
		//int hi = CLTiles[0].length;
		//for (int y = 0; y < hi; y++) {
		//	for (int x = 0; x < wid; x++) {
		//		CollisionTile tl = CLTiles[x][y];
		//		if (tl != null) {
		//			shapeRenderer.setColor(new Color(0,1.0f,0, 0.1f));
		//			shapeRenderer.rect(tl.tilex,tl.tiley,tl.tilewidth,tl.tileheight);
		//	}
		//	}
		//}
		//shapeRenderer.end();
		//Gdx.gl.glDisable(GL20.GL_BLEND);
		checkInteraction(cook,shapeRenderer);

		camera.position.lerp(new Vector3(cook.pos.x, cook.pos.y, 0), .1f);
		//camera.position.set(new Vector2(cook.pos.x, cook.pos.y), .1f);
		camera.update();
		//newCheckCollision(shapeRenderer);
		cook.update(control,(System.currentTimeMillis() - startTime));
		startTime = System.currentTimeMillis();

	}

	private void constructCollisionData(TiledMap mp){
		TiledMapTileLayer botlayer = (TiledMapTileLayer)mp.getLayers().get(0);
		int mapwidth = botlayer.getWidth();
		int mapheight = botlayer.getHeight();
		CLTiles = new CollisionTile[mapwidth][mapheight];
		TiledMapTileLayer toplayer = (TiledMapTileLayer)mp.getLayers().get(1);
		int topwidth = toplayer.getWidth();
		int topheight = toplayer.getHeight();
		for(int y = 0; y < topheight; y++){
			for (int x = 0; x < topwidth; x++){
				Cell tl2 = toplayer.getCell(x,y);
				if (tl2 != null){
					CLTiles[x][y] = new CollisionTile(x * 64, y * 64, 64, 64);
				}
			}
		}
		for(int y = 0; y < mapheight; y++){
			for (int x = 0; x < mapwidth; x++){
				Cell tl = botlayer.getCell(x,y);
				if (tl != null) {
					TiledMapTile tlt = tl.getTile();
					MapProperties mpr = tlt.getProperties();
					if(mpr.get("name") == null){
						CLTiles[x][y] = new CollisionTile(x * 64, y * 64, 64, 64);
					}
					else{
						if (CLTiles[x][y-1] != null)
						{
								CLTiles[x][y-1] = null;
						}
					}
				}
			}
		}
	}

	public void checkInteraction(Cook ck, ShapeRenderer sr){
		float centralcookx = ck.getX() + ck.getWidth()/2;
		float centralcooky = ck.getY();
		int cellx = (int)Math.floor(centralcookx/64);
		int celly = (int)Math.floor(centralcooky/64);
		sr.begin(ShapeType.Line);
		sr.setColor(new Color(0,1,1,1));
		sr.rect(cellx*64, celly*64, 64,64);
		sr.rect(centralcookx-1, centralcooky-1, 2,2);
		sr.end();
		int checkCellX = cellx;
		int checkCellY = celly;
		switch (ck.getDirection()){
			case 'u':
				checkCellY += 2;
				break;
			case 'd':
				break;
			case 'l':
				checkCellX -= 1;
				checkCellY += 1;
				break;
			case 'r':
				checkCellX += 1;
				checkCellY += 1;
				break;
		}
		sr.begin(ShapeType.Line);
		sr.setColor(new Color(1,0,1,1));
		sr.rect(checkCellX*64, checkCellY*64, 64,64);
		sr.end();


	}

	@Override
	public void dispose() {
		sounds.disposeAll();
		batch.dispose();
		map.dispose();
	}
}
package com.team3gdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.team3gdx.game.MainGameClass;
import com.team3gdx.game.save.GameInfo;
import com.team3gdx.game.save.SaveService;

public class LoadScreen extends ScreenAdapter {

    private final MainGameClass game;
    private final SaveService save;
    private final Stage stage;
    private final Table table;
    private Texture background;

    private Array<GameInfo> saves = new Array<>();

    public LoadScreen(final MainGameClass game) {

        this.game = game;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        save = new SaveService();
    }

    @Override
    public void show() {
        background = new Texture(Gdx.files.internal("uielements/MainScreenBackground.jpg"));

        saves = save.getSavedGames();

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = new BitmapFont();

        for (GameInfo save : saves) {
            TextButton button = new TextButton(save.createdAt.toString(), style);

            button.addListener(new ClickListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    game.setScreen(new GameScreen(game, save));
                    super.touchUp(event, x, y, pointer, button);
                }
            });

            table.add(button).size(100, 100).padBottom(10);
            table.row();
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);

        game.batch.begin();
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        stage.act();
        stage.draw();
    }
}

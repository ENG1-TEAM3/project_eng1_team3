package com.undercooked.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;
import com.sun.org.apache.bcel.internal.Const;
import com.undercooked.game.Input.InputController;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.util.Constants;

public class WinScreen extends Screen {

    private String nameInput;
    public float score;
    private GlyphLayout scoreText;
    private GlyphLayout nameGlyph;

    public WinScreen(MainGameClass game) {
        super(game);
    }

    @Override
    public void load() {
    }

    @Override
    public void unload() {

    }

    @Override
    public void show() {
        // Stop the game's music
        game.gameMusic.setLooping(false);
        game.gameMusic.stop();
        // Reset name input
        this.nameInput = "";
        // Make name glyph
        this.nameGlyph = new GlyphLayout();
        // And update it
        updateNameGlyph();
    }

    public String getKey(int start, int end) {
        for (int i = start ; i <= end ; i++) {
            if (Gdx.input.isKeyJustPressed(i)) {
                return Input.Keys.toString(i);
            }
        }
        return null;
    }

    public void updateNameGlyph() {
        this.nameGlyph.setText(game.font, nameInput);
    }

    public void update(float delta) {
        // Check for input "enter"
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            // If no name is provided, just make it "???"
            if (nameInput.length() == 0) {
                nameInput = "???";
            }
            // If it's pressed, go to the LeaderBoard Screen.
            game.screenController.setScreen(Constants.LEADERBOARD_SCREEN_ID);
            // Get the Screen
            LeaderBoard leaderBoard = (LeaderBoard) game.screenController.getScreen(Constants.LEADERBOARD_SCREEN_ID);
            // And try to add the score
            leaderBoard.addLeaderBoardData(nameInput, score);
            // And stop here
            return;
        }

        // If backspace is pressed, remove the last added character
        if (nameInput.length() > 0 && Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            this.nameInput = this.nameInput.substring(0, this.nameInput.length() - 1);
            updateNameGlyph();
        }

        // If the name is 20 chars long, don't check for inputs
        if (nameInput.length() >= 20) return;

        // If any letter keys, a - z, are pressed
        // or if any numbers are pressed, add them.
        String addChar = getKey(Input.Keys.valueOf("A"),Input.Keys.valueOf("Z"));

        if (addChar == null) {
            addChar = getKey(Input.Keys.valueOf("0"), Input.Keys.valueOf("9"));
        }

        if (addChar != null) {
            nameInput += addChar;
            updateNameGlyph();
        }
    }

    @Override
    public void render(float delta) {
        // Update inputs
        InputController.updateKeys();

        // Update
        update(delta);

        // Clear the Screen
        ScreenUtils.clear(0, 0, 0, 0);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        // Draw the score text in the middle of the Screen
        game.font.draw(game.batch, scoreText, Constants.V_WIDTH/2-scoreText.width/2, Constants.V_HEIGHT/2);

        // And below that, draw the name underneath
        game.font.draw(game.batch, nameGlyph, Constants.V_WIDTH/2-nameGlyph.width/2, Constants.V_HEIGHT/2-30);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void fromScreen(Screen screen) {
        // It shouldn't ever reach this screen if not from the
        // GameScreen
        GameScreen gameScreen = (GameScreen) screen;

        // Get the score from the gameScreen
        this.score = gameScreen.gameLogic.getScore();

        // Make the GlyphLayout for the score
        this.scoreText = new GlyphLayout(MainGameClass.font, gameScreen.gameLogic.getScoreString());
    }
}

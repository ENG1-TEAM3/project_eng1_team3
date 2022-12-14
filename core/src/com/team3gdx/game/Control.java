package com.team3gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;

public class Control extends InputAdapter implements InputProcessor {

	public boolean up;
	public boolean down;
	public boolean left;
	public boolean right;

	@Override
	public boolean keyDown(int keyCode) {
		switch (keyCode) {
		case Keys.DOWN:
			down = true;
			break;
		case Keys.UP:
			up = true;
			break;
		case Keys.LEFT:
			left = true;
			break;
		case Keys.RIGHT:
			right = true;
			break;
		case Keys.W:
			up = true;
			break;
		case Keys.A:
			left = true;
			break;
		case Keys.S:
			down = true;
			break;
		case Keys.D:
			right = true;
			break;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.DOWN:
			down = false;
			break;
		case Keys.UP:
			up = false;
			break;
		case Keys.LEFT:
			left = false;
			break;
		case Keys.RIGHT:
			right = false;
			break;
		case Keys.W:
			up = false;
			break;
		case Keys.A:
			left = false;
			break;
		case Keys.S:
			down = false;
			break;
		case Keys.D:
			right = false;
			break;
		case Keys.ESCAPE:
			Gdx.app.exit();
			break;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	public boolean scrolled(int amount) {
		return false;
	}

}

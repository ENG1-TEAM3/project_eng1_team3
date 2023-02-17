package com.undercooked.game.station;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.undercooked.game.food.Ingredient;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.screen.GameScreen.STATE;

public class CookingStation extends Station {

	ParticleEffect[] pES;

	public CookingStation(Vector2 pos, int numberOfSlots, Ingredient[] allowedIngredients, String particlePath,
			String soundPath) {
		super(pos, numberOfSlots, false, allowedIngredients, "audio/soundFX/frying.mp3");
		createParticleEmitter(pos, particlePath);
	}

	/**
	 * Create particle effects above the station.
	 * 
	 * @param pos          The position of the particles.
	 * @param particlePath The path to the particle effect's behaviour.
	 */
	protected void createParticleEmitter(Vector2 pos, String particlePath) {
		pES = new ParticleEffect[numberOfSlots];
		for (int i = 0; i < numberOfSlots; i++) {
			ParticleEffect pE = new ParticleEffect();
			pE.load(Gdx.files.internal(particlePath), Gdx.files.internal("particles/"));

			pE.getEmitters().first().setPosition(16 + pos.x * 64 + ((i * 32) % 64),
					Math.floorDiv((i * 32), 64) * 32 + pos.y * 64 + 16);
			pE.start();
			pES[i] = pE;
		}

	}

	/**
	 * Render the particles.
	 * 
	 * @param batch
	 * @param slotIndex The slot to draw the particle effects at (which slot is
	 *                  active).
	 */
	public void drawParticles(SpriteBatch batch, int slotIndex) {
		batch.begin();
		if (GameScreen.state1 == STATE.Continue)
			pES[slotIndex].update(Gdx.graphics.getDeltaTime());
		pES[slotIndex].draw(batch);
		if (pES[slotIndex].isComplete())
			pES[slotIndex].reset();

		batch.end();
	}

	/**
	 * Lock the current cook at the station.
	 * 
	 * @return A boolean indicating if the cook was successfully locked.
	 */
	public boolean lockCook() {
		if (!slots.isEmpty()) {
			if (lockedCook == null) {
				GameScreen.cook.locked = true;
				lockedCook = GameScreen.cook;
			} else {
				lockedCook.locked = true;
			}
			return true;
		}
		if (lockedCook != null) {
			lockedCook.locked = false;
			lockedCook = null;
		}

		return false;
	}

	/**
	 * Check interactions with the cooking station.
	 * 
	 * @param batch
	 */
	public void checkCookingStation(SpriteBatch batch) {
		if (!slots.empty() && !GameScreen.cook.full() && slots.peek().flipped)
			drawText(batch, "Take [q]", new Vector2(pos.x * 64, pos.y * 64 - 16));
		else
			drawDropText(batch);

		if (GameScreen.control.interact) {
			if (!slots.empty() && !GameScreen.cook.full()) {
				if (slots.peek().flipped) {
					GameScreen.cook.pickUpItem(take());
				}

				return;
			}
		}
		if (GameScreen.control.drop) {
			if (!GameScreen.cook.heldItems.empty() && place(GameScreen.cook.heldItems.peek())) {
				GameScreen.cook.dropItem();
				slots.peek().cooking = true;
			}
		}
		if (!slots.empty() && GameScreen.control.flip)
			slots.peek().flip();

	}

}

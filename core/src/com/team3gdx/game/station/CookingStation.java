package com.team3gdx.game.station;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.food.Ingredient;

public class CookingStation extends Station {

	ParticleEffect[] pES;

	public CookingStation(Vector2 pos, int numberOfSlots, Ingredient[] allowedIngredients, String particlePath) {
		super(pos, numberOfSlots, false, allowedIngredients);
		createParticleEmitter(pos, particlePath);
	}

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

	public void drawParticles(SpriteBatch batch, int slotIndex) {
		batch.begin();
		pES[slotIndex].update(Gdx.graphics.getDeltaTime());
		pES[slotIndex].draw(batch);
		if (pES[slotIndex].isComplete())
			pES[slotIndex].reset();

		batch.end();
	}

}

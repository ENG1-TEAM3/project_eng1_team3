/*******************************************************************************
 * Copyright 2015 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.tomgrill.gdxtesting.tests;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.undercooked.game.files.SettingsControl;

import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class SettingsControlTests {

	SettingsControl setCon;

	@Before
	public void setup() {
		SettingsControl setCon = new SettingsControl("settingsTest.json");
	}

	/** Test loadData() loads the data in settings.json. */
	@Test
	public void loadSettings() {
		setCon.loadData();
		// assertEquals(setCon.getMusicVolume(), 0.5f, 0.01f);
	}

}
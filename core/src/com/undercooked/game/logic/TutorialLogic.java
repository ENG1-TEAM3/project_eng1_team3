package com.undercooked.game.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.Input.InputController;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.food.Request;
import com.undercooked.game.logic.tutorial.TutorialCustomerSpawnStep;
import com.undercooked.game.logic.tutorial.TutorialRegisterInteractStep;
import com.undercooked.game.logic.tutorial.TutorialStationInteractStep;
import com.undercooked.game.logic.tutorial.TutorialStep;
import com.undercooked.game.map.MapCell;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.Listener;

public class TutorialLogic extends ScenarioLoadLogic {

    private TutorialStep currentStep;
    private Array<TutorialStep> tutorialSteps;
    private Listener<TutorialStep> stepFinishListener;
    private Listener<MapCell> registerListener;

    public TutorialLogic(GameScreen game, TextureManager textureManager, AudioManager audioManager) {
        super(game, textureManager, audioManager);
        id = "<main>:tutorial";
        tutorialSteps = new Array<>();

        stepFinishListener = new Listener<TutorialStep>() {
            @Override
            public void tell(TutorialStep value) {
                nextStep();
            }
        };

        registerListener = new Listener<MapCell>() {
            @Override
            public void tell(MapCell value) {
                interactRegister(value);
                // Tell the current step
                tellStep(value);
            }
        };
        cookController.setInteractRegisterListener(registerListener);

        customerController.setCustomerWaitSpeed(0);
    }

    @Override
    public void update(float delta) {

        // Update inputs
        InputController.updateKeys();

        elapsedTime += delta;

        // Update the Stations
        stationController.update(delta, 1f);

        // If the current step is not null, check if it can be skipped.
        if (currentStep != null) {
            cookController.setProcessInputs(currentStep.getProcessInput());
            // Check if it's skippable
            if (currentStep.canSkip()) {
                // If it is, check if Enter is pressed.
                // If it is, go to the next tutorial step
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    nextStep();
                }
            }
        }

        // Update cooks.
        cookController.update(delta);

        // Update Customers.
        customerController.update(delta);

        // Update the current step after everything else, if it's not null
        if (currentStep != null) {
            currentStep.update(delta);
        }

    }

    public void nextStep() {
        // If there is not a next step, then the tutorial is done. Return to main screen
        if (tutorialSteps.isEmpty()) {
            currentStep = null;
            gameScreen.getScreenController().setScreen(Constants.MAIN_SCREEN_ID);
            return;
        }

        // If there is a next step, just set currentStep to that, and remove it from the array
        currentStep = tutorialSteps.removeIndex(0);
        currentStep.setEndListener(stepFinishListener);
        // Start the current step
        currentStep.start();
    }

    @Override
    public void start() {
        super.start();
        // Go to next step
        nextStep();
    }

    @Override
    public void load() {
        // Load the scenario
        super.load();
        // Set up the tutorial
        setupTutorial();
        // Load all the instructions
        for (TutorialStep tutorialStep : tutorialSteps) {
            tutorialStep.load(textureManager, Constants.GAME_TEXTURE_ID);
        }
    }

    @Override
    public void postLoad() {
        super.postLoad();
        // Reset the game
        reset();
        // Post load all the instructions
        for (TutorialStep tutorialStep : tutorialSteps) {
            tutorialStep.postLoad(textureManager);
        }
    }

    @Override
    public void reset() {
        super.reset();

        start();
    }

    /**
     * Sets up the {@link #tutorialSteps} array.
     */
    private void setupTutorial() {
        // Clear tutorial steps
        currentStep = null;
        tutorialSteps.clear();

        Request request = requestPool.get(0);

        // Add all the Tutorial steps
        tutorialSteps.add(new TutorialStep("Welcome to Piazza Panic: UnderCooked. Press Enter to continue.", 20f, false));
        tutorialSteps.add(new TutorialStep("This is a cooking game where you have to deliver what a Customer requests before they leave.", 20f, false));
        tutorialSteps.add(new TutorialCustomerSpawnStep("A Customer has just entered the restaurant! Let's find out what they want.", 20f, customerController, request));
        // tutorialSteps.add(new TutorialStationInteractStep(" Interact with the register that they're waiting at by pressing SPACE.", 0.5f, ));
        tutorialSteps.add(new TutorialRegisterInteractStep("Interact with the register next to the Customer by pressing SPACE to see their Request.", 20f, cookController));
        tutorialSteps.add(new TutorialStep("If you look at the bottom right of the screen, you can see the Customer's request. Here, they want a " + items.getItem(request.itemID).name + ".", 20f, false));
        tutorialSteps.add(new TutorialStep("Now to make it. Go over to the Tomato pantry (the box with tomatoes in it), and press R to take one from it!", 20f, false));
        tutorialSteps.add(new TutorialStep("Now that you have a tomato, you need to cut it. Place it down on the cutting station below with V, and then cut the tomato with SPACE.", 20f, false));
    }

    private void tellStep(Object value) {
        if (currentStep.getClass().equals(TutorialRegisterInteractStep.class) && value.getClass().equals(MapCell.class)) {
            currentStep.finished();
        }
    }

    @Override
    public void unload() {
        super.unload();
        // Clear the steps
        currentStep = null;
        tutorialSteps.clear();
    }

    @Override
    public void setId(String id) {
        // Don't do anything
    }

    @Override
    public boolean canSave() {
        return false;
    }

    public TutorialStep getCurrentStep() {
        return currentStep;
    }

    @Override
    public void moveCamera(float delta) {
        if (currentStep == null) return;
        if (currentStep.getX() < 0 && currentStep.getY() < 0) return;
        gameRenderer.moveCamera(delta, currentStep.getX(), currentStep.getY());
    }
}

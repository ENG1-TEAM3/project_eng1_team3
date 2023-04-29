package com.undercooked.game.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.Input.InputController;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.entity.customer.Customer;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.Request;
import com.undercooked.game.logic.tutorial.*;
import com.undercooked.game.map.MapCell;
import com.undercooked.game.map.MapEntity;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.Listener;

public class TutorialLogic extends ScenarioLoadLogic {

    private TutorialStep currentStep;
    private Array<TutorialStep> tutorialSteps;
    private Listener<TutorialStep> stepFinishListener;

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

        cookController.setInteractRegisterListener(new Listener<MapCell>() {
            @Override
            public void tell(MapCell mapCell) {
                if (interactRegister(mapCell)) {
                    // Tell the current step if it's successful
                    tellStep(mapCell);
                }
            }
        });
        cookController.setServeListener(new Listener<Cook>() {
            @Override
            public void tell(Cook cook) {
                // Only if Cook is not null
                if (cook == null) return;

                // Get the top item
                Item cookItemTop = cook.heldItems.peek();
                // If it's not null, continue
                if (cookItemTop == null) return;

                // Then get the interactTarget of the Cook
                MapCell target = cook.getInteractTarget();
                // Make sure that the target is not null
                if (target == null) return;
                // And that it's also a register
                MapEntity targetEntity = target.getMapEntity();
                if (targetEntity == null || !targetEntity.getID().equals(Constants.REGISTER_ID)) return;

                // If all of that is valid, then send the item and cell to the CustomerController
                if (customerController.serve(target, cookItemTop)) {
                    // If it was successful, then remove the item from the cook
                    cook.takeItem();
                }
            }
        });

        customerController.setServedListener(new Listener<Customer>() {
            @Override
            public void tell(Customer customer) {
                // Tell the current step
                // If it's not valid, just spawn the customer again
                if (!tellStep(customer)) {
                    customerController.spawnCustomer(customer.getRequest());
                    return;
                }
                // Reset the display Customer if they match
                if (customer == displayCustomer) {
                    displayCustomer = null;
                }
                // Add the money
                money += customer.getRequest().getValue();
            }
        });

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
            if (!gameScreen.getScreenController().onScreen(Constants.GAME_SCREEN_ID)) return;
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
        request.setTime(-1);

        float textSpeed = 30f;

        // Add all the Tutorial steps
        tutorialSteps.add(new TutorialStep("Welcome to Piazza Panic: UnderCooked. Press Enter to continue.", textSpeed, false));
        tutorialSteps.add(new TutorialStep("This is a cooking game where you have to deliver what a Customer requests before they leave.\n(Enter)", textSpeed, false));
        tutorialSteps.add(new TutorialStep("To move around, use the W A S D keys. To swap between the different Cooks, press TAB or ENTER.\n(Enter)", textSpeed, false));
        tutorialSteps.add(new TutorialCustomerSpawnStep("Oh, a Customer has just entered the restaurant! Let's find out what they want.\n(Enter)", textSpeed, customerController, request));
        // tutorialSteps.add(new TutorialStationInteractStep(" Interact with the register that they're waiting at by pressing SPACE.", 0.5f, ));
        tutorialSteps.add(new TutorialRegisterInteractStep("Interact with the register next to the Customer by pressing SPACE to see their Request.", textSpeed, cookController));
        tutorialSteps.add(new TutorialStep("If you look at the bottom left of the screen, you can see the Customer's request. Here, they want a " + items.getItem(request.itemID).name + ". Now you need to make it.\n(Enter)", textSpeed, false));
        MapCell mapCell = map.getCell(7,11);
        MapEntity curEntity = mapCell.getMapEntity();
        tutorialSteps.add(new TutorialHighlightEntityStep("Go over to the Tomato pantry (highlighted), and press R to take one from it!\n(Enter)", textSpeed, curEntity));
        tutorialSteps.add(new TutorialCookHoldingStep("Get a Tomato. (R to pick up)", textSpeed, cookController, "<main>:tomato"));
        mapCell = map.getCell(7,8);
        curEntity = mapCell.getMapEntity();
        tutorialSteps.add(new TutorialStep("Pantries provide an infinite number of supplies, so don't be worried of running out.", textSpeed));
        tutorialSteps.add(new TutorialHighlightEntityStep("Now that you have a tomato, you need to cut it. Place it down on the cutting station below with V, and then cut the tomato with SPACE.\n(Enter)", textSpeed, curEntity));
        tutorialSteps.add(new TutorialCookHoldingStep("Cut the Tomato and pick it up. (V to put down, SPACE to cut, R to pick up)", textSpeed, cookController, "<main>:tomato_chopped"));
        tutorialSteps.add(new TutorialStep("Now that you know how to cut the tomato, you now need to repeat what you just did for the Lettuce and Onion.\n(Enter)", textSpeed));
        mapCell = map.getCell(3,8);
        curEntity = mapCell.getMapEntity();
        tutorialSteps.add(new TutorialHighlightEntityStep("After you cut both the Lettuce and Onion, put all 3 items down onto the Preparation Station." +
                " You then press SPACE to start preparing the salad.\n(Enter)", textSpeed, curEntity));
        tutorialSteps.add(new TutorialCookHoldingStep("Combine the Tomato, Lettuce and Onion to make a Salad.\n(V to put down, SPACE to prepare, R to pick up)", 20f, cookController, "<main>:salad"));
        mapCell = map.getCell(0,13);
        curEntity = mapCell.getMapEntity();
        tutorialSteps.add(new TutorialHighlightEntityStep("Now, you need to serve the Customer the salad.", textSpeed, curEntity));
        tutorialSteps.add(new TutorialCustomerServeStep("Place down the Salad on the Register that the Customer is waiting at to serve them. (V to put down)", textSpeed, cookController));
        tutorialSteps.add(new TutorialStep("In the game, Customers may have a time limit on how long they will wait. Make sure to serve them before this runs out!\n(Enter)", textSpeed));
        tutorialSteps.add(new TutorialStep("If you don't manage to serve a Customer, your reputation will go down. This can be viewed at the bottom right of the screen.\n(Enter)", textSpeed));

        tutorialSteps.add(new TutorialCustomerSpawnStep("Here comes another Customer. Try to serve them!", textSpeed, customerController, requestPool.get(1)));
        TutorialCustomerServeStep serveStep = new TutorialCustomerServeStep("", textSpeed, cookController);
        serveStep.setHasText(false);
        tutorialSteps.add(serveStep);

        tutorialSteps.add(new TutorialStep("Well done! Most of the Scenarios in this game follow a similar structure as this. Make sure to check the recipe, and then follow it.\n(Enter)", textSpeed));
        tutorialSteps.add(new TutorialStep("You might have noticed that you have received some money from completing the requests. There are two ways to use this.\n(Enter)", textSpeed));
        mapCell = map.getCell(0,1);
        curEntity = mapCell.getMapEntity();
        tutorialSteps.add(new TutorialHighlightEntityStep("The first is via the Phone Station. By spending money here, you can call Cooks back into the Piazza restaurant.\n(Enter)", textSpeed, curEntity));
        mapCell = map.getCell(4,0);
        curEntity = mapCell.getMapEntity();
        tutorialSteps.add(new TutorialHighlightEntityStep("The second is via disabled Stations. By spending money here, you can unlock a Station to be able to use it.\n(Enter)", textSpeed, curEntity));
        tutorialSteps.add(new TutorialStep("The price of both of these are displayed when you are looking at the Station.\n(Enter)", textSpeed));
        tutorialSteps.add(new TutorialStep("Scenarios may mess with recipes, or add completely new ones that you won't know about.\n(Enter)", textSpeed));
        tutorialSteps.add(new TutorialStep("You may also find power ups that appear on the floor as you play different Scenarios.\n(Enter)", textSpeed));
        tutorialSteps.add(new TutorialStep("Difficulty selection is also available, which increases the length of time Customers are willing to wait. On easier difficulties this is higher, and on the hardest difficulty there is no increase.\n(Enter)", textSpeed));
        tutorialSteps.add(new TutorialStep("That's all you need to know to start playing. Good luck!\n(Enter)", textSpeed));
    }

    private boolean tellStep(Object value) {
        if (currentStep.getClass().equals(TutorialRegisterInteractStep.class) && value.getClass().equals(MapCell.class)) {
            currentStep.finished();
            return true;
        }
        if (currentStep.getClass().equals(TutorialCustomerServeStep.class) && value.getClass().equals(Customer.class)) {
            currentStep.finished();
            return true;
        }
        return false;
    }

    @Override
    public boolean interactRegister(MapCell mapCell) {
        if (super.interactRegister(mapCell)) {
            tellStep(mapCell);
            return true;
        }
        return false;
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

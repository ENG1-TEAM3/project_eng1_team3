package com.undercooked.game.food.interactions;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.food.Ingredients;
import com.undercooked.game.food.interactions.steps.SetStep;
import com.undercooked.game.food.interactions.steps.WaitStep;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.json.JsonFormat;

public class Interactions {

    private class InteractionObject {
        Array<String> ingredients;
        Array<InteractionStep> steps;

        public InteractionObject(Array<InteractionStep> steps, Array<String> ingredients) {
            this.steps = steps;
            this.ingredients = ingredients;
        }
    }

    /** A mapping of stationID to the interactionIDs */
    private ObjectMap<String, Array<String>> stationInteractions;
    /** A mapping of interactionID to the interactions + the ingredients needed */
    private ObjectMap<String, InteractionObject> interactions;

    public void loadInteractionAsset(String assetPath, Ingredients ingredients) {
        JsonValue interactionRoot = FileControl.loadJsonAsset(assetPath, "interactions");
        // If it's not null...
        if (interactionRoot != null) {
            // load the interaction
            loadInteraction(interactionRoot, ingredients);
        }
    }

    private Array<InteractionStep> addInteractions(JsonValue interactionRoot, Ingredients ingredients) {
        Array<InteractionStep> steps = new Array<>();
        // Loop through the interactions
        for (JsonValue interaction : interactionRoot.iterator()) {
            // Add this interaction
            InteractionStep thisStep = addInteraction(interactionRoot, ingredients);
            if (thisStep == null) {
                return null;
            }
        }
        return steps;
    }

    private InteractionStep addInteraction(JsonValue interactionRoot, Ingredients ingredients) {
        InteractionStep interactionStep = null;
        // Get the interaction step type
        String type = interactionRoot.getString("type");
        String value = interactionRoot.getString("value");
        // Depending on the type, initialise interactionStep based on that
        // Depending on the instruction, it may also need to load an ingredient
        // If it can't load it, then return null
        if (type == "wait") {
            interactionStep = new WaitStep();
        } else if (type == "set") {
            interactionStep = new SetStep();
            // If it fails to load, return null as it could mess up the
            // whole interaction
            if (!ingredients.addIngredientAsset(value)) {
                return null;
            }
        }

        // Set the values of the interactionStep
        interactionStep.time = (float) interactionRoot.getDouble("time");
        interactionStep.sound = interactionRoot.getString("sound");


        // If there are success interactions, add those
        if (interactionRoot.get("success").size > 0) {
            interactionStep.success = addInteractions(interactionRoot.get("success"), ingredients);
            // If it returns null, return null.
            if (interactionStep.success == null) {
                return null;
            }
        }
        // Repeat for failure
        if (interactionRoot.get("failure").size > 0) {
            interactionStep.failure = addInteractions(interactionRoot.get("failure"), ingredients);
            // If it returns null, return null.
            if (interactionStep.failure == null) {
                return null;
            }
        }
        return interactionStep;
    }

    private void loadInteraction(JsonValue interactionRoot, Ingredients ingredients) {
        // Make sure it's formatted correctly
        JsonFormat.formatJson(interactionRoot, Constants.DefaultJson.interactionFormat());
        // Check for ID
        String interactionID = interactionRoot.getString("id");
        Array<InteractionStep> out;
        Array<String> neededIngredients;
        if (interactionID != null) {
            neededIngredients = new Array<>();
            // If the id is there, then check the items. They should be an array of strings.
            for (JsonValue intID : interactionRoot.get("items")) {
                String id = intID.toString();
                if (id != null) {
                    // If the id is not null, then add it to the ingredients array.
                    // Repeats are allowed, as recipes may need multiple of the same item.
                    neededIngredients.add(id);
                } else {
                    // If it fails to load, return as it could mess up the
                    // whole interaction
                    return;
                }
            }

            // Then, Make sure that there's at least one step.
            // Loop through all the Interactions recursively in order to get them all stored in the array
            out = addInteractions(interactionRoot.get("interactions"), null);
        } else {
            return;
        }
        // Finally, if it all loaded well, add it to the ObjectMap(s).
        // If the station map doesn't exist yet, add it
        if (!stationInteractions.containsKey(interactionID)) {
            stationInteractions.put(interactionID, new Array<String>());
        }
        // Add the id
        stationInteractions.get(interactionID).add(interactionID);

        // Then do the same for the interaction itself
        interactions.put(interactionID, new InteractionObject(out, neededIngredients));
    }

    public Array<String> getStationInteractions(String stationID) {
        return stationInteractions.get(stationID);
    }

    public InteractionObject getInteractionSteps(String interactionID) {
        return interactions.get(interactionID);
    }

}

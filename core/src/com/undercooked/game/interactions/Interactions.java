package com.undercooked.game.interactions;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.food.Items;
import com.undercooked.game.interactions.steps.*;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.json.JsonFormat;

public class Interactions {

    /** A mapping of stationID to the interactionIDs */
    private ObjectMap<String, Array<String>> stationInteractions;
    /** A mapping of interactionID to the interactions + the ingredients needed */
    private ObjectMap<String, InteractionObject> interactions;

    public Interactions() {
        this.stationInteractions = new ObjectMap<>();
        this.interactions = new ObjectMap<>();
    }

    public void loadInteractionAsset(String assetPath, Items items) {
        JsonValue interactionRoot = FileControl.loadJsonAsset(assetPath, "interactions");
        // If it's not null...
        if (interactionRoot != null) {
            // load the interaction
            loadInteraction(assetPath, interactionRoot, items);
        }
    }

    private Array<InteractionStep> addInteractions(JsonValue interactionRoot, Items items) {
        Array<InteractionStep> steps = new Array<>();
        // Loop through the interactions
        for (JsonValue interaction : interactionRoot.iterator()) {
            // Add this interaction
            InteractionStep thisStep = addInteraction(interaction, items);
            if (thisStep == null) {
                System.out.println("Failed on step: " + interaction.getString("type"));
                return null;
            }
            steps.add(thisStep);
        }
        return steps;
    }

    private InteractionStep addInteraction(JsonValue interactionRoot, Items items) {
        InteractionStep interactionStep = null;
        // Get the interaction step type
        String type = interactionRoot.getString("type");
        String value = interactionRoot.getString("value");
        // Depending on the type, initialise interactionStep based on that
        // Depending on the instruction, it may also need to load an ingredient
        // If it can't load it, then return null
        switch (type) {
            case "wait":
                interactionStep = new WaitStep();
                break;
            case "set":
                interactionStep = new SetStep();
                // If it fails to load, return null as it could mess up the
                // whole interaction
                if (items.addItemAsset(value) == null) {
                    System.out.println("Set failed to load: " + value + " does not exist.");
                    return null;
                }
                break;
            case "give":
                interactionStep = new GiveStep();
                // If it fails to load, return null as it could mess up
                // the whole interaction
                if (items.addItemAsset(value) == null) {
                    System.out.println("Give failed to load: " + value + " does not exist.");
                    return null;
                }
                break;

            // Inputs
            case "pressed":
                interactionStep = new PressedStep();
                break;
            case "just_pressed":
                interactionStep = new JustPressedStep();
                break;
            case "released":
                interactionStep = new ReleasedStep();
            default:
                // If it is none of the above cases, then return null
                // as the interaction has failed to load
                return null;
        }

        // Set the values of the interactionStep
        interactionStep.time = interactionRoot.getFloat("time");
        interactionStep.sound = interactionRoot.getString("sound");
        interactionStep.value = interactionRoot.getString("value");

        // If there are success interactions, add those
        if (interactionRoot.get("success").size > 0) {
            interactionStep.success = addInteractions(interactionRoot.get("success"), items);
            // If it returns null, return null.
        }
        // Repeat for failure
        if (interactionRoot.get("failure").size > 0) {
            interactionStep.failure = addInteractions(interactionRoot.get("failure"), items);
            // If it returns null, return null.
        }
        return interactionStep;
    }

    private void loadInteraction(String interactionID, JsonValue interactionRoot, Items items) {
        // Make sure it's formatted correctly
        JsonFormat.formatJson(interactionRoot, Constants.DefaultJson.interactionFormat());
        // Check for ID
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
                    // Try to load this needed ingredient.
                    if (items.addItemAsset(id) == null) {
                        // If it fails, then interaction can't work, so don't
                        // save it.
                        return;
                    }
                } else {
                    // If it fails to load, return as it could mess up the
                    // whole interaction
                    return;
                }
            }

            // Then, Make sure that there's at least one step.
            // Loop through all the Interactions recursively in order to get them all stored in the array
            out = addInteractions(interactionRoot.get("steps"), items);
        } else {
            return;
        }
        // If out is null, then return
        if (out == null) {
            return;
        }
        String stationID = interactionRoot.getString("station_id");
        // Finally, if it all loaded well, add it to the ObjectMap(s).
        // If the station map doesn't exist yet, add it
        if (!stationInteractions.containsKey(stationID)) {
            stationInteractions.put(stationID, new Array<String>());
        }
        // Add the id
        stationInteractions.get(stationID).add(interactionID);

        // Then do the same for the interaction itself
        interactions.put(interactionID, new InteractionObject(out, neededIngredients));

        /*System.out.println(interactions);
        for (ObjectMap.Entry<String, Array<String>> interaction : stationInteractions) {
            System.out.println(interaction.key);
            System.out.println(interaction.value);
        }*/
    }

    /**
     * Unloads all the loaded interactions
     */
    public void unload() {
        stationInteractions.clear();
        for (InteractionObject intObj : interactions.values()) {
            intObj.unload();
        }
    }

    public Array<String> getStationInteractions(String stationID) {
        return stationInteractions.get(stationID);
    }

    public InteractionObject getInteractionSteps(String interactionID) {
        return interactions.get(interactionID);
    }

}
package com.undercooked.game.interactions;

import com.undercooked.game.entity.cook.Cook;

/**
 * The possible results of the {@link InteractionStep}s.
 * Used by the {@link Cook} to
 * know what to do after sending an input to the interaction.
 */
public enum InteractResult {
    /**
     * Stop checking inputs.
     */
    STOP,
    /**
     * Check over all inputs again from the start.
     * <br>Should be used if you want to check input on a
     * following interaction immediately.
     */
    RESTART,
    /** Skip checking interactions for this interaction. */
    SKIP,
    /**
     * Continue checking inputs.
     */
    NONE
}

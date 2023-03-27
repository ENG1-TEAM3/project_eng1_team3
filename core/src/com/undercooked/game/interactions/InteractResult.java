package com.undercooked.game.interactions;

/**
 * The possible results of the {@link InteractionStep}s.
 * Used by the {@link com.undercooked.game.entity.Cook} to
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

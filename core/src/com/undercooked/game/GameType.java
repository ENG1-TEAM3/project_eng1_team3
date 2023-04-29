package com.undercooked.game;

/**
 * The different game modes that
 * can be played.
 */
public enum GameType {
    /**
     * Scenario mode, where a set number of
     * {@link com.undercooked.game.entity.customer.Customer}s have
     * to be served before time runs out.
     */
    SCENARIO,
    /**
     * Endless mode, where the player has to serve as many
     * {@link com.undercooked.game.entity.customer.Customer}s before
     * they run out of reputation.
     */
    ENDLESS
}
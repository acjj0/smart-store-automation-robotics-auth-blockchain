package com.cscie97.store.model;

/**
 * Enum to create standard and consistent shelf and product temperature info
 */
public enum TemperatureEnum {
    /**
     * Frozen temperatures
     */
    FROZEN,

    /**
     * Refrigerated but not frozen
     */
    REFRIGERATED,

    /**
     * Room temperatures in the location
     */
    AMBIENT,

    /**
     * Warm but not hot
     */
    WARM,

    /**
     * Hot temperature
     */
    HOT
}

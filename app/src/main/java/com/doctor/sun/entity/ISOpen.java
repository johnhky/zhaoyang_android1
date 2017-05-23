package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by heky on 17/5/12.
 */

public class ISOpen {
    @JsonProperty("network")
    private boolean network;
    @JsonProperty("simple")
    private boolean simple;
    @JsonProperty("surface")
    private boolean surface;


    public boolean isNetwork() {
        return network;
    }

    public void setNetwork(boolean network) {
        this.network = network;
    }

    public boolean isSurface() {
        return surface;
    }

    public void setSurface(boolean surface) {
        this.surface = surface;
    }

    public boolean isSimple() {
        return simple;
    }

    public void setSimple(boolean simple) {
        this.simple = simple;
    }
}

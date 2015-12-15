package com.platform.iot.api.balancing;

/**
 * Class attached to a table to tell about migration request and allocated server
 *
  */
public class Migration {

    private boolean started = false;
    private Server server;

    /**
     * Was migration requested for the table holding this?
     *
     * @return
     */
    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    /**
     * The server allocated during migration
     *
     * @return
     */
    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}

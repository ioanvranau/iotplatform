package com.platform.iot.api.balancing;

/**
 * Each server in cluster has one instance of this
 * The address and port gives the uniqueness of this
 *
 */
public class Server {

    private String address;
    private int port;
    private int nbUsers;
    private ClientVersion minClientVersionSupported;

    public Server(String address, int port, ClientVersion clientVersion) {
        this.address = address;
        this.port = port;
        this.minClientVersionSupported = clientVersion;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Returns the number of real active users on this server
     *
     * @return
     */
    public int getNbUsers() {
        return nbUsers;
    }

    public void setNbUsers(int nbUsers) {
        this.nbUsers = nbUsers;
    }

    /**
     * Returns the minimum version of client that this server can handle
     *
     * @return
     */
    public ClientVersion getMinClientVersionSupported() {
        return minClientVersionSupported;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Server server = (Server) o;

        if (port != server.port) return false;
        if (address != null ? !address.equals(server.address) : server.address != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = address != null ? address.hashCode() : 0;
        result = 31 * result + port;
        return result;
    }

    @Override
    public String toString() {
        return "Server{" +
                "address='" + address + '\'' +
                ", port=" + port +
                ", minClientVersionSupported=" + minClientVersionSupported +
                '}';
    }
}

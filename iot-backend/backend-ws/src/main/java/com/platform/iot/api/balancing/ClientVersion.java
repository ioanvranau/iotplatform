package com.platform.iot.api.balancing;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Class used for the version of clients connecting to server
 * <p/>
 */
public class ClientVersion implements Comparable<ClientVersion> {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(LoadBalancer.class);
    /*
     * array of 3 integer holding the version: major, minor, patch
     */
    private int[] version = new int[3];

    private ClientVersion() {
        Arrays.fill(version, 0);
    }

    /**
     * Create an instance from a string if format major.minor.patch
     *
     * @param versionString the version as string
     * @return a new instance of client version
     */
    public static ClientVersion fromString(String versionString) {
        if (StringUtils.isBlank(versionString)) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(versionString, ".");
        if (st.countTokens() > 3) {
            logger.error("incorrect client version provided: " + versionString);
            return null;
        }
        ClientVersion clientVersion = new ClientVersion();
        for (int i = 0; i < 3; i++) {
            if (!st.hasMoreTokens()) {
                break;
            }
            String value = st.nextToken();
            if (!NumberUtils.isNumber(value)) {
                logger.error("client version contains not number values: " + versionString);
                return null;
            }
            clientVersion.version[i] = NumberUtils.createInteger(value);
        }

        return clientVersion;
    }

    /**
     * Compares to client version
     *
     * @param other client version to compare with
     * @return 1 if this greater than other, -1 if less than other or 0 if equal
     */
    @Override
    public int compareTo(ClientVersion other) {
        if (getMajor() > other.getMajor()) {
            return 1;
        } else if (getMajor() > other.getMajor()) {
            return -1;
        } else if (getMajor() == other.getMajor()) {
            if (getMinor() > other.getMinor()) {
                return 1;
            } else if (getMinor() < other.getMinor()) {
                return -1;
            } else if (getMinor() == other.getMinor()) {
                if (getPatch() > other.getPatch()) {
                    return 1;
                } else if (getPatch() < other.getPatch()) {
                    return -1;
                }
            }
        }

        return 0;
    }

    /**
     * Returns the major version
     *
     * @return return the major version
     */
    public int getMajor() {
        return version[0];
    }

    /**
     * Returns the minor version
     *
     * @return
     */
    public int getMinor() {
        return version[1];
    }

    /**
     * returns the patch version
     *
     * @return
     */
    public int getPatch() {
        return version[2];
    }

    /**
     * Transforms to string this client version, in format major.minor.patch
     *
     * @return
     */
    @Override
    public String toString() {
        return getMajor() + "." + getMinor() + "." + getPatch();
    }
}


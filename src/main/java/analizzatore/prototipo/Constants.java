package analizzatore.prototipo;

import java.util.*;

/**
 * Created by Antonio on 20/08/2016.
 */
public final class Constants {
    private Constants(){

    }

    public final static Set<String> DHCP_TRANSITIONS = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
            "DHCP Discover",
            "DHCP Offer",
            "DHCP ACK",
            "DHCP NACK",
            "DHCP Request",
            "DHCP Decline"
            )));

    public final static Set<String> DHCP_STATES = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
            "Init",
            "Selecting",
            "Requesting",
            "Bound",
            "Renewing",
            "Rebiding"
    )));

    public final static Set<String> HTTP_TRANSITIONS = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
            "DHCP Discover",
            "DHCP Offer"
    )));

    public static final String DHCP_PROTOCOL_NAME = "DHCP";
}

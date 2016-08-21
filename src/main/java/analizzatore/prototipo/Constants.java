package analizzatore.prototipo;

import com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

    static final ImmutableMap<String,String> DHCP_EXTRAINFO = new ImmutableMap.Builder<String,String>()
            .put("Init-DHCP Discover","Inviato messeggio di scoperta")
            .put("Selecting-DHCP Offer","Raccolta delle offerte")
            .put("Selecting-DHCP Request","Selezione dell'offerta")
            .put("Requesting-DHCP ACK","Registrazione del lease. Impostazione dei timer T1 e T2")
            .put("Requesting-DHCP Decline","Ack positivo ma non valido. Invio di un messagio di decline.")
            .put("Bound-DHCP Offer","Rifiuto dell'offerta dal momento che si è già in bound.")
            .put("Bound-DHCP ACK","Rifiuto dell'ACK dal momento che si è già in bound.")
            .put("Bound-DHCP NACK","Rifiuto del NACK dal momento che si è già in bound.")
            .put("Bound-DHCP Request","Il timer T1 è scaduto. Invio della richiesta al server di leasing")
            .put("Renewing-DHCP ACK","Registrazione del leae. Impostazione dei timer T1 e T2")
            .put("Renewing-DHCP Request","Il timer T2 è scaduto. Invio in broadcast della richiesta")
            .put("Renewing-DHCP NACK","Stop della rete.")
            .put("Rebinding-DHCP NACK","Lease scaduto. Stop della rete.")
            .put("Rebinding-DHCP ACK","Registrazione del lease. Impostazione dei timer T1 e T2")
            .build();

    public final static Set<String> HTTP_TRANSITIONS = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
            "DHCP Discover",
            "DHCP Offer"
    )));

    public static final String DHCP_PROTOCOL_NAME = "DHCP";
}

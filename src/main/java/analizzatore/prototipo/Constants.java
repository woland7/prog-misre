package analizzatore.prototipo;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * Created by Antonio on 20/08/2016.
 */
public final class Constants {
    private Constants(){

    }

    public static final String DHCP_PROTOCOL_NAME = "DHCP";

    public static final String DHCP_RESOURCE = "DHCP.scxml";

    public final static ImmutableSet<String> DHCP_TRANSITIONS = new ImmutableSet.Builder<String>()
            .add("DHCP Discover")
            .add("DHCP Offer")
            .add("DHCP ACK")
            .add("DHCP NACK")
            .add("DHCP Request")
            .add("DHCP Decline")
            .build();

    public final static ImmutableSet<String> DHCP_STATES = new ImmutableSet.Builder<String>()
            .add("Init")
            .add("Selecting")
            .add("Requesting")
            .add("Bound")
            .add("Renewing")
            .add("Rebinding")
            .build();

    static final ImmutableMap<String,String> DHCP_EXTRAINFO = new ImmutableMap.Builder<String,String>()
            .put("Init-DHCP Discover","Inviato messeggio di scoperta")
            .put("Selecting-DHCP Offer","Raccolta delle offerte")
            .put("Selecting-DHCP Request","Selezione dell'offerta")
            .put("Requesting-DHCP ACK","Registrazione del lease. Impostazione dei timer T1 e T2")
            .put("Requesting-DHCP Decline","ACK positivo ma non valido. Invio di un messagio di Decline.")
            .put("Bound-DHCP Offer","Rifiuto dell'offerta poiché si è già in bound.")
            .put("Bound-DHCP ACK","Rifiuto dell'ACK poiché si è già in bound.")
            .put("Bound-DHCP NACK","Rifiuto del NACK poiché si è già in bound.")
            .put("Bound-DHCP Request","Il timer T1 è scaduto. Invio della richiesta al server di leasing")
            .put("Renewing-DHCP ACK","Registrazione del lease. Impostazione dei timer T1 e T2")
            .put("Renewing-DHCP Request","Il timer T2 è scaduto. Invio in broadcast della richiesta")
            .put("Renewing-DHCP NACK","Stop della rete.")
            .put("Rebinding-DHCP NACK","Lease scaduto. Stop della rete.")
            .put("Rebinding-DHCP ACK","Registrazione del lease. Impostazione dei timer T1 e T2")
            .build();

    public static final String HTTP_PROTOCOL_NAME = "HTTP";

    public static final String HTTP_RESOURCE = "HTTP.scxml";

    public final static ImmutableSet<String> HTTP_TRANSITIONS = new ImmutableSet.Builder<String>()
            .add("GET")
            .add("POST")
            .add("PUT")
            .add("HEAD")
            .add("DELETE")
            .add("(10[0-3])")
            .add("(20[0-6])")
            .add("(30[0-8])")
            .add("(40[0-9]|41[0-7])")
            .add("(50[0-9]|51[0-1])")
            .build();

    public final static ImmutableSet<String> HTTP_STATES = new ImmutableSet.Builder<String>()
            .add("NeedRequest")
            .add("SentRequest")
            .add("Information")
            .add("Successful")
            .add("Redirection")
            .add("ClientError")
            .add("ServerError")
            .build();
}

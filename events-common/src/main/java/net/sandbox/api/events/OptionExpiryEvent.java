package net.sandbox.api.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.sandbox.api.domain.OptionEvent;

import java.util.UUID;

/**
 * Created by eagleeyeyuy on 26/4/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OptionExpiryEvent implements OptionEvent {

    private String uuid = UUID.randomUUID().toString();

    @Override
    public String toString() {
        return "OptionExpiryEvent{" +
                "uuid='" + uuid + '\'' +
                '}';
    }
}

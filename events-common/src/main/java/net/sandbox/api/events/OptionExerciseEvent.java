package net.sandbox.api.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.sandbox.api.domain.OptionEvent;

import java.util.UUID;

/**
 * Created by eagleeyeyuy on 2/5/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OptionExerciseEvent implements OptionEvent {

    @JsonProperty
    private String uuid = UUID.randomUUID().toString();

    @Override
    public String toString() {
        return "OptionExerciseEvent{" +
                "uuid='" + uuid + '\'' +
                '}';
    }
}

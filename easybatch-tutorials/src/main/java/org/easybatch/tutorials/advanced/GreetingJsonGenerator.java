package org.easybatch.tutorials.advanced;

import org.easybatch.core.api.RecordProcessor;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;

/**
 * A record processor that generates greeting data in JSON format.
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
public class GreetingJsonGenerator implements RecordProcessor<Greeting, Greeting> {

    @Override
    public Greeting processRecord(Greeting greeting) throws Exception {

        JsonWriter jsonWriter = Json.createWriter(System.out);
        JsonObject greetingModel = Json.createObjectBuilder()
                .add("id", greeting.getId())
                .add("name", greeting.getName())
                .build();

        jsonWriter.writeObject(greetingModel);

        return greeting;

    }

}

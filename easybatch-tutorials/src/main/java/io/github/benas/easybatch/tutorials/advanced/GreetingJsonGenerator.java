package io.github.benas.easybatch.tutorials.advanced;

import io.github.benas.easybatch.core.api.AbstractRecordProcessor;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;

/**
 * A record processor that generates greeting data in JSON format.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class GreetingJsonGenerator extends AbstractRecordProcessor<Greeting> {

    @Override
    public void processRecord(Greeting greeting) throws Exception {

        JsonWriter jsonWriter = Json.createWriter(System.out);
        JsonObject greetingModel = Json.createObjectBuilder()
                .add("sequence", greeting.getSequence())
                .add("name", greeting.getName())
                .build();

        jsonWriter.writeObject(greetingModel);

    }

}

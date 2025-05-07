package org.ksm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

@Path("/excercise/deepMerge")
public class DeepMergeResource {

    @Inject
    HeroRepository heroRepository;

    @Inject
    ObjectMapper objectMapper;

    /*
     * Recieving an input string json
     * Pulling a json from db
     * Converting the string json to merge with json from db
     * Returning an updated string json
     */
    @PATCH
    @Path("/{heroId}")
    @Transactional
    @Consumes("application/json")
    @Produces("application/json")
    public String updatHero(@PathParam("heroId") String heroId, String updatedHero) {
        //log.infof("Updating hero with heroId = %s", heroId);
        Hero sourceHero = heroRepository.findExistingById(heroId);
        
        try {
            JsonNode sourceNode = objectMapper.valueToTree(sourceHero);
            JsonNode updateNode = objectMapper.readTree(updatedHero);
            JsonNode mergedNode = deepMergeJsonNodes(sourceNode, updateNode);

            Hero mergedHero = objectMapper.treeToValue(mergedNode, Hero.class);

            sourceHero.setAlias(mergedHero.getAlias());
            sourceHero.setName(mergedHero.getName());
            sourceHero.setCanFly(mergedHero.getCanFly());

            return sourceHero.toString();
        } catch (JsonProcessingException e) {
            //log.errorf("Error merging heroes: %s", e.getMessage());
            throw new BadRequestException("Invalid JSON format in updatedHero");
        }
    }
    
    private JsonNode deepMergeJsonNodes(JsonNode source, JsonNode update) {

        if (source == null || source.isNull()) return update;
        if (update == null || update.isNull()) return source;
        if (!source.isObject() || !update.isObject()) return update;

        // result obj
        ObjectNode merged = objectMapper.createObjectNode();

        // copy 'source' jsonNode to 'merged' obj
        source.fields().forEachRemaining(entry -> merged.set(entry.getKey(), entry.getValue()));

        // apply any updates (recursive merge for nested objects)
        update.fields().forEachRemaining(entry -> {

            // for each entry in the update jsonNode, declare and set the key/value variables externally
            String key = entry.getKey();
            JsonNode value = entry.getValue();

            // if the key exists, value from source node is an obj (implying nest), value from update node is an obj/nest
            if (merged.has(key) && merged.get(key).isObject() && value.isObject()) {
                // if the value is nested, set the key and recall this method (recursive)
                merged.set(key, deepMergeJsonNodes(merged.get(key), value));
            } else {
                // if the value is not nested, replace the value, or append the key/value
                merged.set(key, value);
            }
        });

        return merged;
    }
}    
package oogasalad.file_manager.runner_file_manager.runner_parser.records;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import oogasalad.runner.model.card.group.CardGroup;
import oogasalad.runner.model.player.Player;

public class PlayerInterfaceAdapter implements JsonSerializer<Player>,
    JsonDeserializer<Player> {
  @Override
  public JsonElement serialize(Player src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject obj = new JsonObject();
    obj.addProperty("type", src.getClass().getName());
    obj.add("data", context.serialize(src));
    return obj;
  }

  @Override
  public Player deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    JsonObject obj = jsonElement.getAsJsonObject();
    String className = obj.get("type").getAsString();
    try {
      Class<?> clazz = Class.forName(className);
      return jsonDeserializationContext.deserialize(obj.get("data"), clazz);
    } catch (ClassNotFoundException e) {
      throw new JsonParseException(e);
    }  }
}

package oogasalad.runner.view.assistance.chat_gpt;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import oogasalad.runner.view.assistance.ExternalAssistant;
import oogasalad.runner.view.assistance.Message;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


/**
 * This class implements the ExternalAssistant interface using the ChatGPT API to generate responses
 * to user prompts. It sends user prompts to the ChatGPT API, parses the response, and returns it to
 * the user. The conversation log keeps track of previous user prompts and generated responses. The
 * JSON file containing the game ruleset is used to provide accurate and helpful responses to user
 * prompts related to game rules and mechanics. To use this class, create an instance and load the
 * current game's ruleset JSON file. Call the provideAdvice method to get a response from the
 * ChatGPT API based on the user prompt. The conversation log can be accessed through the
 * conversationLog instance variable.
 *
 * @author Ted
 */
public class ChatGPTExternalAssistant implements ExternalAssistant {

  private static final String API_URL = "https://api.openai.com/v1/chat/completions";
  private static final String API_KEY = "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";
  private static final String ParameterTextFilePath = "src\\main\\java\\oogasalad\\runner\\view\\assistance\\chat_gpt\\ExternalAssistanceRoleTrainingText.txt";
  private final List<Message> conversationLog = new ArrayList<>();

  public ChatGPTExternalAssistant(String filePathToCurrentGameJson) {
    //conversationLog.add(new Message("system", getPrompt(ParameterTextFilePath)));

    //loadCurrentGameRulesetJson(filePathToCurrentGameJson);
  }


  @Override
  public String provideAdvice(String userPrompt) {
    CloseableHttpClient client = HttpClients.createDefault();
    System.out.println("Requesting external assistance..");
    String generatedText = null;
    try {
      HttpPost httpPost = new HttpPost(API_URL);
      httpPost.setHeader("Authorization", "Bearer " + API_KEY);
      httpPost.setHeader("Content-Type", "application/json");
      httpPost.setEntity(createRequestPayloadEntity(userPrompt));
      CloseableHttpResponse response = client.execute(httpPost);
      HttpEntity responseEntity = response.getEntity();
      String responseBody = EntityUtils.toString(responseEntity);
      generatedText = parseResponse(responseBody, userPrompt);
    } catch (IOException e) {
      System.err.println("Error occurred while requesting external assistance.");
      e.printStackTrace();
    } finally {
      try {
        client.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return generatedText;
  }

  private String parseResponse(String responseBody, String userPrompt) {
    Gson gson = new Gson();
    JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
    JsonArray choices = jsonResponse.getAsJsonArray("choices");
    JsonObject choice = choices.get(0).getAsJsonObject();
    JsonObject message = choice.getAsJsonObject("message");
    String generatedText = message.get("content").getAsString();
    conversationLog.remove(conversationLog.size() - 1);
    conversationLog.add(
        new Message("user", "Prior User Query In Current Conversation: " + userPrompt));
    conversationLog.add(new Message("system", "Given Answer: " + generatedText));
    System.out.println("User Prompt: " + userPrompt);
    System.out.println("Generated Text: " + generatedText);
    return generatedText;
  }

  public StringEntity createRequestPayloadEntity(String userPrompt) {
    // Add the latest user prompt to the conversation
    conversationLog.add(new Message("user", "Current User Prompt: " + userPrompt));

    JsonArray messages = new JsonArray();
    for (Message message : conversationLog) {
      JsonObject messageObj = new JsonObject();
      messageObj.addProperty("role", message.getRole());
      messageObj.addProperty("content", message.getContent());
      messages.add(messageObj);
    }

    JsonObject requestBody = new JsonObject();
    requestBody.addProperty("model", "gpt-3.5-turbo");
    requestBody.add("messages", messages);
    requestBody.addProperty("temperature", 0.5);
    requestBody.addProperty("top_p", .5);
    requestBody.addProperty("n", 1);
    requestBody.addProperty("stream", false);
    //requestBody.put("stop", stopSequences);
    requestBody.addProperty("max_tokens", 300);

    // Add other parameters if needed

    // Convert the JsonObject to a JSON string
    Gson gson = new Gson();
    String jsonString = gson.toJson(requestBody);

    // Create a StringEntity from the JSON string
    StringEntity requestEntity = new StringEntity(jsonString, StandardCharsets.UTF_8);
    requestEntity.setContentType("application/json");

    return requestEntity;
  }

  private String getPrompt(String filePath) {
    StringBuilder contentBuilder = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = br.readLine()) != null) {
        contentBuilder.append(line).append("\n");
      }
      return contentBuilder.toString();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void loadCurrentGameRulesetJson(String filePath) {
    try {
      conversationLog.add(new Message("system",
          new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8)
              + " /n Now that the rules for the game have been loaded, please feel free to ask any questions related to the game's rules and mechanics. I'm here to help and will do my best to provide accurate and helpful responses based ONLY ON  WHAT I KNOW FROM THE GAME RULE JSON FILE!!!!!!"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}

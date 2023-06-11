package oogasalad.runner.view.assistance;

/**
 * A class that represents a message in a conversation log. Each message has a role (either "user"
 * or "system") and content (the text of the message).
 *
 * @author Ted
 */

public class Message {

  private String role;
  private String content;

  public Message(String role, String content) {
    this.role = role;
    this.content = content;

  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
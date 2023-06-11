package oogasalad.runner.model.condition;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import oogasalad.i18n.AppType;
import oogasalad.i18n.translators.DefaultTranslator;
import oogasalad.i18n.translators.Translator;
import oogasalad.runner.model.card.Card;
import oogasalad.runner.model.card.StandardCard;
import oogasalad.runner.model.card.StandardCard.StandardSuit;
import oogasalad.runner.model.card.StandardCard.StandardValue;
import oogasalad.runner.model.card.group.CardGroup;
import oogasalad.runner.model.card.group.ConcreteCardGroup;
import org.junit.jupiter.api.Test;

public class RunConditionTest {
  @Test
  void test() {
    CardGroup group = new ConcreteCardGroup("test", CardGroup.ViewType.SEQUENCE, CardGroup.Direction.FACE_UP);
    RunCondition condition = new RunCondition(Map.of("group_tag", ""), null);
    group.add(new StandardCard(StandardSuit.CLUB, StandardValue.ACE));
    Card card3 = new StandardCard(StandardSuit.CLUB, StandardValue.KING);
    Card card4 = new StandardCard(StandardSuit.CLUB, StandardValue.QUEEN);
    group.add(card3);
    group.add(card4);
    assertTrue(condition.isValid(group));

    group.remove(card3);
  }

  @Test
  void testExceptionMessage() {
    CardGroup group = new ConcreteCardGroup("test", CardGroup.ViewType.SEQUENCE, CardGroup.Direction.FACE_UP);
    RunCondition condition = new RunCondition(Map.of("group_tag", "set"), null);
    Translator translator = new DefaultTranslator(AppType.RUNNER, "English");
    assertEquals("All sets must be runs", translator.translateToUserSpace(condition.description()));
  }
}

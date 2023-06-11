package oogasalad.runner.model.card.group;

import static org.junit.jupiter.api.Assertions.*;

import oogasalad.runner.model.card.Card;
import oogasalad.runner.model.card.StandardCard;
import oogasalad.runner.model.card.StandardCard.StandardSuit;
import oogasalad.runner.model.card.StandardCard.StandardValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConcreteCardGroupTest {
  ConcreteCardGroup group;

  @BeforeEach
  void setUp() {
    group = new ConcreteCardGroup();
  }

  @Test
  void testAddAndRemoveCard() {
    Card card1 = new StandardCard(StandardSuit.CLUB, StandardValue.ACE);
    Card card2 = new StandardCard(StandardSuit.CLUB, StandardValue.TWO);
    group.add(card1);
    assertTrue(group.contains(card1));
    assertEquals(1, group.size());
    group.add(card2);
    assertTrue(group.contains(card2));
    assertEquals(2, group.size());
    group.remove(card1);
    assertFalse(group.contains(card1));
    assertEquals(1, group.size());
  }

  @Test
  void testDrawCard() {
    Card card1 = new StandardCard(StandardSuit.CLUB, StandardValue.ACE);
    Card card2 = new StandardCard(StandardSuit.CLUB, StandardValue.TWO);
    group.add(card1);
    group.add(card2);
    assertEquals(card2, group.draw());
    assertEquals(1, group.size());
    assertEquals(card1, group.draw());
    assertEquals(0, group.size());
    group.add(card1);
    group.add(card2);
    assertEquals(card1, group.draw(1));
    assertEquals(1, group.size());
    assertEquals(card2, group.draw(0));
    assertEquals(0, group.size());
  }

  @Test
  void testCardAt() {
    Card card1 = new StandardCard(StandardSuit.CLUB, StandardValue.ACE);
    Card card2 = new StandardCard(StandardSuit.CLUB, StandardValue.TWO);
    group.add(card1);
    group.add(card2);
    assertEquals(card2, group.cardAt(0));
    assertEquals(card1, group.cardAt(1));
  }
  @Test
  void testIndexOf() {
    Card card1 = new StandardCard(StandardSuit.CLUB, StandardValue.ACE);
    Card card2 = new StandardCard(StandardSuit.CLUB, StandardValue.TWO);
    group.add(card1);
    group.add(card2);
    assertEquals(0, group.indexOf(card2));
    assertEquals(1, group.indexOf(card1));
  }
  @Test
  void testIsEmpty() {
    assertTrue(group.isEmpty());
    Card card1 = new StandardCard(StandardSuit.CLUB, StandardValue.ACE);
    group.add(card1);
    assertFalse(group.isEmpty());
  }
  @Test
  void testSort() {
    Card card1 = new StandardCard(StandardSuit.CLUB, StandardValue.ACE);
    Card card2 = new StandardCard(StandardSuit.CLUB, StandardValue.TWO);
    Card card3 = new StandardCard(StandardSuit.HEART, StandardValue.THREE);
    Card card4 = new StandardCard(StandardSuit.HEART, StandardValue.FOUR);
    group.add(card4);
    group.add(card3);
    group.add(card2);
    group.add(card1);
    group.sort();
    assertEquals(card1, group.cardAt(0));
    assertEquals(card2, group.cardAt(1));
    assertEquals(card3, group.cardAt(2));
    assertEquals(card4, group.cardAt(3));
  }
  @Test
  void testContains() {
    Card card1 = new StandardCard(StandardSuit.CLUB, StandardValue.ACE);
    Card card2 = new StandardCard(StandardSuit.CLUB, StandardValue.TWO);
    group.add(card1);
    assertTrue(group.contains(card1));
    assertFalse(group.contains(card2));
  }

  @Test
  void testIterator() {
    Card card1 = new StandardCard(StandardSuit.CLUB, StandardValue.ACE);
    Card card2 = new StandardCard(StandardSuit.CLUB, StandardValue.TWO);
    group.add(card1);
    group.add(card2);
    int i = 0;
    for (Card card : group) {
      if (i == 0) {
        assertEquals(card1, card);
      } else if (i == 1) {
        assertEquals(card2, card);
      }
      i++;
    }
  }

}
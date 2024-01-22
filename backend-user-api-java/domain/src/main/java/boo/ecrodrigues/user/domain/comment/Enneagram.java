package boo.ecrodrigues.user.domain.comment;

import java.util.Arrays;
import java.util.Optional;

public enum Enneagram {
  E_1W2("1w2"),
  E_2W3("2w3"),
  E_3W2("3w2"),
  E_3W4("3w4"),
  E_4W3("4w3"),
  E_4W5("4w5"),
  E_5W4("5w4"),
  E_5W6("5w6"),
  E_6W5("6w5"),
  E_6W7("6w7"),
  E_7W6("7w6"),
  E_7W8("7w8"),
  E_8W7("8w7"),
  E_8W9("8w9"),
  E_9W8("9w8"),
  E_9W1("9w1");

  private final String name;

  Enneagram(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static Optional<Enneagram> of(final String label) {
    return Arrays.stream(Enneagram.values())
        .filter(it -> it.name.equalsIgnoreCase(label))
        .findFirst();
  }
}

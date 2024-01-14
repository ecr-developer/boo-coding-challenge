package boo.ecrodrigues.user.application.account.create;

public record CreateAccountCommand(
    String name,
    boolean isActive
) {
  public static CreateAccountCommand with(
      final String aName,
      final boolean isActive
  ) {
    return new CreateAccountCommand(aName, isActive);
  }
}

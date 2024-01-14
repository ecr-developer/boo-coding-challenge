package boo.ecrodrigues.user.application.account.update;

public record UpdateAccountCommand(
    String id,
    String name,
    boolean isActive
) {
  public static UpdateAccountCommand with(
      final String anId,
      final String aName,
      final boolean isActive
  ) {
    return new UpdateAccountCommand(anId, aName, isActive);
  }
}

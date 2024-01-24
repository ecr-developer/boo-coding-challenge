package boo.ecrodrigues.user.application;

public abstract class MultiUseCase<IN, SIN, OUT> {

  public abstract OUT execute(IN anIn, SIN aSin);

}

package model;

public abstract class BinaryExpression implements Expression {
	protected Expression left;
	protected Expression right;
	@Override
	public abstract double calculate();

}

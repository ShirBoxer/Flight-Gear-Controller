package model;

public class Divide extends BinaryExpression {
	public Divide(Expression l , Expression r) {
		super.left = l;
		super.right = r;
	}
	@Override
	public double calculate() {
		double d = super.right.calculate();
		if(d == 0){
			System.out.println("Cannot divide by zero --ERROR--\n");
			return 0; //need try catch.
		}
		return super.left.calculate()/d;
	}

}

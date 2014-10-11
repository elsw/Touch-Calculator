package uk.co.withersnet.formulacalculator.operators;

import uk.co.withersnet.formulacalculator.util.Node;
import uk.co.withersnet.formulacalculator.util.Number;
import uk.co.withersnet.formulacalculator.util.Operator;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Multiply extends Operator {
	

	public Multiply(){
		super();
	}
	
	@Override
	public void draw(Canvas canvas,Paint paint){
		super.draw(canvas, paint);
		canvas.drawText("x", bounds.left, bounds.bottom, paint);
	}

	@Override
	public Number execute() throws Exception {
		if(number1 == null || number2 == null){
			throw new Exception("Operator has not enough values");
		}
		return new Number(number1 * number2);
	}
	
	@Override
	public String toString(){
		return "x";
	}
	@Override
	public Node getClone() {
		return new Multiply();
	}
}

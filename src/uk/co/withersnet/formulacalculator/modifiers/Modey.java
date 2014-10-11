package uk.co.withersnet.formulacalculator.modifiers;

import uk.co.withersnet.formulacalculator.util.GlobalConstants;
import uk.co.withersnet.formulacalculator.util.Modifier;
import uk.co.withersnet.formulacalculator.util.Node;
import uk.co.withersnet.formulacalculator.util.Number;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Modey extends Modifier {
	
	public Modey() {
		super();
		super.setFontSize(GlobalConstants.exponentFontSize);
	}
	public Modey(Modey m) {
		super(m);
		super.setFontSize(GlobalConstants.exponentFontSize);
	}
	
	@Override
	public void updateBounds(int x, int y, Paint paint) {
		Rect r = new Rect();
		paint.getTextBounds("e", 0, 1, r);
		super.updateBoundsFromBottom(x + r.width(), y - r.height(), paint);
		bounds.set(x, bounds.top, bounds.right, y);
	}
	@Override
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawText("e", bounds.left, bounds.bottom, paint);
		super.draw(canvas,paint);
	}
	@Override
	public Number execute() throws Exception {
		return new Number(Math.exp(super.execute().valueOf()));
	}
	
	@Override
	public String toString() {
		return "e^( " + super.toString() + " )";
	}
	
	@Override
	public Node getClone() {
		return new Modey(this);
	}
}
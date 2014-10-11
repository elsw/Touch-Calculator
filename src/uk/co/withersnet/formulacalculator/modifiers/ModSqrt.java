package uk.co.withersnet.formulacalculator.modifiers;

import uk.co.withersnet.formulacalculator.util.GlobalConstants;
import uk.co.withersnet.formulacalculator.util.Modifier;
import uk.co.withersnet.formulacalculator.util.Node;
import uk.co.withersnet.formulacalculator.util.Number;
import android.graphics.Canvas;
import android.graphics.Paint;

public class ModSqrt extends Modifier {
	
	public ModSqrt() {
		super();
	}
	public ModSqrt(ModSqrt m) {
		super(m);
	}
	
	@Override
	public void updateBounds(int x, int y, Paint paint) {
		super.updateBounds(x + (2 * GlobalConstants.spacing), y, paint);
		bounds.set(bounds.left - (2 * GlobalConstants.spacing), bounds.top, bounds.right + GlobalConstants.spacing, bounds.bottom);
	}
	@Override
	public void updateBoundsFromBottom(int x, int y, Paint paint) {
		super.updateBoundsFromBottom(x + (2 * GlobalConstants.spacing), y, paint);
		bounds.set(bounds.left - (2 * GlobalConstants.spacing), bounds.top - GlobalConstants.spacing, bounds.right + GlobalConstants.spacing, bounds.bottom);
	}
	@Override
	public void draw(Canvas canvas, Paint paint) {
		super.draw(canvas, paint);
		canvas.drawLine(bounds.left,bounds.bottom - 10,bounds.left + GlobalConstants.spacing,bounds.bottom,paint);
		canvas.drawLine(bounds.left + GlobalConstants.spacing,bounds.bottom,bounds.left + (2 * GlobalConstants.spacing),bounds.top,paint);
		canvas.drawLine(bounds.left + (2 * GlobalConstants.spacing),bounds.top,bounds.right,bounds.top,paint);
		canvas.drawLine(bounds.right,bounds.top,bounds.right,bounds.top + 10,paint);
	}
	@Override
	public Number execute() throws Exception {
		return new Number(Math.sqrt(super.execute().valueOf()));
	}
	
	@Override
	public String toString() {
		return "sqrt( " + super.toString() + " )";
	}
	
	@Override
	public Node getClone() {
		return new ModSqrt(this);
	}
}
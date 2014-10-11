package uk.co.withersnet.formulacalculator.modifiers;

import uk.co.withersnet.formulacalculator.util.GlobalConstants;
import uk.co.withersnet.formulacalculator.util.Modifier;
import uk.co.withersnet.formulacalculator.util.Node;
import uk.co.withersnet.formulacalculator.util.Number;
import android.graphics.Canvas;
import android.graphics.Paint;

public class ModAbs extends Modifier {
	
	public ModAbs() {
		super();
	}
	public ModAbs(ModAbs m) {
		super(m);
	}
	
	@Override
	public void updateBounds(int x, int y, Paint paint) {
		super.updateBounds(x + GlobalConstants.spacing, y, paint);
		bounds.set(bounds.left - GlobalConstants.spacing, bounds.top, bounds.right + GlobalConstants.spacing, bounds.bottom);
	}
	@Override
	public void updateBoundsFromBottom(int x, int y, Paint paint) {
		super.updateBoundsFromBottom(x + (2 * GlobalConstants.spacing), y, paint);
		bounds.set(bounds.left - GlobalConstants.spacing, bounds.top, bounds.right + GlobalConstants.spacing, bounds.bottom);
	}
	@Override
	public void draw(Canvas canvas, Paint paint) {
		super.draw(canvas, paint);
		canvas.drawLine(bounds.left,bounds.bottom,bounds.left,bounds.top,paint);
		canvas.drawLine(bounds.right,bounds.bottom,bounds.right,bounds.top,paint);
	}
	@Override
	public Number execute() throws Exception {
		return new Number(Math.abs(super.execute().valueOf()));
	}
	
	@Override
	public String toString() {
		return "Abs( " + super.toString() + " )";
	}
	
	@Override
	public Node getClone() {
		return new ModAbs(this);
	}
}

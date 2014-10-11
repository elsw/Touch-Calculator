package uk.co.withersnet.formulacalculator.modifiers;

import uk.co.withersnet.formulacalculator.util.DualModifier;
import uk.co.withersnet.formulacalculator.util.GlobalConstants;
import uk.co.withersnet.formulacalculator.util.Node;
import uk.co.withersnet.formulacalculator.util.Number;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Modx10y extends DualModifier {
	
	public Modx10y(){
		super();
		super.setFontSize(GlobalConstants.exponentFontSize);
	}
	public Modx10y(Modx10y mod){
		super(mod);
		super.setFontSize(GlobalConstants.exponentFontSize);
	}
	
	@Override
	public Number execute() throws Exception {
		return new Number(modifier.execute().valueOf() * (Math.pow(10, super.execute().valueOf())) );
	}
	@Override
	public void draw(Canvas canvas, Paint paint) {
		modifier.draw(canvas, paint);
		
		canvas.drawText("x10", modifier.getBounds().right + GlobalConstants.spacing, bounds.bottom, paint);
		super.draw(canvas, paint);
	}
	
	@Override
	public void updateBounds(int x, int y, Paint paint) {
		modifier.updateBounds(x, y, paint);
		paint.getTextBounds("x10", 0, 3, bounds);
		
		super.updateBoundsFromBottom(modifier.getBounds().right + bounds.width() + (2 * GlobalConstants.spacing), y - (int)(0.75f * bounds.height()), paint);//we need to know the size of the exponent
		this.bounds.set(x, super.getBounds().top,super.getBounds().right , y);
	}
	@Override
	public void updateBoundsFromBottom(int x, int y, Paint paint) {
		updateBounds(x,y,paint);
	}
	
	
	@Override
	public Node getClone() {
		return new Modx10y(this);
	}
	@Override
	public String toString() {
		return "mod(" + modifier.toString()+" x10 "+super.toString()+")";
	}
}

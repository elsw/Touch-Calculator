package uk.co.withersnet.formulacalculator.modifiers;

import uk.co.withersnet.formulacalculator.util.DualModifier;
import uk.co.withersnet.formulacalculator.util.GlobalConstants;
import uk.co.withersnet.formulacalculator.util.Node;
import uk.co.withersnet.formulacalculator.util.Number;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Modxy extends DualModifier {
	
	public Modxy(){
		super();
		super.setFontSize(GlobalConstants.exponentFontSize);
	}
	public Modxy(String exponent){
		super();
		super.setFontSize(GlobalConstants.exponentFontSize);
		modActive = false;
		super.addDigit(exponent);
		super.putCursorAtEnd();
	}
	public Modxy(Modxy mod){
		super(mod);
		super.setFontSize(GlobalConstants.exponentFontSize);
	}
	@Override
	public Number execute() throws Exception {
		return new Number(Math.pow(modifier.execute().valueOf(), super.execute().valueOf()));
	}
	@Override
	public void draw(Canvas canvas, Paint paint) {
		modifier.draw(canvas, paint);
		super.draw(canvas, paint);
	}
	
	@Override
	public void updateBounds(int x, int y, Paint paint) {
		modifier.updateBounds(x, y, paint);
		
		super.updateBoundsFromBottom(modifier.getBounds().right + GlobalConstants.spacing, y - (int)(0.75f * modifier.getBounds().height()), paint);//we need to know the size of the exponent
		this.bounds.set(x, super.getBounds().top,super.getBounds().right , y);
	}
	@Override
	public void updateBoundsFromBottom(int x, int y, Paint paint) {
		updateBounds(x,y,paint);
	}
	
	@Override
	public Node getClone() {
		return new Modxy(this);
	}
	@Override
	public String toString() {
		return "mod(" + modifier.toString()+" ^ "+super.toString()+")";
	}
}

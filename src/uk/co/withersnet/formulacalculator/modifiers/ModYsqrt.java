package uk.co.withersnet.formulacalculator.modifiers;

import uk.co.withersnet.formulacalculator.util.DualModifier;
import uk.co.withersnet.formulacalculator.util.GlobalConstants;
import uk.co.withersnet.formulacalculator.util.Node;
import uk.co.withersnet.formulacalculator.util.Number;
import android.graphics.Canvas;
import android.graphics.Paint;

public class ModYsqrt extends DualModifier {
	
	public ModYsqrt(){
		super();
		modifier.setFontSize(GlobalConstants.exponentFontSize);
	}
	public ModYsqrt(String exponent){
		super();
		moveActiveNumber = false;
		modifier.setFontSize(GlobalConstants.exponentFontSize);
		modActive = false;
		modifier.addDigit(exponent);
	}
	public ModYsqrt(ModYsqrt mod){
		super(mod);
		modifier.setFontSize(GlobalConstants.exponentFontSize);
	}
	@Override
	public Number execute() throws Exception {
		return new Number(Math.pow(super.execute().valueOf() , 1/(modifier.execute().valueOf()) ));
	}
	@Override
	public void draw(Canvas canvas, Paint paint) {
		modifier.draw(canvas, paint);
		super.draw(canvas, paint);
		canvas.drawLine(modifier.getBounds().right,bounds.bottom - 10,modifier.getBounds().right + GlobalConstants.spacing,bounds.bottom,paint);
		canvas.drawLine(modifier.getBounds().right + GlobalConstants.spacing,bounds.bottom,modifier.getBounds().right + (2 * GlobalConstants.spacing),bounds.top,paint);
		canvas.drawLine(modifier.getBounds().right + (2 * GlobalConstants.spacing),bounds.top,bounds.right,bounds.top,paint);
		canvas.drawLine(bounds.right,bounds.top,bounds.right,bounds.top + 10,paint);
	}
	
	@Override
	public void updateBounds(int x, int y, Paint paint) {
		paint.getTextBounds("t",0,1,bounds);
		modifier.updateBoundsFromBottom(x, y - bounds.height(), paint);
		
		super.updateBoundsFromBottom(modifier.getBounds().right + (2 * GlobalConstants.spacing), y, paint);
		if(modifier.getBounds().top > super.getBounds().top){
			this.bounds.set(x, super.getBounds().top - GlobalConstants.spacing,super.getBounds().right , y);
		}else{
			this.bounds.set(x, modifier.getBounds().top - GlobalConstants.spacing,super.getBounds().right , y);
		}
		
	}
	
	@Override
	public void updateBoundsFromBottom(int x, int y, Paint paint) {
		updateBounds(x,y,paint);
	}
	
	@Override
	public Node getClone() {
		return new ModYsqrt(this);
	}
	@Override
	public String toString() {
		return "mod( ^" + modifier.toString()+" sqrt( "+super.toString()+")";
	}
}

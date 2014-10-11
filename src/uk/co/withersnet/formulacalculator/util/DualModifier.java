package uk.co.withersnet.formulacalculator.util;

import android.graphics.Canvas;
import android.graphics.Paint;


//this modifier uses a modifier as 1 feild and the inherited node array as the other field
public abstract class DualModifier extends Modifier {
	
	//an extra field of nodes, if a number is currently active when a dual modifier is pressed, it deleted and passed to the constructor of this
	protected Modifier modifier;
	protected boolean modActive = true;
	protected boolean moveActiveNumber = true;//if true the active number will be moved to the modifier
	
	public DualModifier(){
		super();
		modifier = new Modifier();
		modifier.drawBoundsWhenEmpty(true);
		super.drawBoundsWhenEmpty(true);
	}
	public DualModifier(DualModifier mod){
		super(mod);
		this.modifier = (Modifier) ( mod.modifier.getClone());
	}
	public Modifier getModifier(){
		return modifier;
	}

	@Override
	public void addDigit(String number) {
		if(modActive){
			modifier.addDigit(number);
		}else{
			super.addDigit(number);
		}
	}
	@Override
	public void addModifier(Modifier m) {
		if(modActive){
			modifier.addModifier(m);
		}else{
			super.addModifier(m);
		}
	}
	@Override
	public void addOperator(String operator) {
		if(modActive){
			modifier.addOperator(operator);
		}else{
			super.addOperator(operator);
		}
	}
	@Override
	public void addUnselectable(Unselectable operator) {
		if(modActive){
			modifier.addUnselectable(operator);
		}else{
			super.addUnselectable(operator);
		}
	}
	@Override
	public void deleteDigit() {
		if(modActive){ 
			modifier.deleteDigit();
		}else{
			if(super.isEmpty()){
				modActive = true;
				modifier.putCursorAtEnd();
			}else{
				super.deleteDigit();
			}
		}
	}
	@Override
	public void drawCusor(Canvas canvas, Paint paint) {
		if(modActive){
			modifier.drawCusor(canvas, paint);
		}else{
			super.drawCusor(canvas, paint);
		}
	}
	@Override
	public boolean isEmpty() {
		return(super.isEmpty() && modifier.isEmpty());
	}
	public boolean isFull(){
		return((!super.isEmpty()) && (!modifier.isEmpty()));
	}
	
	@Override
	public void moveCursorLeft() {
		if(modActive){
			modifier.moveCursorLeft();
		}else{
			if(super.cursorAtStart()){
				modActive = true;
				modifier.putCursorAtEnd();
			}else{
				super.moveCursorLeft();
			}
		}
	}
	@Override
	public void moveCursorRight() {
		if(modActive){
			if(modifier.cursorAtEnd()){
				modActive = false;
				super.putCursorAtStart();
			}else{
				modifier.moveCursorRight();
			}
		}else{
			super.moveCursorRight();
		}
	}
	@Override
	public void setCursor(int x, int y) {
		double d = 1000000;
		int index = 0;
		for(int i = 0 ; i < nodes.size() ; i++){
			double tempD = nodes.get(i).getDistanceFrom(x, y);
			if(tempD < d){
				d = tempD;
				index = i;
			}
		}
		
		if(modifier.getDistanceFrom(x, y) < d){
			modifier.setCursor(x, y);
			modActive = true;
		}else{
			modActive = false;
			activeIndex = index;
			nodes.get(index).setCursor(x, y);
		}
	}
	@Override
	public boolean cursorAtEnd() {
		if(!modActive){
			return super.cursorAtEnd();
		}else{
			return false;
		}
	}
	@Override
	public boolean cursorAtStart() {
		if(modActive){
			return modifier.cursorAtStart();
		}else{
			return false;
		}
	}
	/* 
	 * functions that use the behaviour of the single node array of modifier
	 */
	protected boolean superCursorAtStart(){
		return super.cursorAtStart();
	}
	protected boolean superCursorAtEnd(){
		return super.cursorAtEnd();
	}
	protected void putSuperCursorAtStart(){
		super.putCursorAtStart();
	}
	protected void putSuperCursorAtEnd(){
		super.putCursorAtEnd();
	}
	protected void superSetCursor(int x,int y){
		super.setCursor(x, y);
	}
	
	@Override
	public void putCursorAtEnd() {
		modActive = false;
		modifier.putCursorAtEnd();
		super.putCursorAtEnd();
	}
	@Override
	public void putCursorAtStart() {
		modActive = true;
		modifier.putCursorAtStart();
		super.putCursorAtStart();
	}
	@Override
	public void invertNumber() {
		modifier.invertNumber();
	}
	public boolean moveActiveNumber(){
		return moveActiveNumber;
	}
}

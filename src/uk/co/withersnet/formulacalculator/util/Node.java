package uk.co.withersnet.formulacalculator.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

abstract public class Node {
	
	protected Rect bounds = new Rect();
	private boolean drawBounds = false;
	protected boolean drawBoundsWhenEmpty = false;
	
	/* 
	 * Draws onto canvas using current bounds
	 * call this after updating the bounds
	 * things like fitting the equation on screen and fractions need sizes before they can draw
	 */
	public void draw(Canvas canvas,Paint paint){
		if(drawBounds){
			canvas.drawRect(bounds.left,bounds.top,bounds.right,bounds.bottom, paint);
		}
	}
	public void drawBoundsWhenEmpty(boolean b){
		drawBoundsWhenEmpty = b;
	}
	/*
	 * x , y are the coordinates of the bottom left corner of the node
	 */
	public void updateBounds(int x, int y, Paint paint){
	}
	/* if a modifier draws below the given coordinates, 
	 * it must override this to give an option to draw the bounds above the coordinates
	 */
	public void updateBoundsFromBottom(int x, int y, Paint paint){
		updateBounds(x ,y ,paint);
	}
	
	/*
	 * Bounds are updated on draw and updateBounds()
	 */
	public final Rect getBounds(){
		return bounds;
	}
	
	public Number execute() throws Exception{
		return null;
	}
	//public void setDrawBorder(boolean b){
	//	drawBorder = b;
	//}
	
	//Call this after calling draw
	public void drawCusor(Canvas canvas,Paint paint){
	}
	public boolean cursorAtStart(){
		return false;
	}
	public boolean cursorAtEnd(){
		return false;
	}
	public boolean cursorAtTop(){
		return true;
	}
	public boolean cursorAtBottom(){
		return true;
	}
	public void putCursorAtEnd(){
	}
	public void putCursorAtStart(){
	}
	public void putCursorAtBottom(){
	}
	public void putCursorAtTop(){
	}
	public void invertNumber(){};
	
	public abstract Node getClone();
	
	public void addDigit(String number){
	}
	public void deleteDigit(){
	}
	public void moveCursorLeft(){
	}
	public void moveCursorRight(){
	}
	public void moveCursorUp(){
	}
	public void moveCursorDown(){
	}
	public boolean isEmpty(){
		return true;
	}
	public void setCursor(int x,int y){
	}
	/*
	 * @return the distance from x,y to the left of right of the node bounds
	 */
	public double getDistanceFrom(int x,int y){
		//find nearest corner
		int xC,yC;
		if(Math.abs(x - bounds.left) < Math.abs(x - bounds.right)){
			xC = bounds.left;
		}else{
			xC = bounds.right;
		}
		if(Math.abs(y - bounds.top) < Math.abs(y - bounds.bottom)){
			yC = bounds.top;
		}else{
			yC = bounds.bottom;
		}
		//return distance
		return Math.sqrt( Math.pow(xC - x,2) + Math.pow(yC - y,2) );

	}
	public boolean isDrawingBounds() {
		return drawBounds;
	}
	public void setDrawBounds(boolean drawBounds) {
		this.drawBounds = drawBounds;
	}
}

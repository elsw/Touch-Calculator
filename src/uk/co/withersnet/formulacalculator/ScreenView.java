package uk.co.withersnet.formulacalculator;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Hashtable;

import uk.co.withersnet.formulacalculator.util.GlobalConstants;
import uk.co.withersnet.formulacalculator.util.Modifier;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;



 public class ScreenView extends View {
    	
    	Modifier mainModifier;
    	Double answer;
    	double prevAnswer = 0;
    	String error = null;
    	Paint paint = new Paint();
    	boolean updateBounds = true;
    	boolean decimalAnswer = false;
    	
    	boolean engNotation = false;
    	int engOffset = 0;
    	
    	private int screenWidth;
    	private int screenHeight;
    	
    	private int prevDragX = 0;
    	private int prevDragY = 0;
    	private Integer currentDrawX = null;//dont know screen size here
    	private Integer currentDrawY = null;
    	
    	
    	//button modifiers
    	boolean shift = false;
    	boolean recall = false;
    	boolean store = false;
    	Hashtable<String,Double> stores = new Hashtable<String,Double>(10);
    	
    	public ScreenView(Context context) {
			super(context);	
			paint.setStyle(Paint.Style.STROKE);
		}
		public void setModifier(Modifier m){
			mainModifier = m;
		}
    	public void setAnswer(Double a){
    		answer = a;
    		prevAnswer = a;
    		engOffset = 0;
    		engNotation = false;
    	}
    	public double getPrevAnswer(){
    		return prevAnswer;
    	}
    	public void removeAnswer(){
    		answer = null;
    	}
    	public boolean answerDisplayed(){
    		return (answer != null);
    	}
		public void setError(String e){
			error = e;
		}
		public void removeError(){
			error = null;
		}
		//call this before invalidate
		public void resetDrawStart(){
			currentDrawX = (int)(GlobalConstants.drawStartX * screenWidth);
			currentDrawY = (int)(GlobalConstants.drawStartY * screenHeight);
			invalidate();
		}
		//exponent goes up, number goes down
		public void increaseEngOffset(){
			engNotation = true;
			engOffset++;
		}
		//exp goes down, number goes up
		public void decreaseEngOffset(){
			if(engNotation){
				engOffset--;
			}else{
				engNotation = true;
			}
		}
		public void resetEngOffset(){
			engNotation = false;
			engOffset = 0;
		}
		//force the answer to display in x10^ format
		public void forceEngNotation(){
			engNotation = true;
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			int action = event.getActionMasked();
			if(event.getPointerCount() == 2){
				if(action == MotionEvent.ACTION_MOVE){
					int changeX = (int)event.getX() - prevDragX;
					int changeY = (int)event.getY() - prevDragY;
					prevDragX = (int)event.getX();
					prevDragY = (int)event.getY();
					mainModifier.updateBounds(currentDrawX + changeX, currentDrawY + changeY, paint);
					
					if(mainModifier.getBounds().right > 100 && mainModifier.getBounds().left < screenWidth - 100){
						currentDrawX += changeX;
					}
					if(mainModifier.getBounds().bottom > 50 && mainModifier.getBounds().top < screenHeight - 50){
						currentDrawY += changeY;
					}				
					
					invalidate();
				}else if(action == MotionEvent.ACTION_POINTER_DOWN){
					prevDragX = (int)event.getX();
					prevDragY = (int)event.getY();
				}else if(action == MotionEvent.ACTION_POINTER_UP){
					
				}
				
				return true;
			}else{
				mainModifier.setCursor((int)event.getX(), (int)event.getY());
				this.invalidate();
				return true;
			}
		}
		
		public void invalidateWithoutBoundsUpdate(){
			updateBounds = false;
			invalidate();
		}
		
    	@Override
    	protected void onDraw(Canvas canvas) {
    		if(currentDrawX == null){
    			screenWidth = canvas.getWidth();
    			currentDrawX = (int)(GlobalConstants.drawStartX * canvas.getWidth());
    		}
    		if(currentDrawY == null){
    			screenHeight = canvas.getHeight();
    			currentDrawY = (int)(GlobalConstants.drawStartY * canvas.getHeight());
    		}
    		
    		if(mainModifier != null){
    			canvas.drawColor(Color.WHITE);
    			if(updateBounds){
    				mainModifier.updateBounds(currentDrawX,currentDrawY , paint);
    			}else{
    				updateBounds = true;
    			}
    			
    			mainModifier.setFontSize(GlobalConstants.fontSize);
    			
    			mainModifier.draw(canvas,paint);
    			mainModifier.drawCusor(canvas, paint);
    			
    			
    			if(answer != null){
    				if(String.valueOf(answer).contains("Infinity")){
    					error = "Number too Big";
    				}else{
	    				Rect bounds = new Rect();
	    				NumberFormat formatter;
	    				int expDrawStartX = (int)(GlobalConstants.drawAnsX * canvas.getWidth());
	    				
	    				if(Math.abs(answer) > GlobalConstants.minBeforeExp || 
	    				   (Math.abs(answer) < 1/GlobalConstants.minBeforeExp && answer != 0) || engNotation){
    						formatter = new DecimalFormat("0." + GlobalConstants.maxPrecision + "E0");
    						String ansStr = formatter.format(answer);
    						String[] ans = ansStr.split("E");
    						
    						if(engNotation){
    							int exp = Integer.valueOf(ans[1]);
    							double number = Double.valueOf(ans[0]);
    							int offset = -(exp % 3) + (engOffset * 3);
    							
    							formatter = new DecimalFormat("0." + GlobalConstants.maxPrecision);
    							
    							ans[0] = formatter.format(number / Math.pow(10, offset));
    							ans[1] = String.valueOf(exp + offset);
    						}
    						
    						ans[0] += "  x10";
    						paint.getTextBounds(ans[0], 0, ans[0].length(), bounds);
        				
    						paint.setTextSize(GlobalConstants.exponentFontSize);
    						canvas.drawText(ans[1], expDrawStartX,(
    								int)(GlobalConstants.drawAnsY * canvas.getHeight() - (GlobalConstants.fontSize * 0.3f)), paint);
        				
    						paint.setTextSize(GlobalConstants.fontSize);
    						canvas.drawText(ans[0], expDrawStartX - GlobalConstants.spacing - bounds.width() ,
    								              (int)(GlobalConstants.drawAnsY * canvas.getHeight()),paint);
    						
	    				}else{
	    					formatter = new DecimalFormat("0." + GlobalConstants.maxPrecision);
	    					String ansStr;
	    					if((answer/Math.PI) == Math.floor(answer/Math.PI) && !decimalAnswer && answer != 0){
    							ansStr = formatter.format(answer/Math.PI);
    							ansStr += "\u03C0";
    						}else if( (1/(answer/Math.PI)) == Math.floor(1/(answer/Math.PI)) && !decimalAnswer && answer != 0){
    							ansStr = formatter.format(answer/Math.PI);
    							ansStr += "\u03C0";
    							//TODO format answer in fraction format
    						}else{
    							ansStr = formatter.format(answer);
    						}
    					paint.getTextBounds(ansStr, 0, ansStr.length(), bounds);
        				canvas.drawText(ansStr, expDrawStartX - GlobalConstants.spacing - bounds.width() ,
        									(int)(GlobalConstants.drawAnsY * canvas.getHeight()),paint);
    				}
    			}
    			}
    			if(error != null){
    				paint.setTextSize(GlobalConstants.errorFontSize);
    				canvas.drawText(error,(int)(GlobalConstants.drawErrorX * canvas.getWidth()),(int)(GlobalConstants.drawErrorY * canvas.getHeight()), paint);
    				paint.setTextSize(GlobalConstants.fontSize);
    			}
    			if(shift){
    				paint.setTextSize(GlobalConstants.errorFontSize);
    				canvas.drawText("shift", 0.05f * canvas.getWidth() , 0.05f * canvas.getHeight(), paint);
    				paint.setTextSize(GlobalConstants.fontSize);
    			}else if(store){
    				paint.setTextSize(GlobalConstants.errorFontSize);
    				canvas.drawText("Store 0 to 9?", 0.05f * canvas.getWidth() , 0.8f * canvas.getHeight(), paint);
    				paint.setTextSize(GlobalConstants.fontSize);
    			}else if(recall){
    				paint.setTextSize(GlobalConstants.errorFontSize);
    				canvas.drawText("Recall 0 to 9?", 0.05f * canvas.getWidth() , 0.8f * canvas.getHeight(), paint);
    				paint.setTextSize(GlobalConstants.fontSize);
    			}
    			if(GlobalConstants.deg){
    				paint.setTextSize(GlobalConstants.errorFontSize);
    				canvas.drawText("deg", 0.15f * canvas.getWidth() , 0.05f * canvas.getHeight(), paint);
    				paint.setTextSize(GlobalConstants.fontSize);
    			}
    			if(GlobalConstants.rad){
    				paint.setTextSize(GlobalConstants.errorFontSize);
    				canvas.drawText("rad", 0.15f * canvas.getWidth() , 0.05f * canvas.getHeight(), paint);
    				paint.setTextSize(GlobalConstants.fontSize);
    			}
    			if(decimalAnswer){
    				paint.setTextSize(GlobalConstants.errorFontSize);
    				canvas.drawText("  -D", 0.25f * canvas.getWidth() , 0.05f * canvas.getHeight(), paint);
    				paint.setTextSize(GlobalConstants.fontSize);
    			}else{
    				paint.setTextSize(GlobalConstants.errorFontSize);
    				canvas.drawText("S-", 0.25f * canvas.getWidth() , 0.05f * canvas.getHeight(), paint);
    				paint.setTextSize(GlobalConstants.fontSize);
    			}
    			String s = "STO: ";
    			String tmp;
    			for(int i = 0 ; i < 10 ; i++){
    				tmp = String.valueOf(i);
    				if(stores.containsKey(tmp)){
    					s += tmp;
    				}else{
    					s += "  ";
    				}
    			}
    			paint.setTextSize(GlobalConstants.errorFontSize);
				canvas.drawText(s, 0.45f * canvas.getWidth() , 0.05f * canvas.getHeight(), paint);
				paint.setTextSize(GlobalConstants.fontSize);
    		}
    	}
    	
    	public void clearButtonMods(){
    		shift = false;
    		store = false;
    		recall = false;
    	}
    	public void store(String key,Double n){
    		stores.put(key, n);
    	}
    	public void clearStores(){
    		stores.clear();
    	}
    	public boolean hasStore(String key){
    		return stores.containsKey(key);
    	}
    	public Double getStore(String key){
    		return stores.get(key);
    	}
    }
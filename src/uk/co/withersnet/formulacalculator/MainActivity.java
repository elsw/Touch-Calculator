package uk.co.withersnet.formulacalculator;

import java.text.DecimalFormat;

import uk.co.withersnet.formulacalculator.brackets.CloseBracket;
import uk.co.withersnet.formulacalculator.brackets.Cosine;
import uk.co.withersnet.formulacalculator.brackets.Cosine1;
import uk.co.withersnet.formulacalculator.brackets.Ln;
import uk.co.withersnet.formulacalculator.brackets.Log10;
import uk.co.withersnet.formulacalculator.brackets.OpenBracket;
import uk.co.withersnet.formulacalculator.brackets.Sine;
import uk.co.withersnet.formulacalculator.brackets.Sine1;
import uk.co.withersnet.formulacalculator.brackets.Tan;
import uk.co.withersnet.formulacalculator.brackets.Tan1;
import uk.co.withersnet.formulacalculator.modifiers.Fraction;
import uk.co.withersnet.formulacalculator.modifiers.ModAbs;
import uk.co.withersnet.formulacalculator.modifiers.ModLogab;
import uk.co.withersnet.formulacalculator.modifiers.ModSqrt;
import uk.co.withersnet.formulacalculator.modifiers.ModYsqrt;
import uk.co.withersnet.formulacalculator.modifiers.Modey;
import uk.co.withersnet.formulacalculator.modifiers.Modx10y;
import uk.co.withersnet.formulacalculator.modifiers.Modxy;
import uk.co.withersnet.formulacalculator.util.GlobalConstants;
import uk.co.withersnet.formulacalculator.util.Modifier;
import uk.co.withersnet.formulacalculator.util.Number;
import uk.co.withersnet.formulacalculator.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	Modifier mainModifier = new Modifier(); //Main blank modifier
	ScreenView screen;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainModifier.setFontSize(GlobalConstants.fontSize);
        
        setContentView(R.layout.activity_main);

        LinearLayout myLayout = (LinearLayout) findViewById(R.id.screen);

        screen = new ScreenView(this);
        screen.setModifier(mainModifier);
        myLayout.addView(screen);
        
        GlobalConstants.fontSize = getResources().getInteger(R.integer.font_size);
        GlobalConstants.exponentFontSize = getResources().getInteger(R.integer.exponent_font_size);
        GlobalConstants.errorFontSize = getResources().getInteger(R.integer.error_font_size);
    }


    public void numberPressed(View view){
    	screen.removeError();
    	
    	String number = view.getTag().toString(); 
    	
    	if(screen.shift){
			if(number.equals("1")){
				//rad
				GlobalConstants.rad = true;
				GlobalConstants.deg = false; 
			}else if(number.equals("2")){
				//deg
				GlobalConstants.rad = false;
				GlobalConstants.deg = true;
			}
			
			screen.shift = false;
    	}else if(screen.store){
    		if(screen.answer != null){
        		screen.store(number, screen.answer);
    		}
    		screen.store = false;
    	}else if(screen.recall){
    		if(screen.hasStore(number)){
    			DecimalFormat formatter = new DecimalFormat("0." + GlobalConstants.maxPrecision);
    			mainModifier.addNumber(new Number(formatter.format(screen.getStore(number))));
    		}
    		screen.recall = false;
    	}else{
    		//if(screen.answerDisplayed()){
    		//	mainModifier.addBracketsAtEnds();
    		//}
	        mainModifier.addDigit(number);
	        screen.removeAnswer();
    	}
        screen.invalidate();
    }
    public void commandPressed(View view) {
    	String command = view.getTag().toString();
    	
    	if(screen.shift){
    		if(command.equals("RCL")){
    			//store
	    		screen.clearButtonMods();
	    		screen.store = true;
	    	}else if(command.equals("ENG")){
	    		//ENG^-1
	    		screen.increaseEngOffset();
	    	}
    		screen.shift = false;
    	}else{
    		if(command.equals("sd")){
    			screen.decimalAnswer = !screen.decimalAnswer;
	    	}else{
		    	screen.removeError();
		        if(command.equals("=")){
		        	try{
		        		screen.setAnswer(mainModifier.executeTopLevel().valueOf());
		        	}catch(NumberFormatException e){
		        		screen.setError(e.getMessage().replace("double", "number"));
		        		e.printStackTrace();
		    			//throw new NumberFormatException("invalid number");
		    		}catch(Exception e){
		        		screen.setError(e.getLocalizedMessage());
		        		e.printStackTrace();
		        	}
		        }else if(command.equals("AC")){
		        	screen.resetDrawStart();
		        	mainModifier.clear();
		        	screen.removeAnswer();
		        }else if (command.equals("DEL")){
		        	mainModifier.deleteDigit();
		        	screen.removeAnswer();
		        }else if(command.equals("shift")){
		        	screen.shift = true;
		    	}else if(command.equals("reset")){
		    		screen.resetDrawStart();
		    		screen.removeAnswer();
		    	}else if(command.equals("RCL")){
		    		screen.clearButtonMods();
		    		screen.recall = true;
		    	}else if(command.equals("erase")){
		    		screen.clearButtonMods();
		    		screen.removeAnswer();
		    		screen.clearStores();
		    	}else if(command.equals("ENG")){
		    		screen.decreaseEngOffset();
		    	}else if(command.equals("Ans")){
		    		DecimalFormat formatter = new DecimalFormat("0.################");
		    		mainModifier.addNumber(new Number(formatter.format(screen.getPrevAnswer())));
		    		screen.removeAnswer();
		    	}else if(command.equals("help")){
		    		Intent helpScreen = new Intent(getApplicationContext(), HelpActivity.class);
		            startActivity(helpScreen);
		    	}
	    	}
    	}
    	
    	screen.invalidate();
    }
    
    public void modifierPressed(View view){
    	screen.removeAnswer();
    	screen.removeError();

        String modifier = view.getTag().toString();
        if(screen.shift){
        	if(modifier.equals("x10^")){
    			//pi
    			String str = "\u03C0";
    			mainModifier.addDigit(str);
    		}else if(modifier.equals("sin")){
    			mainModifier.addUnselectable(new Sine1());
	        }else if(modifier.equals("cos")){
	        	mainModifier.addUnselectable(new Cosine1());
	        }else if(modifier.equals("tan")){
	        	mainModifier.addUnselectable(new Tan1());
	        }else if(modifier.equals("ln")){
	        	mainModifier.addModifier(new Modey());
	        }else if(modifier.equals("sqrt")){
	        	mainModifier.addModifier(new ModYsqrt("3"));
	        }
        	screen.clearButtonMods();
    	}else{
	        if(modifier.equals("(")){
	        	mainModifier.addUnselectable(new OpenBracket());
	        }else if(modifier.equals(")")){
	        	mainModifier.addUnselectable(new CloseBracket());
	        }else if(modifier.equals("frac")){
	        	mainModifier.addModifier(new Fraction());
	        }else if(modifier.equals("x10^")){
	        	mainModifier.addModifier(new Modx10y());
	        }else if(modifier.equals("xy")){
	        	mainModifier.addModifier(new Modxy());
	        }else if(modifier.equals("x2")){
	        	mainModifier.addModifier(new Modxy("2"));
	        }else if(modifier.equals("abs")){
	        	mainModifier.addModifier(new ModAbs());
	        }else if(modifier.equals("sqrt")){
	        	mainModifier.addModifier(new ModSqrt());
	        }else if(modifier.equals("sin")){
	        	mainModifier.addUnselectable(new Sine());
	        }else if(modifier.equals("cos")){
	        	mainModifier.addUnselectable(new Cosine());
	        }else if(modifier.equals("tan")){
	        	mainModifier.addUnselectable(new Tan());
	        }else if(modifier.equals("log")){
	        	mainModifier.addUnselectable(new Log10());
	        }else if(modifier.equals("ln")){
	        	mainModifier.addUnselectable(new Ln());
	        }else if(modifier.equals("logab")){
	        	mainModifier.addModifier(new ModLogab());
	        }else if(modifier.equals("ysqrt")){
	        	mainModifier.addModifier(new ModYsqrt());
	        }
    	}
    	screen.invalidate();
    }
    
    public void operatorPressed(View view){
	    	String command = view.getTag().toString();
	    	screen.clearButtonMods();
	    	if(screen.answerDisplayed() && mainModifier.cursorAtEnd()){
				mainModifier.addBracketsAtEnds();
			}
	    	
	    	mainModifier.addOperator(command);
	    	
    	screen.invalidate();
    }
       
    public void directionPressed(View view){
    	String command = view.getTag().toString();
    	screen.clearButtonMods();
    	
    	if(command.equals("left")){
    		mainModifier.moveCursorLeft();
    	}else if(command.equals("right")){
    		mainModifier.moveCursorRight();
    	}else if(command.equals("up")){
    		mainModifier.moveCursorUp();
    	}else if(command.equals("down")){
    		mainModifier.moveCursorDown();
    	}
    	screen.invalidate();
    }
    
    public void printMainMod(View view){
    	Log.d("Main Mod : ", mainModifier.toString());
    }
}

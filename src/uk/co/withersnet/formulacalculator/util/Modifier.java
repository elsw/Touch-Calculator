package uk.co.withersnet.formulacalculator.util;

import java.util.ArrayList;

import uk.co.withersnet.formulacalculator.brackets.CloseBracket;
import uk.co.withersnet.formulacalculator.brackets.OpenBracket;
import uk.co.withersnet.formulacalculator.operators.Add;
import uk.co.withersnet.formulacalculator.operators.Divide;
import uk.co.withersnet.formulacalculator.operators.Multiply;
import uk.co.withersnet.formulacalculator.operators.Subtract;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Modifier extends Node{
	
	//the current index , 0 = first node, operators are not selectable,
	protected int activeIndex = 0;
	public ArrayList<Node> nodes;
	//if font size not set, use fontSize from level above this
	protected Integer fontSize = null;
	
	public Modifier() {
		nodes = new ArrayList<Node>();
		nodes.add(new Empty());
	}
	public Modifier(Modifier m){
		nodes = new ArrayList<Node>();
		this.activeIndex = m.activeIndex;
		for(Node n:m.nodes){
			nodes.add(n.getClone());
		}
	}
	public Modifier(Number n){
		nodes = new ArrayList<Node>();
		nodes.add(n);
	}
	public Modifier(ArrayList<Node> nodes){
		this.nodes = nodes;
	}
	
	public void addNumber(Number n){
		Node activeNode = nodes.get(activeIndex);
		if(activeNode instanceof Modifier){
			((Modifier) activeNode).addNumber(n);
		}else if(activeNode instanceof Empty){
			nodes.remove(activeIndex);
			nodes.add(activeIndex, n);
		}else if(activeNode instanceof Number){
			((Number)activeNode).insertDigits(n.toString());
		}else{
			activeIndex++;
			nodes.add(activeIndex, n);
		}
	}
	
	@Override		
	public void addDigit(String number){
		Node activeNode = nodes.get(activeIndex);
		if(activeNode instanceof Number || activeNode instanceof Modifier){
			activeNode.addDigit(number);
		}else if(activeNode instanceof Empty){
			nodes.remove(activeIndex);
			nodes.add(activeIndex, new Number(number));
		}
	}
	
	
	public void addUnselectable(Unselectable operator){
		Node activeNode = nodes.get(activeIndex);
		if(activeNode instanceof Empty){
			activeIndex++;
			nodes.add(activeIndex,operator);
			activeIndex++;
			nodes.add(activeIndex, new Empty());
		}else if(activeNode instanceof Number){
			if(activeNode.cursorAtEnd()){
				activeIndex++;
				nodes.add(activeIndex,operator);
				activeIndex++;
				if(activeIndex == nodes.size()){
					nodes.add(activeIndex, new Empty());
				}else{
					activeNode = nodes.get(activeIndex);
					if(activeNode instanceof Unselectable || activeNode instanceof Modifier){
						nodes.add(activeIndex, new Empty());
					}
				}
			}else if(activeNode.cursorAtStart()){
				nodes.add(activeIndex,operator);
				if(activeIndex > 0 && (nodes.get(activeIndex - 1) instanceof Unselectable || nodes.get(activeIndex - 1) instanceof Modifier) ){
					nodes.add(activeIndex,new Empty());
					activeIndex++;
				}else if(activeIndex == 0){
					nodes.add(0,new Empty());
					activeIndex++;
				}
				activeIndex++;
			}else{
				  Number n = ((Number) activeNode).splitNumber();
				  activeIndex++;
				  nodes.add(activeIndex, operator);
				  activeIndex++;
				  nodes.add(activeIndex,n);
				  n.putCursorAtStart();
			}
		}else if(activeNode instanceof Modifier){
			((Modifier) activeNode).addUnselectable(operator);
		}
	}
	
	public void addModifier(Modifier m){
		Node activeNode = nodes.get(activeIndex);
		if(activeNode instanceof Empty){
			if( (activeIndex > 0) && nodes.get(activeIndex - 1) instanceof Modifier && m instanceof DualModifier){
				if(((DualModifier)m).moveActiveNumber){
					//the modifier before the empty wants to be put into the dualmodifier
					((DualModifier)m).getModifier().addNode(nodes.get(activeIndex - 1));
					nodes.remove(activeIndex - 1);
					nodes.add(activeIndex - 1,m);//add modifier in place
					//move back to modifier (currently at the empty)
					activeIndex--;
					//m.moveCursorDown();
					((DualModifier)m).modActive = false;
					
					if(((DualModifier)m).isFull()){
						activeIndex++;
					}
				}else{
					nodes.add(activeIndex + 1,m);
					activeIndex++;
				}

			}else if( (activeIndex > 0) && nodes.get(activeIndex - 1) instanceof CloseBracket && m instanceof DualModifier){
				
				if(((DualModifier)m).moveActiveNumber){
					
					//everything in the brackets wants to be put in the dual modifier
					int start = activeIndex - 1;//start = CloseBraket location
					int i = start;
					
					while(i > -1 && !(nodes.get(i) instanceof OpenBracket)){
						((DualModifier)m).getModifier().getNodeArray().add(0, nodes.get(i));
						nodes.remove(i);
						i--;
					}
					//open bracket reached or start or array reached
					if(i == -1){
						//no open bracket found
						nodes.add(0,m);
						nodes.add(0,new Empty());
						activeIndex = 0;
					}else{
						((DualModifier)m).getModifier().getNodeArray().add(0, nodes.get(i));
						((DualModifier)m).getModifier().getNodeArray().add(0, new Empty());//need a Empty next to bracket
						nodes.remove(i);
						//open bracket found at i
						nodes.add(i,m);
						//move back to modifier (currently at the empty)
						activeIndex = i;
					}

					//m.moveCursorDown();
					((DualModifier)m).modActive = false;
					
					if(((DualModifier)m).isFull()){
						activeIndex++;
					}
					
				}else{
					nodes.add(activeIndex + 1,m);
					activeIndex++;
				}
			}else{
				//add modifier after empty
				activeIndex++;
				nodes.add(activeIndex, m);
				if(activeIndex + 1 < nodes.size()){
					if(!(nodes.get(activeIndex + 1) instanceof Number)){
						//operator or modifier is next, add empty
						nodes.add(activeIndex + 1,new Empty());
					}
				}else{
					nodes.add(activeIndex + 1,new Empty());
				}
			}
		}else if(activeNode instanceof Modifier){
			((Modifier) activeNode).addModifier(m);
		}else{
			//is number
			if(activeNode.cursorAtStart()){
				nodes.add(activeIndex,m);
				nodes.add(activeIndex,new Empty());
				activeIndex++;
			}else if(activeNode.cursorAtEnd()){
				if(m instanceof DualModifier){
					if(((DualModifier)m).moveActiveNumber){
						((DualModifier)m).getModifier().addNode(activeNode);
						nodes.remove(activeIndex);
						nodes.add(activeIndex,m);
						//m.moveCursorDown();
						((DualModifier)m).modActive = false;
					}else{
						nodes.add(activeIndex + 1,m);
						activeIndex++;
					}
						
					if(activeIndex + 1 < nodes.size()){
						if(!(nodes.get(activeIndex + 1) instanceof Number)){
							//operator or modifier is next, add empty
							nodes.add(activeIndex + 1,new Empty());
						}
					}else{
						nodes.add(activeIndex + 1,new Empty());
					}
					//add empty left of modifier, since there shouldnt be a empty to the left of the number at the start
					nodes.add(activeIndex,new Empty());
					activeIndex++;
					if(((DualModifier)m).isFull()){
						activeIndex++;
					}
				}else{
					activeIndex ++;
					nodes.add(activeIndex,m);
					nodes.add(activeIndex + 1,new Empty());
					
				}
			}else{
				if(activeNode instanceof Number){
					if(m instanceof DualModifier){
						Number n = ((Number)activeNode).splitNumber();
						((DualModifier)m).getModifier().addNode(activeNode);
						nodes.remove(activeIndex);
						nodes.add(activeIndex,m);
						((DualModifier)m).modActive = false;
						nodes.add(activeIndex + 1,n);
					}else{
						Number n = ((Number)activeNode).splitNumber();
						activeIndex++;
						nodes.add(activeIndex, n);
						nodes.add(activeIndex,m);
					}
				}else if(activeNode instanceof Modifier){
					((Modifier) activeNode).addModifier(m);
				}
			}
		}
	}
	
	public void addOperator(String operator){
		if(operator.equals("x")){
			addUnselectable(new Multiply());
		}else if(operator.equals("+")){
			addUnselectable(new Add());
		}else if(operator.equals("/")){
			addUnselectable(new Divide());
		}else if(operator.equals("-")){
			addUnselectable(new Subtract());
		}
	}
	
	public void deleteDigit(){
		Node activeNode = nodes.get(activeIndex);
		if(activeNode instanceof Empty){
			if(activeIndex > 0){
				if(nodes.get(activeIndex - 1) instanceof Modifier){
					activeIndex--;
					nodes.get(activeIndex).putCursorAtEnd();
				}else{
					//assume unselectable
					nodes.remove(activeIndex);
					activeIndex--;
					nodes.remove(activeIndex);
					activeIndex--;
					nodes.get(activeIndex).putCursorAtEnd();
				}
			}
		}else if(activeNode instanceof Number || activeNode instanceof Modifier){
			if(activeNode instanceof Modifier && activeNode.isEmpty()){
				nodes.remove(activeIndex);
				nodes.remove(activeIndex);
				activeIndex--;
			}else if(activeNode.cursorAtStart()){
				if(activeIndex > 0){
					activeIndex--;
					activeNode = nodes.get(activeIndex);
					if(activeNode instanceof Number || activeNode instanceof Modifier){
						activeNode.putCursorAtEnd();
						activeNode.deleteDigit();
					}else if(activeNode instanceof Unselectable){
						nodes.remove(activeIndex);
						
							if(nodes.get(activeIndex - 1) instanceof Empty){
								if(activeIndex == 1){
									nodes.remove(activeIndex - 1);
									activeIndex--;
								}
							}else if(nodes.get(activeIndex - 1) instanceof Number){
								//merge numbers
								nodes.get(activeIndex - 1).putCursorAtEnd();
								((Number) nodes.get(activeIndex - 1)).appendDigits(nodes.get(activeIndex).toString());
								nodes.remove(activeIndex);
								activeIndex--;
							}
						
					}
				}
			}else{
				activeNode.deleteDigit();
				if(activeNode.isEmpty() && !(activeNode instanceof Modifier)){ 
					nodes.remove(activeIndex);
					if(activeIndex == 0 || nodes.get(activeIndex - 1) instanceof Unselectable || nodes.get(activeIndex - 1) instanceof Modifier){
						nodes.add(activeIndex, new Empty());
					}
				}
			}
		} 
	}
	
	public void clear(){
		nodes.clear();
		activeIndex = 0;
		nodes.add(new Empty());
	}
	
	
	@Override
	public void draw(Canvas canvas, Paint paint) {
		super.draw(canvas, paint);
		float prevFontSize = paint.getTextSize();
		if(fontSize != null){
			paint.setTextSize(fontSize);
		}
		
		for(Node n:nodes){
			n.draw(canvas, paint);
		}
		if(drawBoundsWhenEmpty && nodes.size() == 1 && nodes.get(0) instanceof Empty){
			Rect r = nodes.get(0).bounds;
			canvas.drawRect(r.left - 3, r.top, r.right + 3, r.bottom, paint);
		}
		paint.setTextSize(prevFontSize);
	}
	
	private void updateBracketsBounds(Paint paint){
		int bracketCount = 0;
		
		//set open bracket height
		for(int i = 0 ; i < nodes.size() ; i++){
			Node currentNode = nodes.get(i);
			if(currentNode instanceof OpenBracket){
				int n = i + 1;
				bracketCount = 1;
				while(bracketCount != 0){
					if(n < nodes.size()){
						Node node = nodes.get(n);
						if(node instanceof OpenBracket){
							bracketCount++;
						}else if(node instanceof CloseBracket){
							bracketCount--;
						}else{
							if(node.getBounds().top < currentNode.getBounds().top){
								currentNode.getBounds().top = node.getBounds().top;
							}
							if(node.getBounds().bottom > currentNode.getBounds().bottom){
								currentNode.getBounds().bottom = node.getBounds().bottom;
							}
						}
					}else{
						//reached end of node array, exit loop
						bracketCount = 0;
					}
					n++;
				}
			}
		}
		
		//set close bracket height
		for(int i = 0 ; i < nodes.size() ; i++){
			Node currentNode = nodes.get(i);
			if(currentNode instanceof CloseBracket){
				int n = i - 1;
				bracketCount = 1;
				while(bracketCount != 0){
					if(n >= 0){
						Node node = nodes.get(n);
						if(node instanceof OpenBracket){
							bracketCount--;
						}else if(node instanceof CloseBracket){
							bracketCount++;
						}else{
							if(node.getBounds().top < currentNode.getBounds().top){
								currentNode.getBounds().top = node.getBounds().top;
							}
							if(node.getBounds().bottom > currentNode.getBounds().bottom){
								currentNode.getBounds().bottom = node.getBounds().bottom;
							}
						}
					}else{
						//reached end of node array, exit loop
						bracketCount = 0;
					}
					n--;
				}
			}
		}
	}
	
	
	@Override
	public void updateBounds(int x, int y, Paint paint) {
		float prevFontSize = paint.getTextSize();
		if(fontSize != null){
			paint.setTextSize(fontSize);
		}
				
		int left = x;
		int right = x;
		int up = y;
		int down = y + 2;
		
		for(Node node:nodes){
			node.updateBounds(right, y, paint);
			right += node.getBounds().width();
			right+= GlobalConstants.spacing;
			// 0,0 is at top left of screen
			if(node.getBounds().top < up){
				up = node.getBounds().top - 2;
			}
			if(node.getBounds().bottom > down){
				down = node.getBounds().bottom + 2;
			}
		}
		bounds.set(left,up,right,down);
		updateBracketsBounds(paint);
		
		paint.setTextSize(prevFontSize);
	}
	@Override
	public void updateBoundsFromBottom(int x, int y, Paint paint) {
		float prevFontSize = paint.getTextSize();
		if(fontSize != null){
			paint.setTextSize(fontSize);
		}
		
		int left = x;
		int right = x;
		int up = y;
		int down = y;
		
		for(Node node:nodes){
			node.updateBoundsFromBottom(right, y, paint);
			right += node.getBounds().width();
			right+= GlobalConstants.spacing;
			// 0,0 is at top left of screen
			if(node.getBounds().top < up){
				up = node.getBounds().top;
			}
			if(node.getBounds().bottom > down){
				up = node.getBounds().top;
			}
		}
		bounds.set(left,up,right,down);
		updateBracketsBounds(paint);
		
		paint.setTextSize(prevFontSize);
	}
	@Override
	public void drawCusor(Canvas canvas,Paint paint){
		float prevFontSize = paint.getTextSize();
		if(fontSize != null){
			paint.setTextSize(fontSize);
		}
		nodes.get(activeIndex).drawCusor(canvas,paint);
		paint.setTextSize(prevFontSize);
	}
	
	@Override
	public void putCursorAtEnd() {
			activeIndex = nodes.size() - 1;
			nodes.get(activeIndex).putCursorAtEnd();
	}
	@Override
	public void putCursorAtStart() {
			activeIndex = 0;
			nodes.get(0).putCursorAtStart();
	}
	@Override
	public void putCursorAtTop() {
		nodes.get(activeIndex).putCursorAtTop();
	}
	@Override
	public void putCursorAtBottom() {
		nodes.get(activeIndex).putCursorAtBottom();
	}
	public Number execute() throws Exception{
		return execute(false);//dont copy array
	}
	
	public Number executeTopLevel() throws Exception{
		return execute(true);//the top level modifier will make a deep copy of the nodes array
	}
	private Number execute(boolean copyArray) throws Exception{
		
		try{
			//make a copy of nodes
			ArrayList<Node> nodesCopy;
			if(copyArray){
				//create deep copy of array, should only be called on the top level modifier
				nodesCopy = ((Modifier)getClone()).nodes;
			}else{
				nodesCopy = this.nodes;
			}
			
			//remove Emptys
			for(int i = 0 ; i < nodesCopy.size() ; i++){
				if(nodesCopy.get(i) instanceof Empty){
					nodesCopy.remove(i);
				}
			}
			//check if empty
			if(nodesCopy.size() == 0){
				throw new Exception("Error : Empty equation");
			}
			
			boolean brackets = true;
			while(brackets){
				brackets = false;
				//combine things in brackets into a single blank modifier so they are executed together
				//look for CloseBrakets, then find the OpenBracket before it, then combine everything between them
				for(int i = 0 ; i < nodesCopy.size() ; i++){
					Node currentNode = nodesCopy.get(i);
					if(currentNode instanceof CloseBracket){
						
						int n = i;
						boolean loop = true;
						while(loop == true){
							n--;
							if(n >= 0){
								Node testNode = nodesCopy.get(n);
								if(testNode instanceof OpenBracket){
									//n = open index, i = close index
									Modifier combinedModifier = new Modifier();//blank modifier
									combinedModifier.nodes.remove(0);//remove Empty
									for(int j = n + 1 ; j < i ; j++){
										//loop through nodes inbetween brackets
										combinedModifier.nodes.add(nodesCopy.get(j));
									}
									for(int j = n + 1 ; j < i ; j++){
										//delete nodes inbetween brackets
										nodesCopy.remove(n + 1);
									}
									nodesCopy.remove(n + 1);//remove close bracket
									
									Number result = ((OpenBracket)testNode).modify(combinedModifier.execute());//modify using open bracket method
									nodesCopy.remove(n);    //remove open bracket
									
									nodesCopy.add(n, result);
									loop = false;
								}
							}else{
								throw new Exception("Error : Non equal brackets");
							}
						}
						//break from for loop and do the whole thing again until there are no brackets
						brackets = true;
						break;
					}
				}
			}
			
			//there could be some OpenBrackets left if doing something like cos(45
			//modify number after OpenBracket
			int n = nodesCopy.size() - 1;
			while(n >= 0){
				if(nodesCopy.get(n) instanceof OpenBracket){
					if(n + 1 < nodesCopy.size()){
						nodesCopy.set(n + 1, ((OpenBracket)nodesCopy.get(n)).modify( nodesCopy.get(n + 1).execute() ));
					}else{
						throw new Exception("Error : Nothing inside bracket");
					}
					nodesCopy.remove(n);
				}else{
					n--;
				}
			}
			
			//sort out minuses and pluses that dont have a number either side
			//look for instances like 5 x -3, 5 / +3, etc
			boolean loop = true;
			while(loop == true){
				loop = false;
				for(int i = 0 ; i < nodesCopy.size() ; i++){
					if(nodesCopy.get(i) instanceof Subtract){
						if(i == 0){
							nodesCopy.get(i + 1).invertNumber();
							nodesCopy.remove(0);
							loop = true;
						}else{
							if(nodesCopy.get(i - 1) instanceof Operator && (nodesCopy.get(i + 1) instanceof Number ||  nodesCopy.get(i + 1) instanceof Modifier)){
								
								nodesCopy.get(i + 1).invertNumber();
								nodesCopy.remove(i);	
								loop = true;
							}
						}
					}else if(nodesCopy.get(i) instanceof Add){
						if(i == 0){
							nodesCopy.remove(0);	
						}else{
							if(nodesCopy.get(i - 1) instanceof Operator && (nodesCopy.get(i + 1) instanceof Number ||  nodesCopy.get(i + 1) instanceof Modifier)){
								nodesCopy.remove(i);	
								loop = true;
							}
						}
					}
				}
			}
			
			//find sets of 2 consecutive numbers or modifiers, then insert a multiply
			for(int i = 0 ; i < (nodesCopy.size() - 1) ; i++){
				if(nodesCopy.get(i) instanceof Number ||  nodesCopy.get(i) instanceof Modifier){
					if(nodesCopy.get(i + 1) instanceof Number ||  nodesCopy.get(i + 1) instanceof Modifier){
						nodesCopy.add((i + 1), new Multiply());
					}
				}
			}
			
			if(nodesCopy.size() == 1 && nodesCopy.get(0) instanceof Modifier){
				//in this case there are no operators
				nodesCopy.add(new Number(nodesCopy.get(0).execute()));
				nodesCopy.remove(0);
			}
		
		
			boolean divides = true; 
			boolean multiply = true;
			boolean add = true;
			boolean subtract = true;
			
			//simply by executing numbers either side of operators
			while(divides){
				divides = false; //set to true if find divide to do another pass
				for(int i = 0 ; i < nodesCopy.size() ; i++){
					if(nodesCopy.get(i) instanceof Divide){
						
						Divide d = (Divide)nodesCopy.get(i);
						d.setNumber1(nodesCopy.get(i-1).execute());
						d.setNumber2(nodesCopy.get(i+1).execute());
						nodesCopy.set(i, d.execute());
						nodesCopy.remove(i+1);
						nodesCopy.remove(i-1);
						
						divides = true;
						
						break;
					}
				}
			}
			while(multiply){
				multiply = false;
				for(int i = 0 ; i < nodesCopy.size() ; i++){
					if(nodesCopy.get(i) instanceof Multiply){
						
						Multiply d = (Multiply)nodesCopy.get(i);
						d.setNumber1(nodesCopy.get(i-1).execute());
						d.setNumber2(nodesCopy.get(i+1).execute());
						nodesCopy.set(i, d.execute());
						nodesCopy.remove(i+1);
						nodesCopy.remove(i-1);
						
						multiply = true;
						break;
					}
				}
			}
			while(add){
				add = false; 
				for(int i = 0 ; i < nodesCopy.size() ; i++){
					if(nodesCopy.get(i) instanceof Add){
						
						Add d = (Add)nodesCopy.get(i);
						d.setNumber1(nodesCopy.get(i-1).execute());
						d.setNumber2(nodesCopy.get(i+1).execute());
						nodesCopy.set(i, d.execute());
						nodesCopy.remove(i+1);
						nodesCopy.remove(i-1);
						
						add = true;
						break;
					}
				}
			}
			while(subtract){
				subtract = false;
				for(int i = 0 ; i < nodesCopy.size() ; i++){
					if(nodesCopy.get(i) instanceof Subtract){
						
						Subtract d = (Subtract)nodesCopy.get(i);
						d.setNumber1(nodesCopy.get(i-1).execute());
						d.setNumber2(nodesCopy.get(i+1).execute());
						nodesCopy.set(i, d.execute());
						nodesCopy.remove(i+1);
						nodesCopy.remove(i-1);
						
						subtract = true;
						break;
					}
				}
			}
			
			return (Number)nodesCopy.get(0);
		}catch(IndexOutOfBoundsException e){
			//throw new Exception("Operator has not enough values");
			throw new ArrayIndexOutOfBoundsException("No Number next to Operator");
		}
	}
	
	
	@Override
	public void invertNumber() {
		for(Node n:nodes){
			n.invertNumber();
		}
	}
	
	private void logNodesCopy(ArrayList<Node> nodesCopy){
		String s = new String();
		for(int i = 0 ; i < nodesCopy.size() ; i++){
			s += nodesCopy.get(i).toString();
			s += " ";
		}
		Log.d("nodes Copy :", s);
	}
	
	@Override
	public String toString() {
		String s = new String();
		for(Node n:nodes){
			s += n.toString();
			s += " ";
		}
		s += " AI:" + activeIndex;
		return s;
	}
	
	public Node getClone() {
		return new Modifier(this);
	}
	public ArrayList<Node> getNodeArray(){
		return nodes;
	}
	public boolean cursorAtStart(){
		if(activeIndex == 0){
			return nodes.get(0).cursorAtStart();
		}else{
			return false;
		}
	}
	public boolean cursorAtEnd(){
		if(activeIndex == nodes.size() - 1){
				return nodes.get(activeIndex).cursorAtEnd();
		}else{
			return false;
		}
	}
	@Override
	public void setCursor(int x, int y) {
		double distance = 100000;
		int index = 0;
		
		for(int i = 0 ; i < nodes.size() ; i++){
			if(!(nodes.get(i) instanceof Unselectable)){
				double d = nodes.get(i).getDistanceFrom(x, y);
				if(d < distance){
					distance = d;
					index = i;
				}
			}
		}
		activeIndex = index;
		nodes.get(index).setCursor(x, y);
	}
	@Override
	public void moveCursorRight() {
		if(nodes.get(activeIndex).cursorAtEnd()){
			if(activeIndex < nodes.size() - 1){ 
				activeIndex++;
				if(nodes.get(activeIndex) instanceof Unselectable){
					activeIndex++;
				}
				nodes.get(activeIndex).putCursorAtStart();
			}
		}else{
			nodes.get(activeIndex).moveCursorRight();
		}
	}
	@Override
	public void moveCursorLeft() {
		if(nodes.get(activeIndex).cursorAtStart()){
			if(activeIndex > 0){
				activeIndex--;
				if(nodes.get(activeIndex) instanceof Unselectable){
					activeIndex--;
				}
				nodes.get(activeIndex).putCursorAtEnd();
			}
		}else{
			nodes.get(activeIndex).moveCursorLeft();
		}
	}
	
	@Override
	public void moveCursorUp() {
			nodes.get(activeIndex).moveCursorUp();
	}
	@Override
	public void moveCursorDown() {
			nodes.get(activeIndex).moveCursorDown();
	}
	@Override
	public boolean cursorAtTop() {
		return nodes.get(activeIndex).cursorAtTop();
	}
	@Override
	public boolean cursorAtBottom() {
		return nodes.get(activeIndex).cursorAtBottom();
	}
	
	@Override
	public boolean isEmpty() {
		return(nodes.size() == 1 && nodes.get(0) instanceof Empty);
	}
	

	public void addNode(Node n){
		if(n instanceof Modifier){
			addModifier((Modifier)n);
		}else if (n instanceof Number){
			addNumber((Number)n);
		}else if(n instanceof Unselectable){
			addUnselectable((Unselectable)n);
		}
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public void addBracketsAtEnds(){
		if(!(nodes.get(0) instanceof OpenBracket)){
			nodes.add(0, new OpenBracket());
			nodes.add(0, new Empty());
			activeIndex += 2;
		}
		if(!(nodes.get(nodes.size() - 1) instanceof CloseBracket)){
			nodes.add(new CloseBracket());
			nodes.add(new Empty());
			activeIndex += 2;
		}
	}
}


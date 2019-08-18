package chess;
import javax.swing.JOptionPane;

public class Piece{
	int color;
	char value;
	boolean isClicked, isAttacked, isHighlighted, moved, isMoving, twoMove, isAnimating;
	//standard piece
	public Piece (int a, char b){
		if(b == ' '){
			color = 2;
		}
		else{
			color = a;
		}
		value = b;
		isClicked = false;
		isAttacked = false;
		isHighlighted = false;
		moved = false;
		isMoving = false;
		twoMove = false;
		isAnimating = false;
	}
	//blank squares
	public Piece(char b){
		color = 2;
		value = b;
		isClicked = false;
		isAttacked = false;
		isHighlighted = false;
		moved = false;
		isMoving = false;
		twoMove = false;
		isAnimating = false;
	}
	//border
	public Piece(int a){
		color = 3;
		value = 'X';
		isClicked = false;
		isAttacked = false;
		isHighlighted = false;
		moved = false;
		isMoving = false;
		twoMove = false;
		isAnimating = false;
	}
	//duplicate
	public Piece(Piece p){
		color = p.getColor();
		value = p.getValue();
		isClicked = p.isClicked();
		isAttacked = p.isAttacked();
		isHighlighted = p.isHighlighted();
		moved = p.hasMoved();
		isMoving = p.isMoving();
		twoMove = p.isTwoMove();
		isAnimating = p.isAnimating();
	}
	//turn a square blank
	public void empty(){
		color = 2;
		value = ' ';
		isClicked = false;
		isHighlighted = false;
		moved = false;
		isMoving = false;
		twoMove = false;
		isAnimating = false;
	}
	//set this piece to p
	public void setPiece(Piece p){
		color = p.getColor();
		value = p.getValue();
		isClicked = false;
		isHighlighted = false;
		moved = true;
		p.empty();
		isMoving = false;
		twoMove = false;
		isAnimating = false;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int c) {
		color = c;
	}
	public char getValue() {
		return value;
	}
	public void setValue(char p) {
		value = p;
	}
	public boolean isClicked() {
		return isClicked;
	}
	public void setClicked(boolean b) {
		isClicked = b;
	}
	public boolean isAttacked() {
		return isAttacked;
	}
	public void setAttacked(boolean b) {
		isAttacked = b;
	}
	public boolean isHighlighted(){
		return isHighlighted;
	}
	public void setHighlighted(boolean b){
		isHighlighted = b;
	}
	public boolean hasMoved(){
		return moved;
	}
	public void setMoved(boolean b){
		moved = b;
	}	
	public boolean isMoving(){
		return isMoving;
	}
	public void setMoving(boolean b){
		isMoving = b;
	}
	public boolean isTwoMove() {
		return twoMove;
	}
	public void setTwoMove(boolean b) {
		twoMove = b;
	}
	public boolean isAnimating(){
		return isAnimating;
	}
	public void setAnimating(boolean b){
		isAnimating = b;
	}
	//promote piece
	public char promote(){
		boolean success = false;
		String p = "";
		char a = '~';
		while(!success){
			p = JOptionPane.showInputDialog("Choose a piece to promote your pawn to");
			try{
				p = p.toUpperCase();
				if(p.equals("ROOK") || p.equals("R") || p.equals("BISHOP") || p.equals("B") || p.equals("KNIGHT") || p.equals("N") || p.equals("QUEEN") || p.equals("Q")){
					success = true;
					if(p.equals("KNIGHT")){
						a = 'N';
					}
					else{
						a = p.charAt(0);
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "Invalid Choice, Choose Again");
				}
			}
			catch(Exception e){
				success = true;
			}
		}
		if(a != '~'){
			value = a;
		}
		return a;
	}
	//pointless for final version, needed for Chess V1-3
	public void checkKing(Piece[][] b, Piece[][] n, int x, int y, int t){
		for(int i = -1; i < 2; i++){
			if(!n[x+i][y+1].isAttacked() && b[x+i][y+1].getColor()!=3 && b[x+i][y+1].getColor()!=t){
				b[x+i][y+1].setHighlighted(true);
			}
			else{
				b[x+i][y+1].setHighlighted(false);
			}
		}
		for(int i = -1; i < 2; i++){
			if(!n[x+i][y-1].isAttacked() && b[x+i][y-1].getColor()!=3 && b[x+i][y-1].getColor()!=t){
				b[x+i][y-1].setHighlighted(true);
			}
			else{
				b[x+i][y-1].setHighlighted(false);
			}
		}
		if(!n[x-1][y].isAttacked() && b[x-1][y].getColor()!=3 && b[x-1][y].getColor()!=t){
			b[x-1][y].setHighlighted(true);
		}
		else{
			b[x-1][y].setHighlighted(false);
		}
		if(!n[x+1][y].isAttacked() && b[x+1][y].getColor()!=3 && b[x+1][y].getColor()!=t){
			b[x+1][y].setHighlighted(true);
		}
		else{
			b[x+1][y].setHighlighted(false);
		}
		if(!b[x][y].hasMoved() && !b[x][8].hasMoved() && b[x][6].getColor()==2 && b[x][7].getColor()==2 && !b[x][6].isAttacked() && !b[x][7].isAttacked()){
			b[x][7].setHighlighted(true);
		}
		if(!b[x][y].hasMoved() && !b[x][1].hasMoved() && b[x][2].getColor()==2 && b[x][3].getColor()==2 && b[x][4].getColor()==2 && !b[x][2].isAttacked() && !b[x][3].isAttacked() && !b[x][4].isAttacked()){
			b[x][3].setHighlighted(true);
		}
	}
	//if chosen piece is valid, highlights possible moves. l=0 then highlights, l=1 it sets attacks
	public void check(Piece[][] b, int x, int y, int t, int l){
		if(color == t){
			if(value == 'P'){
				if(t==0){
					if(b[x-1][y].getColor()==2){
						if(l==0){
							b[x-1][y].setHighlighted(true);
						}
						if(!hasMoved()){
							if(b[x-2][y].getColor()==2){
								if(l==0){
									b[x-2][y].setHighlighted(true);
								}
							}
						}
					}
					if(b[x-1][y-1].getColor()==1){
						if(l==0){
							b[x-1][y-1].setHighlighted(true);
						}
						else if(l==1){
							b[x-1][y-1].setAttacked(true);
						}
					}
					if(b[x-1][y+1].getColor()==1){
						if(l==0){
							b[x-1][y+1].setHighlighted(true);
						}
						else if(l==1){
							b[x-1][y+1].setAttacked(true);
						}
					}
					if(b[x][y-1].isTwoMove() && b[x][y-1].getColor() == 1 && l == 0){
						b[x-1][y-1].setHighlighted(true);
					}
					if(b[x][y+1].isTwoMove() && b[x][y+1].getColor() == 1 && l == 0){
						b[x-1][y+1].setHighlighted(true);
					}
				}
				else if(t==1){
					if(b[x+1][y].getColor()==2){
						if(l==0){
							b[x+1][y].setHighlighted(true);
						}
						if(!hasMoved()){
							if(b[x+2][y].getColor()==2){
								if(l==0){
									b[x+2][y].setHighlighted(true);
								}
							}
						}
					}
					if(b[x+1][y-1].getColor()==0){
						if(l==0){
							b[x+1][y-1].setHighlighted(true);
						}
						else if(l==1){
							b[x+1][y-1].setAttacked(true);
						}
					}
					if(b[x+1][y+1].getColor()==0){
						if(l==0){
							b[x+1][y+1].setHighlighted(true);
						}
						else if(l==1){
							b[x+1][y+1].setAttacked(true);
						}
					}
					if(b[x][y-1].isTwoMove() && b[x][y-1].getColor() == 0 && l == 0){
						b[x+1][y-1].setHighlighted(true);
					}
					if(b[x][y+1].isTwoMove() && b[x][y+1].getColor() == 0 && l == 0){
						b[x+1][y+1].setHighlighted(true);
					}
				}
			}
			if(value == 'R' || value == 'Q'){
				int i=1;
				while(b[x+i][y].getColor()!=3 && b[x+i][y].getColor()!=t){
					if(b[x+i][y].getColor()!=2){
						if(l==0){
							b[x+i][y].setHighlighted(true);
						}
						else if(l==1){
							b[x+i][y].setAttacked(true);
						}
						break;
					}
					else{
						if(l==0){
							b[x+i][y].setHighlighted(true);
						}
						else if(l==1){
							b[x+i][y].setAttacked(true);
						}
						i++;
					}
				}
				i=-1;
				while(b[x+i][y].getColor()!=3 && b[x+i][y].getColor()!=t){
					if(b[x+i][y].getColor()!=2){
						if(l==0){
							b[x+i][y].setHighlighted(true);
						}
						else if(l==1){
							b[x+i][y].setAttacked(true);
						}
						break;
					}
					else{
						if(l==0){
							b[x+i][y].setHighlighted(true);
						}
						else if(l==1){
							b[x+i][y].setAttacked(true);
						}
						i--;
					}
				}
				i=1;
				while(b[x][y+i].getColor()!=3 && b[x][y+i].getColor()!=t){
					if(b[x][y+i].getColor()!=2){
						if(l==0){
							b[x][y+i].setHighlighted(true);
						}
						else if(l==1){
							b[x][y+i].setAttacked(true);
						}
						break;
					}
					else{
						if(l==0){
							b[x][y+i].setHighlighted(true);
						}
						else if(l==1){
							b[x][y+i].setAttacked(true);
						}
						i++;
					}
				}
				i=-1;
				while(b[x][y+i].getColor()!=3 && b[x][y+i].getColor()!=t){
					if(b[x][y+i].getColor()!=2){
						if(l==0){
							b[x][y+i].setHighlighted(true);
						}
						else if(l==1){
							b[x][y+i].setAttacked(true);
						}
						break;
					}
					else{
						if(l==0){
							b[x][y+i].setHighlighted(true);
						}
						else if(l==1){
							b[x][y+i].setAttacked(true);
						}
						i--;
					}
				}
			}
			if(value == 'B' || value == 'Q'){
				int i = 1;
				while(b[x+i][y+i].getColor()!=3 && b[x+i][y+i].getColor()!=t){
					if(b[x+i][y+i].getColor()!=2){
						if(l==0){
							b[x+i][y+i].setHighlighted(true);
						}
						else if(l==1){
							b[x+i][y+i].setAttacked(true);	
						}
						break;
					}
					else{
						if(l==0){
							b[x+i][y+i].setHighlighted(true);
						}
						else if(l==1){
							b[x+i][y+i].setAttacked(true);	
						}
						i++;
					}
				}
				i = -1;
				while(b[x+i][y+i].getColor()!=3 && b[x+i][y+i].getColor()!=t){
					if(b[x+i][y+i].getColor()!=2){
						if(l==0){
							b[x+i][y+i].setHighlighted(true);
						}
						else if(l==1){
							b[x+i][y+i].setAttacked(true);	
						}
						break;
					}
					else{
						if(l==0){
							b[x+i][y+i].setHighlighted(true);
						}
						else if(l==1){
							b[x+i][y+i].setAttacked(true);	
						}
						i--;
					}
				}
				i = 1;
				int j = -1;
				while(b[x+i][y+j].getColor()!=3 && b[x+i][y+j].getColor()!=t){
					if(b[x+i][y+j].getColor()!=2){
						if(l==0){
							b[x+i][y+j].setHighlighted(true);
						}
						else if(l==1){
							b[x+i][y+j].setAttacked(true);	
						}
						break;
					}
					else{
						if(l==0){
							b[x+i][y+j].setHighlighted(true);
						}
						else if(l==1){
							b[x+i][y+j].setAttacked(true);	
						}
						i++;
						j--;
					}
				}
				i = -1;
				j = 1;
				while(b[x+i][y+j].getColor()!=3 && b[x+i][y+j].getColor()!=t){
					if(b[x+i][y+j].getColor()!=2){
						if(l==0){
							b[x+i][y+j].setHighlighted(true);
						}
						else if(l==1){
							b[x+i][y+j].setAttacked(true);	
						}
						break;
					}
					else{
						if(l==0){
							b[x+i][y+j].setHighlighted(true);
						}
						else if(l==1){
							b[x+i][y+j].setAttacked(true);	
						}
						i--;
						j++;
					}
				}
			}
			if(value == 'N'){
				if(b[x+1][y].getColor()!=3){
					if(b[x+2][y+1].getColor()!=t && b[x+2][y+1].getColor()!=3){
						if(l==0){
							b[x+2][y+1].setHighlighted(true);
						}
						else if(l==1){
							b[x+2][y+1].setAttacked(true);
						}
					}
					if(b[x+2][y-1].getColor()!=t && b[x+2][y-1].getColor()!=3){
						if(l==0){
							b[x+2][y-1].setHighlighted(true);
						}
						else if(l==1){
							b[x+2][y-1].setAttacked(true);
						}
					}
				}
				if(b[x-1][y].getColor()!=3){
					if(b[x-2][y+1].getColor()!=t && b[x-2][y+1].getColor()!=3){
						if(l==0){
							b[x-2][y+1].setHighlighted(true);
						}
						else if(l==1){
							b[x-2][y+1].setAttacked(true);
						}
					}
					if(b[x-2][y-1].getColor()!=t && b[x-2][y-1].getColor()!=3){
						if(l==0){
							b[x-2][y-1].setHighlighted(true);
						}
						else if(l==1){
							b[x-2][y-1].setAttacked(true);
						}
					}
				}
				if(b[x][y+1].getColor()!=3){
					if(b[x+1][y+2].getColor()!=t && b[x+1][y+2].getColor()!=3){
						if(l==0){
							b[x+1][y+2].setHighlighted(true);
						}
						else if(l==1){
							b[x+1][y+2].setAttacked(true);
						}
					}
					if(b[x-1][y+2].getColor()!=t && b[x-1][y+2].getColor()!=3){
						if(l==0){
							b[x-1][y+2].setHighlighted(true);
						}
						else if(l==1){
							b[x-1][y+2].setAttacked(true);
						}
					}
				}
				if(b[x][y-1].getColor()!=3){
					if(b[x+1][y-2].getColor()!=t && b[x+1][y-2].getColor()!=3){
						if(l==0){
							b[x+1][y-2].setHighlighted(true);
						}
						else if(l==1){
							b[x+1][y-2].setAttacked(true);
						}
					}
					if(b[x-1][y-2].getColor()!=t && b[x-1][y-2].getColor()!=3){
						if(l==0){
							b[x-1][y-2].setHighlighted(true);
						}
						else if(l==1){
							b[x-1][y-2].setAttacked(true);
						}
					}
				}
			}
			if(value == 'K'){
				for(int i = -1; i < 2; i++){
					if(b[x+i][y+1].getColor()!=3 && b[x+i][y+1].getColor()!=t){
						if(l==0){
							b[x+i][y+1].setHighlighted(true);
						}
						else if(l==1){
							b[x+i][y+1].setAttacked(true);
						}
					}
					if(b[x+i][y-1].getColor()!=3 && b[x+i][y-1].getColor()!=t){
						if(l==0){
							b[x+i][y-1].setHighlighted(true);
						}
						else if(l==1){
							b[x+i][y-1].setAttacked(true);
						}
					}
				}
				if(b[x-1][y].getColor()!=3 && b[x-1][y].getColor()!=t){
					if(l==0){
						b[x-1][y].setHighlighted(true);
					}
					else if(l==1){
						b[x-1][y].setAttacked(true);
					}
				}
				if(b[x+1][y].getColor()!=3 && b[x+1][y].getColor()!=t){
					if(l==0){
						b[x+1][y].setHighlighted(true);
					}
					else if(l==1){
						b[x+1][y].setAttacked(true);
					}
				}
				if(!b[x][y].hasMoved() && !b[x][8].hasMoved() && b[x][6].getColor()==2 && b[x][7].getColor()==2 && !b[x][6].isAttacked() && !b[x][7].isAttacked() && !b[x][y].isAttacked()){
					b[x][7].setHighlighted(true);
				}
				if(!b[x][y].hasMoved() && !b[x][1].hasMoved() && b[x][2].getColor()==2 && b[x][3].getColor()==2 && b[x][4].getColor()==2 && !b[x][2].isAttacked() && !b[x][3].isAttacked() && !b[x][4].isAttacked() && !b[x][y].isAttacked()){
					b[x][3].setHighlighted(true);
				}
			}
		}
	}
	public String toString(){
		char h;
		if(isHighlighted){
			h = 'H';
		}
		else{
			h = ' ';
		}
		char c = 'K';
		if(color == 0){
			c = 'W';
		}
		else if(color == 1){
			c = 'B';
		}
		if(color == 2){
			return ("[ " + h + " ]");
		}
		if(color == 3){
			return (" " + h + " ");
		}
		return ("[" + c + h + value + "]");
	}
}
package chess;

public class Board {
	private Piece[][] pieces;
	private int checkingX, checkingY, newPosX, newPosY, oldPosX, oldPosY;
	private int selectedX, selectedY, enX, enY;
	public Board(Piece[][] b){
		pieces = b;
		checkingX = -1;
		checkingY = -1;
		newPosX = -1;
		newPosY = -1;
		oldPosX = -1;
		oldPosY = -1;
		selectedX = -1;
		selectedY = -1;
	}
	public Board(Board b){
		Piece[][] newPieces = new Piece[10][10];
		for(int x = 0; x < 10; x++){
			for(int y = 0; y < 10; y++){
				newPieces[x][y] = new Piece(b.getPieces()[x][y]);
			}
		}
		pieces = newPieces;
		checkingX = b.getCheckingX();
		checkingY = b.getCheckingY();
		newPosX = b.getNewPosX();
		newPosY = b.getNewPosY();
		oldPosX = b.getOldPosX();
		oldPosY = b.getOldPosY();
		enX = b.getEnX();
		enY = b.getEnY();
		selectedX = b.getSelectedX();
		selectedY = b.getSelectedY();
	}
	public Piece[][] getPieces(){
		return pieces;
	}
	public void setPieces(Piece[][] p){
		pieces = p;
	}
	public int getCheckingX() {
		return checkingX;
	}
	public void setCheckingX(int x) {
		checkingX = x;
	}
	public int getCheckingY() {
		return checkingY;
	}
	public void setCheckingY(int y) {
		checkingY = y;
	}
	public int getNewPosX() {
		return newPosX;
	}
	public void setNewPosX(int x) {
		newPosX = x;
	}
	public int getNewPosY() {
		return newPosY;
	}
	public void setNewPosY(int y) {
		newPosY = y;
	}
	public int getOldPosX() {
		return oldPosX;
	}
	public void setOldPosX(int x) {
		oldPosX = x;
	}
	public int getOldPosY() {
		return oldPosY;
	}
	public void setOldPosY(int y) {
		oldPosY = y;
	}	
	public int getSelectedX(){
		return selectedX;
	}
	public void setSelectedX(int x){
		selectedX = x;
	}
	public int getSelectedY(){
		return selectedY;
	}
	public void setSelectedY(int y){
		selectedY = y;
	}
	public int getEnX(){
		return enX;
	}
	public void setEnX(int a){
		enX = a;
	}
	public int getEnY(){
		return enY;
	}
	public void setEnY(int a){
		enY = a;
	}
}

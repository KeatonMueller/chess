package chess;

import java.util.Arrays;

public class Moves {
	private static long test = Long.parseLong("0000000000000000000000000000000000000000000000000000000000100000", 2);
	private static long FILE_A = 72340172838076673L;
	private static long FILE_H = -9187201950435737472L;
	private static long FILE_AB = 217020518514230019L;
	private static long FILE_GH = -4557430888798830400L;
	private static long RANK_1 = -72057594037927936L;
	private static long RANK_4 = 1095216660480L;
	private static long RANK_5 = 4278190080L;
	private static long RANK_8 = 255L;
	private static long CENTER = 103481868288L;
	private static long EXTENDED_CENTER = 6622906269440L;
	private static long KING_SIDE = -1085102592571150096L;
	private static long QUEEN_SIDE = 1085102592571150095L;
	private static long KNIGHT_SPAN = 43234889994L;
	private static long KING_SPAN = 460039L;
	private static long NOT_MY_PIECES;
	private static long MY_PIECES;
	private static long EMPTY;
	private static long OCCUPIED;
	private static long CASTLE_ROOKS[] = {63, 56, 7, 0};
	private static long RankMasks8[] = {
			0xFFL, 0xFF00L, 0xFF0000L, 0xFF000000L, 0xFF00000000L, 0xFF0000000000L, 0xFF000000000000L, 0xFF00000000000000L	
	};
	private static long FileMasks8[] = {
			0x101010101010101L, 0x202020202020202L, 0x404040404040404L, 0x808080808080808L,
			0x1010101010101010L, 0x2020202020202020L, 0x4040404040404040L, 0x8080808080808080L
	};
	private static long DiagonalMasks8[] = {
			0x1L, 0x102L, 0x10204L, 0x1020408L, 0x102040810L, 0x10204081020L, 0x1020408102040L,
			0x102040810204080L, 0x204081020408000L, 0x408102040800000L, 0x810204080000000L,
			0x1020408000000000L, 0x2040800000000000L, 0x4080000000000000L, 0x8000000000000000L
	};
	private static long AntiDiagonalMasks8[] = {
			0x80L, 0x8040L, 0x804020L, 0x80402010L, 0x8040201008L, 0x804020100804L, 0x80402010080402L,
			0x8040201008040201L, 0x4020100804020100L, 0x2010080402010000L, 0x1008040201000000L,
			0x804020100000000L, 0x402010000000000L, 0x201000000000000L, 0x100000000000000L
	};
	/*public static void main(String[] args) {
		drawBitBoard(test);
		drawBitBoard(1L<<5);
		drawBitBoard((test>>>5)&1);
	}*/
	public static String possibleMovesW(long wP, long wR, long wN, long wB, long wQ, long wK, long bP, long bR, long bN, long bB, long bQ, long bK, long eP, boolean cWK, boolean cWQ, boolean cBK, boolean cBQ){
		NOT_MY_PIECES = ~(wP|wR|wN|wB|wQ|wK|bK);
		MY_PIECES = wP|wR|wN|wB|wQ;
		OCCUPIED = wP|wR|wN|wB|wQ|wK|bP|bR|bN|bB|bQ|bK;
		EMPTY = ~OCCUPIED;
		String list = possibleWP(wP, bP, eP);
		list += possibleB(wB);
		list += possibleR(wR);
		list += possibleQ(wQ);
		list += possibleN(wN);
		list += possibleCW(wR, cWK, cWQ);
		return list;
	}
	public static String possibleMovesB(long wP, long wR, long wN, long wB, long wQ, long wK, long bP, long bR, long bN, long bB, long bQ, long bK, long eP, boolean cWK, boolean cWQ, boolean cBK, boolean cBQ){
		NOT_MY_PIECES = ~(bP|bR|bN|bB|bQ|bK|wK);
		MY_PIECES = bP|bR|bN|bB|bQ;
		OCCUPIED = wP|wR|wN|wB|wQ|wK|bP|bR|bN|bB|bQ|bK;
		EMPTY = ~OCCUPIED;
		String list = possibleBP(bP, wP, eP);
		list += possibleB(bB);
		list += possibleR(bR);
		list += possibleQ(bQ);
		list += possibleN(bN);
		list += possibleCB(bR, cBK, cBQ);
		return list;
	}
	public static long makeMove(long board, String move, char type) {
		if (Character.isDigit(move.charAt(3))) {//'regular' move
			int start=(Character.getNumericValue(move.charAt(0))*8)+(Character.getNumericValue(move.charAt(1)));
			int end=(Character.getNumericValue(move.charAt(2))*8)+(Character.getNumericValue(move.charAt(3)));
			if (((board>>>start)&1)==1) {board&=~(1L<<start); board|=(1L<<end);} else {board&=~(1L<<end);}
		} else if (move.charAt(3)=='P') {//pawn promotion
			int start, end;
			if (Character.isUpperCase(move.charAt(2))) {
				start=Long.numberOfTrailingZeros(FileMasks8[move.charAt(0)-'0']&RankMasks8[1]);
				end=Long.numberOfTrailingZeros(FileMasks8[move.charAt(1)-'0']&RankMasks8[0]);
			} else {
				start=Long.numberOfTrailingZeros(FileMasks8[move.charAt(0)-'0']&RankMasks8[6]);
				end=Long.numberOfTrailingZeros(FileMasks8[move.charAt(1)-'0']&RankMasks8[7]);
			}
			if (type==move.charAt(2)) {board&=~(1L<<start); board|=(1L<<end);} else {board&=~(1L<<end);}
		} else if (move.charAt(3)=='E') {//en passant
			int start, end;
			if (Character.isUpperCase(move.charAt(2))) {
				start=Long.numberOfTrailingZeros(FileMasks8[move.charAt(0)-'0']&RankMasks8[4]);
				end=Long.numberOfTrailingZeros(FileMasks8[move.charAt(1)-'0']&RankMasks8[5]);
				board&=~(1L<<(FileMasks8[move.charAt(1)-'0']&RankMasks8[4]));
			} else {
				start=Long.numberOfTrailingZeros(FileMasks8[move.charAt(0)-'0']&RankMasks8[3]);
				end=Long.numberOfTrailingZeros(FileMasks8[move.charAt(1)-'0']&RankMasks8[2]);
				board&=~(1L<<(FileMasks8[move.charAt(1)-'0']&RankMasks8[3]));
			}
			if (((board>>>start)&1)==1) {board&=~(1L<<start); board|=(1L<<end);}
		} else {
			System.out.print("ERROR: Invalid move type");
		}
		return board;
	}
	public static long makeMoveEP(long board,String move) {
		if (Character.isDigit(move.charAt(3))) {
			int start=(Character.getNumericValue(move.charAt(0))*8)+(Character.getNumericValue(move.charAt(1)));
			if ((Math.abs(move.charAt(0)-move.charAt(2))==2)&&(((board>>>start)&1)==1)) {//pawn double push
				return FileMasks8[move.charAt(1)-'0'];
			}
		}
		return 0;
	}
	public static long HVMoves(int s){
		long binaryS=1L<<s;
        long possibilitiesHorizontal = (OCCUPIED - 2 * binaryS) ^ Long.reverse(Long.reverse(OCCUPIED) - 2 * Long.reverse(binaryS));
        long possibilitiesVertical = ((OCCUPIED&FileMasks8[s % 8]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(OCCUPIED&FileMasks8[s % 8]) - (2 * Long.reverse(binaryS)));
        return (possibilitiesHorizontal&RankMasks8[s / 8]) | (possibilitiesVertical&FileMasks8[s % 8]);
    }
	public static long DMoves(int s){
		long binaryS=1L<<s;
        long possibilitiesDiagonal = ((OCCUPIED&DiagonalMasks8[(s / 8) + (s % 8)]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(OCCUPIED&DiagonalMasks8[(s / 8) + (s % 8)]) - (2 * Long.reverse(binaryS)));
        long possibilitiesAntiDiagonal = ((OCCUPIED&AntiDiagonalMasks8[(s / 8) + 7 - (s % 8)]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(OCCUPIED&AntiDiagonalMasks8[(s / 8) + 7 - (s % 8)]) - (2 * Long.reverse(binaryS)));
        return (possibilitiesDiagonal&DiagonalMasks8[(s / 8) + (s % 8)]) | (possibilitiesAntiDiagonal&AntiDiagonalMasks8[(s / 8) + 7 - (s % 8)]);
    }
	public static String possibleWP(long wP, long bP, long eP){
		String list = "";
		//capture right
		long PAWN_MOVES = (wP>>7)&NOT_MY_PIECES&OCCUPIED&~RANK_8&~FILE_A;
		long poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		while(poss != 0){
			int x = Long.numberOfTrailingZeros(poss);
			list += ""+(x/8+1)+(x%8-1)+(x/8)+(x%8);
			PAWN_MOVES &= ~poss;
			poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		}
		//capture left
		PAWN_MOVES = (wP>>9)&NOT_MY_PIECES&OCCUPIED&~RANK_8&~FILE_H;
		poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		while(poss != 0){
			int x = Long.numberOfTrailingZeros(poss);
			list += "" + (x/8+1) + (x%8+1) + (x/8) + (x%8);
			PAWN_MOVES &= ~poss;
			poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		}
		//forward one
		PAWN_MOVES = (wP>>8)&EMPTY&~RANK_8;
		poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		while(poss != 0){
			int x = Long.numberOfTrailingZeros(poss);
			list += "" + (x/8+1) + (x%8) + (x/8) + (x%8);
			PAWN_MOVES &= ~poss;
			poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		}
		//forward two
		PAWN_MOVES = (wP>>16)&EMPTY&(EMPTY>>8)&RANK_4;
		poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		while(poss != 0){
			int x = Long.numberOfTrailingZeros(poss);
			list += "" + (x/8+2) + (x%8) + (x/8) + (x%8);
			PAWN_MOVES &= ~poss;
			poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		}
		//y1,y2,Promotion Type, "P"
		//capture right promotion
		PAWN_MOVES = (wP>>7)&NOT_MY_PIECES&OCCUPIED&RANK_8&~FILE_A;
		poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		while(poss != 0){
			int x = Long.numberOfTrailingZeros(poss);
			list += "" + (x%8-1) + (x%8) + "QP" + (x%8-1) + (x%8) + "RP" + (x%8-1) + (x%8) + "NP" + (x%8-1) + (x%8) + "BP";
			PAWN_MOVES &= ~poss;
			poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		}
		//capture left promotion
		PAWN_MOVES = (wP>>9)&NOT_MY_PIECES&OCCUPIED&RANK_8&~FILE_H;
		poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		while(poss != 0){
			int x = Long.numberOfTrailingZeros(poss);
			list += "" + (x%8+1) + (x%8) + "QP" + (x%8+1) + (x%8) + "RP" + (x%8+1) + (x%8) + "NP" + (x%8+1) + (x%8) + "BP";
			PAWN_MOVES &= ~poss;
			poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		}
		//forward one promotion
		PAWN_MOVES = (wP>>8)&EMPTY&RANK_8;
		poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		while(poss != 0){
			int x = Long.numberOfTrailingZeros(poss);
			list += "" + (x%8) + (x%8) + "QP" + (x%8) + (x%8) + "RP" + (x%8) + (x%8) + "NP" + (x%8) + (x%8) + "BP";
			PAWN_MOVES &= ~poss;
			poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		}
		//en passant
		//y1,y2, ,E
		//en passant right
		poss = (wP<<1)&bP&eP&RANK_5&~FILE_A;
		if(poss != 0){
			int x = Long.numberOfTrailingZeros(poss);
			list += "" + (x%8-1) + (x%8) + "WE";
		}
		//en passant left
		poss = (wP>>1)&bP&eP&RANK_5&~FILE_H;
		if(poss != 0){
			int x = Long.numberOfTrailingZeros(poss);
			list += "" + (x%8+1) + (x%8) + "WE";
		}
		return list;
	}
	public static String possibleBP(long bP, long wP, long eP){
		String list = "";
		//capture right
		long PAWN_MOVES = (bP<<7)&NOT_MY_PIECES&OCCUPIED&~RANK_1&~FILE_H;
		long poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		while(poss != 0){
			int x = Long.numberOfTrailingZeros(poss);
			list += ""+(x/8-1)+(x%8+1)+(x/8)+(x%8);
			PAWN_MOVES &= ~poss;
			poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		}
		//capture left
		PAWN_MOVES = (bP<<9)&NOT_MY_PIECES&OCCUPIED&~RANK_1&~FILE_A;
		poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		while(poss != 0){
			int x = Long.numberOfTrailingZeros(poss);
			list += "" + (x/8-1) + (x%8-1) + (x/8) + (x%8);
			PAWN_MOVES &= ~poss;
			poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		}
		//forward one
		PAWN_MOVES = (bP<<8)&EMPTY&~RANK_1;
		poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		while(poss != 0){
			int x = Long.numberOfTrailingZeros(poss);
			list += "" + (x/8-1) + (x%8) + (x/8) + (x%8);
			PAWN_MOVES &= ~poss;
			poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		}
		//forward two
		PAWN_MOVES = (bP<<16)&EMPTY&(EMPTY<<8)&RANK_5;
		poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		while(poss != 0){
			int x = Long.numberOfTrailingZeros(poss);
			list += "" + (x/8-2) + (x%8) + (x/8) + (x%8);
			PAWN_MOVES &= ~poss;
			poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		}
		//y1,y2,Promotion Type, "P"
		//capture right promotion
		PAWN_MOVES = (bP<<7)&NOT_MY_PIECES&OCCUPIED&RANK_1&~FILE_H;
		poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		while(poss != 0){
			int x = Long.numberOfTrailingZeros(poss);
			list += "" + (x%8+1) + (x%8) + "qP" + (x%8+1) + (x%8) + "rP" + (x%8+1) + (x%8) + "nP" + (x%8+1) + (x%8) + "bP";
			PAWN_MOVES &= ~poss;
			poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		}
		//capture left promotion
		PAWN_MOVES = (bP<<9)&NOT_MY_PIECES&OCCUPIED&RANK_1&~FILE_A;
		poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		while(poss != 0){
			int x = Long.numberOfTrailingZeros(poss);
			list += "" + (x%8-1) + (x%8) + "qP" + (x%8-1) + (x%8) + "rP" + (x%8-1) + (x%8) + "nP" + (x%8-1) + (x%8) + "bP";
			PAWN_MOVES &= ~poss;
			poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		}
		//forward one promotion
		PAWN_MOVES = (bP<<8)&EMPTY&RANK_1;
		poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		while(poss != 0){
			int x = Long.numberOfTrailingZeros(poss);
			list += "" + (x%8) + (x%8) + "qP" + (x%8) + (x%8) + "rP" + (x%8) + (x%8) + "nP" + (x%8) + (x%8) + "bP";
			PAWN_MOVES &= ~poss;
			poss = PAWN_MOVES & ~(PAWN_MOVES-1);
		}
		//en passant
		//y1,y2, ,E
		//en passant right
		poss = (bP<<1)&wP&eP&RANK_4&~FILE_A;
		if(poss != 0){
			int x = Long.numberOfTrailingZeros(poss);
			list += "" + (x%8-1) + (x%8) + "bE";
		}
		//en passant left
		poss = (bP>>1)&wP&eP&RANK_4&~FILE_H;
		if(poss != 0){
			int x = Long.numberOfTrailingZeros(poss);
			list += "" + (x%8+1) + (x%8) + "bE";
		}
		return list;
	}
	public static String possibleCW(long wR, boolean cWK, boolean cWQ){
		String list="";
		if (cWK&&(((1L<<CASTLE_ROOKS[0])&wR)!=0)) {
			if ((OCCUPIED&((1L<<61)|(1L<<62)))==0) {
				list+="7476";
			}
		}
		if (cWQ&&(((1L<<CASTLE_ROOKS[1])&wR)!=0)){
			if ((OCCUPIED&((1L<<57)|(1L<<58)|(1L<<59)))==0) {
				list+="7472";
			}
		}
		return list;
	}
	public static String possibleCB(long bR, boolean cBK, boolean cBQ){
		String list="";
		if (cBK&&(((1L<<CASTLE_ROOKS[2])&bR)!=0)){
			if ((OCCUPIED&((1L<<5)|(1L<<6)))==0) {
				list+="0406";
			}
		}
		if (cBQ&&(((1L<<CASTLE_ROOKS[3])&bR)!=0)){
			if ((OCCUPIED&((1L<<1)|(1L<<2)|(1L<<3)))==0) {
				list+="0402";
			}
		}
		return list;
	}
	public static String possibleB(long B){
		String list = "";
		long i = B & ~(B-1);
		long poss;
		while(i != 0){
			int iLocation = Long.numberOfTrailingZeros(i);
			poss = DMoves(iLocation)&NOT_MY_PIECES;
			long j  = poss & ~(poss-1);
			while(j != 0){
				int x = Long.numberOfTrailingZeros(j);
				list += "" + (iLocation/8) + (iLocation%8) + (x/8) + (x%8);
				poss &= ~j;
				j = poss & ~(poss-1);
			}
			B &= ~i;
			i = B & ~(B-1);
		}
		return list;
	}
	public static String possibleR(long R){
		String list = "";
		long i = R & ~(R-1);
		long poss;
		while(i != 0){
			int iLocation = Long.numberOfTrailingZeros(i);
			poss = HVMoves(iLocation)&NOT_MY_PIECES;
			long j  = poss & ~(poss-1);
			while(j != 0){
				int x = Long.numberOfTrailingZeros(j);
				list += "" + (iLocation/8) + (iLocation%8) + (x/8) + (x%8);
				poss &= ~j;
				j = poss & ~(poss-1);
			}
			R &= ~i;
			i = R & ~(R-1);
		}
		return list;
	}
	public static String possibleQ(long Q){
		String list = "";
		long i = Q & ~(Q-1);
		long poss;
		while(i != 0){
			int iLocation = Long.numberOfTrailingZeros(i);
			poss = HVMoves(iLocation)&DMoves(iLocation)&NOT_MY_PIECES;
			long j  = poss & ~(poss-1);
			while(j != 0){
				int x = Long.numberOfTrailingZeros(j);
				list += "" + (iLocation/8) + (iLocation%8) + (x/8) + (x%8);
				poss &= ~j;
				j = poss & ~(poss-1);
			}
			Q &= ~i;
			i = Q & ~(Q-1);
		}
		return list;
	}
	public static String possibleN(long N){
		String list = "";
		long i = N & ~(N-1);
		long poss;
		while(i != 0){
			int iLocation = Long.numberOfTrailingZeros(i);
			if(iLocation > 18){
				poss = KNIGHT_SPAN<<(iLocation-18);
			}
			else{
				poss = KNIGHT_SPAN>>(18-iLocation);
			}
			if(iLocation%8 < 4){
				poss &= ~FILE_GH&NOT_MY_PIECES;
			}
			else{
				poss &= ~FILE_AB&NOT_MY_PIECES;
			}
			long j  = poss & ~(poss-1);
			while(j != 0){
				int x = Long.numberOfTrailingZeros(j);
				list += "" + (iLocation/8) + (iLocation%8) + (x/8) + (x%8);
				poss &= ~j;
				j = poss & ~(poss-1);
			}
			N &= ~i;
			i = N & ~(N-1);
		}
		return list;
	}
	public static String possibleK(long wK){
		String list = "";
		long i = wK & ~(wK-1);
		long poss;
		while(i != 0){
			int iLocation = Long.numberOfTrailingZeros(i);
			if(iLocation > 9){
				poss = KING_SPAN<<(iLocation-9);
			}
			else{
				poss = KING_SPAN>>(9-iLocation);
			}
			if(iLocation%8 < 4){
				poss &= ~FILE_GH&NOT_MY_PIECES;
			}
			else{
				poss &= ~FILE_AB&NOT_MY_PIECES;
			}
			long j  = poss & ~(poss-1);
			while(j != 0){
				int x = Long.numberOfTrailingZeros(j);
				list += "" + (iLocation/8) + (iLocation%8) + (x/8) + (x%8);
				poss &= ~j;
				j = poss & ~(poss-1);
			}
			wK &= ~i;
			i = wK & ~(wK-1);
		}
		return list;
	}
	public static long unsafeForBlack(long wP, long wR, long wN, long wB, long wQ, long wK, long bP, long bR, long bN, long bB, long bQ, long bK){
		long unsafe;
		OCCUPIED = wP|wR|wN|wB|wQ|wK|bP|bR|bN|bB|bQ|bK;
		unsafe = ((wP>>>7)&~FILE_A);
		unsafe |= ((wP>>>9)&~FILE_H);
		long poss;
		long i = wN&~(wN-1);
		while(i != 0){
			int iLocation = Long.numberOfTrailingZeros(i);
			if(iLocation > 18){
				poss = KNIGHT_SPAN<<(iLocation-18);
			}
			else{
				poss = KNIGHT_SPAN>>(18 - iLocation);
			}
			if(iLocation%8 < 4){
				poss &= ~FILE_GH;
			}
			else{
				poss &= ~FILE_AB;
			}
			unsafe |= poss;
			wN &= ~i;
			i = wN&~(wN-1);
		}
		long QB = wB|wQ;
		i = QB&~(QB-1);
		while(i != 0){
			int iLocation = Long.numberOfTrailingZeros(i);
			poss = DMoves(iLocation);
			unsafe |= poss;
			QB &= ~i;
			i = QB&~(QB-1);
		}
		long QR = wQ|wR;
		i = QR&~(QR-1);
		while(i != 0){
			int iLocation = Long.numberOfTrailingZeros(i);
			poss = HVMoves(iLocation);
			unsafe |= poss;
			QR &= ~i;
			i = QR&~(QR-1);
		}
		int iLocation = Long.numberOfTrailingZeros(wK);
		if(iLocation > 9){
			poss = KING_SPAN<<(iLocation-9);
		}
		else{
			poss = KING_SPAN>>(9-iLocation);
		}
		if(iLocation%8 < 4){
			poss &= ~FILE_GH;
		}
		else{
			poss &= ~FILE_AB;
		}
		unsafe |= poss;
		return unsafe;
	}
	public static long unsafeForWhite(long wP, long wR, long wN, long wB, long wQ, long wK, long bP, long bR, long bN, long bB, long bQ, long bK){
		long unsafe;
		OCCUPIED = wP|wR|wN|wB|wQ|wK|bP|bR|bN|bB|bQ|bK;
		unsafe = ((bP<<7)&~FILE_H);
		unsafe |= ((bP<<9)&~FILE_A);
		long poss;
		long i = bN&~(bN-1);
		while(i != 0){
			int iLocation = Long.numberOfTrailingZeros(i);
			if(iLocation > 18){
				poss = KNIGHT_SPAN<<(iLocation-18);
			}
			else{
				poss = KNIGHT_SPAN>>(18 - iLocation);
			}
			if(iLocation%8 < 4){
				poss &= ~FILE_GH;
			}
			else{
				poss &= ~FILE_AB;
			}
			unsafe |= poss;
			bN &= ~i;
			i = bN&~(bN-1);
		}
		long QB = bB|bQ;
		i = QB&~(QB-1);
		while(i != 0){
			int iLocation = Long.numberOfTrailingZeros(i);
			poss = DMoves(iLocation);
			unsafe |= poss;
			QB &= ~i;
			i = QB&~(QB-1);
		}
		long QR = bQ|bR;
		i = QR&~(QR-1);
		while(i != 0){
			int iLocation = Long.numberOfTrailingZeros(i);
			poss = HVMoves(iLocation);
			unsafe |= poss;
			QR &= ~i;
			i = QR&~(QR-1);
		}
		int iLocation = Long.numberOfTrailingZeros(wK);
		if(iLocation > 9){
			poss = KING_SPAN<<(iLocation-9);
		}
		else{
			poss = KING_SPAN>>(9-iLocation);
		}
		if(iLocation%8 < 4){
			poss &= ~FILE_GH;
		}
		else{
			poss &= ~FILE_AB;
		}
		unsafe |= poss;
		return unsafe;
	}
	public static void drawBitBoard(long b){
		String[][] board = new String[8][8];
		for(int x = 0; x < 64; x++){
			board[x/8][x%8] = "";
		}
		for(int x = 0; x < 64; x++){
			if(((b>>>x)&1) == 1){
				board[x/8][x%8] = "P";
			}
			if(board[x/8][x%8].equals("")){
				board[x/8][x%8] = " ";
			}
		}
		for(int x = 0; x < 8; x++){
			System.out.println(Arrays.toString(board[x]));
		}
	}
}

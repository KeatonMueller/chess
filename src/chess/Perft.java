package chess;

public class Perft {
    public static String moveToAlgebra(String move)
    {
        String moveString="";
        moveString+=""+(char)(move.charAt(1)+49);
        moveString+=""+('8'-move.charAt(0));
        moveString+=""+(char)(move.charAt(3)+49);
        moveString+=""+('8'-move.charAt(2));
        return moveString;
    }
    static int perftTotalMoveCounter=0;
    protected static int perftMoveCounter=0;
    protected static int perftMaxDepth=3;
    public static void perftRoot(long wP,long wR,long wN,long wB,long wQ,long wK,long bP,long bR,long bN,long bB,long bQ,long bK,long eP,boolean cWK,boolean cWQ,boolean cBK,boolean cBQ,boolean whiteToMove,int depth) {
        String moves;
        if (whiteToMove) {
            moves=Moves.possibleMovesW(wP,wR,wN,wB,wQ,wK,bP,bR,bN,bB,bQ,bK,eP,cWK,cWQ,cBK,cBQ);
        } else {
            moves=Moves.possibleMovesB(wP,wR,wN,wB,wQ,wK,bP,bR,bN,bB,bQ,bK,eP,cWK,cWQ,cBK,cBQ);
        }
        for (int i=0;i<moves.length();i+=4) {
            long wPt=Moves.makeMove(wP, moves.substring(i,i+4), 'P'), wRt=Moves.makeMove(wR, moves.substring(i,i+4), 'R'),
                    wNt=Moves.makeMove(wN, moves.substring(i,i+4), 'N'), wBt=Moves.makeMove(wB, moves.substring(i,i+4), 'B'),
                    wQt=Moves.makeMove(wQ, moves.substring(i,i+4), 'Q'), wKt=Moves.makeMove(wK, moves.substring(i,i+4), 'K'),
                    bPt=Moves.makeMove(bP, moves.substring(i,i+4), 'p'), bRt=Moves.makeMove(bR, moves.substring(i,i+4), 'r'),
                    bNt=Moves.makeMove(bN, moves.substring(i,i+4), 'n'), bBt=Moves.makeMove(bB, moves.substring(i,i+4), 'b'),
                    bQt=Moves.makeMove(bQ, moves.substring(i,i+4), 'q'), bKt=Moves.makeMove(bK, moves.substring(i,i+4), 'k'),
                    ePt=Moves.makeMoveEP(wP|bP,moves.substring(i,i+4));
            boolean cWKt=cWK,cWQt=cWQ,cBKt=cBK,cBQt=cBQ;
            if (Character.isDigit(moves.charAt(i+3))) {//'regular' move
                int start=(Character.getNumericValue(moves.charAt(i))*8)+(Character.getNumericValue(moves.charAt(i+1)));
                if (((1L<<start)&wK)!=0) {cWKt=false; cWQt=false;}
                else if (((1L<<start)&bK)!=0) {cBKt=false; cBQt=false;}
                else if (((1L<<start)&wB&(1L<<63))!=0) {cWKt=false;}
                else if (((1L<<start)&wB&(1L<<56))!=0) {cWQt=false;}
                else if (((1L<<start)&bB&(1L<<7))!=0) {cBKt=false;}
                else if (((1L<<start)&bB&1L)!=0) {cBQt=false;}
            }
            if (((wKt&Moves.unsafeForWhite(wPt,wRt,wNt,wBt,wQt,wKt,bPt,bRt,bNt,bBt,bQt,bKt))==0 && whiteToMove) ||
                    ((bKt&Moves.unsafeForBlack(wPt,wRt,wNt,wBt,wQt,wKt,bPt,bRt,bNt,bBt,bQt,bKt))==0 && !whiteToMove)) {
                perft(wPt,wRt,wNt,wBt,wQt,wKt,bPt,bRt,bNt,bBt,bQt,bKt,ePt,cWKt,cWQt,cBKt,cBQt,!whiteToMove,depth+1);
                System.out.println(moveToAlgebra(moves.substring(i,i+4))+" "+perftMoveCounter);
                perftTotalMoveCounter+=perftMoveCounter;
                perftMoveCounter=0;
            }
        }
    }
    public static void perft(long wP,long wR,long wN,long wB,long wQ,long wK,long bP,long bR,long bN,long bB,long bQ,long bK,long eP,boolean cWK,boolean cWQ,boolean cBK,boolean cBQ,boolean whiteToMove,int depth){
        if (depth<perftMaxDepth) {
            String moves;
            if (whiteToMove) {
                moves=Moves.possibleMovesW(wP,wR,wN,wB,wQ,wK,bP,bR,bN,bB,bQ,bK,eP,cWK,cWQ,cBK,cBQ);
            } else {
                moves=Moves.possibleMovesB(wP,wR,wN,wB,wQ,wK,bP,bR,bN,bB,bQ,bK,eP,cWK,cWQ,cBK,cBQ);
            }
            for (int i=0;i<moves.length();i+=4) {
                long wPt=Moves.makeMove(wP, moves.substring(i,i+4), 'P'), wNt=Moves.makeMove(wN, moves.substring(i,i+4), 'N'),
                        wBt=Moves.makeMove(wB, moves.substring(i,i+4), 'B'), wRt=Moves.makeMove(wR, moves.substring(i,i+4), 'R'),
                        wQt=Moves.makeMove(wQ, moves.substring(i,i+4), 'Q'), wKt=Moves.makeMove(wK, moves.substring(i,i+4), 'K'),
                        bPt=Moves.makeMove(bP, moves.substring(i,i+4), 'p'), bNt=Moves.makeMove(bN, moves.substring(i,i+4), 'n'),
                        bBt=Moves.makeMove(bB, moves.substring(i,i+4), 'b'), bRt=Moves.makeMove(bR, moves.substring(i,i+4), 'r'),
                        bQt=Moves.makeMove(bQ, moves.substring(i,i+4), 'q'), bKt=Moves.makeMove(bK, moves.substring(i,i+4), 'k'),
                        ePt=Moves.makeMoveEP(wP|bP,moves.substring(i,i+4));
                boolean cWKt=cWK,cWQt=cWQ,cBKt=cBK,cBQt=cBQ;
                if (Character.isDigit(moves.charAt(3))) {//'regular' move
                    int start=(Character.getNumericValue(moves.charAt(i))*8)+(Character.getNumericValue(moves.charAt(i+1)));
                    if (((1L<<start)&wK)!=0) {cWKt=false; cWQt=false;}
                    if (((1L<<start)&bK)!=0) {cBKt=false; cBQt=false;}
                    if (((1L<<start)&wR&(1L<<63))!=0) {cWKt=false;}
                    if (((1L<<start)&wR&(1L<<56))!=0) {cWQt=false;}
                    if (((1L<<start)&bR&(1L<<7))!=0) {cBKt=false;}
                    if (((1L<<start)&bR&1L)!=0) {cBQt=false;}
                }
                if (((wKt&Moves.unsafeForWhite(wPt,wRt,wNt,wBt,wQt,wKt,bPt,bRt,bNt,bBt,bQt,bKt))==0 && whiteToMove) ||
                        ((bKt&Moves.unsafeForBlack(wPt,wRt,wNt,wBt,wQt,wKt,bPt,bRt,bNt,bBt,bQt,bKt))==0 && !whiteToMove)) {
                    if (depth+1==perftMaxDepth) {perftMoveCounter++;}
                    perft(wPt,wRt,wNt,wBt,wQt,wKt,bPt,bRt,bNt,bBt,bQt,bKt,ePt,cWKt,cWQt,cBKt,cBQt,!whiteToMove,depth+1);
                }
            }
        }
    }
}
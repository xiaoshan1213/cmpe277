package com.cmpe277.sam.gameoflifedraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by sam on 2/21/17.
 */

public class GameView extends View {

    private static int[][] board;
    private int viewWidth;
    private int viewHeight;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        board = new int[12][12];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        viewWidth = this.getWidth();
        viewHeight = this.getHeight();
        for(int i=0;i<13;i++){
            canvas.drawLine(0,viewHeight/12*i,viewWidth,viewHeight/12*i,paint);
            canvas.drawLine(viewWidth/12*i,0,viewWidth/12*i,viewHeight,paint);
        }
        for(int i=0;i<12;i++){
            for(int j=0;j<12;j++){
                paint.setTextSize(50);
                if(board[i][j]==1){
                    paint.setColor(Color.RED);
                    canvas.drawCircle(viewWidth/12*i+viewWidth/24,viewHeight/12*j+viewHeight/24,viewWidth/24,paint);
                }
                //canvas.drawText(board[i][j]+"",viewWidth/12*i+viewWidth/24-10,viewHeight/12*j+viewHeight/24+10,paint);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX()/(viewWidth/12);
        int y = (int)event.getY()/(viewHeight/12);
        if(event.getAction()==MotionEvent.ACTION_UP) {
            if (board[x][y] == 1) {
                board[x][y] = 0;
            } else {
                board[x][y] = 1;
            }
        }
        invalidate();
        return true;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);

    }

    public void reset(){
        for(int i=0;i<board.length;i++){
            for(int j=0;j<board[0].length;j++){
                if(board[i][j]==1)
                    board[i][j]=0;
            }
        }
        invalidate();
    }

    public void next(){
        int m=board.length,n=board[0].length;
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                int lives=liveNeighbors(board,m,n,i,j);
                if(board[i][j]==1&&lives>=2&&lives<=3)
                    board[i][j]=3;
                if(board[i][j]==0&&lives==3)
                    board[i][j]=2;
            }
        }
        for(int i=0;i<m;i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] >>= 1;
            }
        }
        invalidate();
    }

    private int liveNeighbors(int[][] board,int m,int n,int i,int j){
        int lives=0;
        for(int x=Math.max(i-1,0);x<=Math.min(i+1,m-1);x++)
            for(int y=Math.max(j-1,0);y<=Math.min(j+1,n-1);y++)
                lives+=board[x][y]&1;
        lives-=board[i][j]&1;
        return lives;
    }

    public int[] to1Darr(){
        int width=board.length;
        int height=board[0].length;
        int[] res=new int[width*height];
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                res[i*width+j]=board[i][j];
            }
        }
        return res;
    }

    public void restore2Darr(int[] arr){
        int width=board.length;
        int height=board[0].length;
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                board[i][j]=arr[i*width+j];
            }
        }
    }
}

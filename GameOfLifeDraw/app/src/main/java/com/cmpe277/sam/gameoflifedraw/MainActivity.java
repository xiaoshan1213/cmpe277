package com.cmpe277.sam.gameoflifedraw;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private GameView gameview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameview = (GameView) findViewById(R.id.gameview);
        if(savedInstanceState!=null) {
            int[] restorearr = savedInstanceState.getIntArray("arr");
            System.out.println(restorearr);
            gameview.restore2Darr(restorearr);
            gameview.invalidate();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int[] savedarr=gameview.to1Darr();
        outState.putIntArray("arr",savedarr);
    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        int[] restorearr=savedInstanceState.getIntArray("arr");
//        gameview.restore2Darr(restorearr);
//        gameview.invalidate();
//    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE) {
//            //Toast.makeText(this, "change1", Toast.LENGTH_SHORT).show();
//            setContentView(R.layout.activity_main);
//            //gameview = (GameView) findViewById(R.id.gameview);
//        }else{
//            //Toast.makeText(this, "change2", Toast.LENGTH_SHORT).show();
//            setContentView(R.layout.activity_main);
//            //gameview = (GameView) findViewById(R.id.gameview);
//        }
//    }

    public void reset(View view){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setMessage("are you sure to reset?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        gameview.reset();
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    public void next(View view){
        gameview.next();
    }
}

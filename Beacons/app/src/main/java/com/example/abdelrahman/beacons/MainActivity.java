package com.example.abdelrahman.beacons;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.bluetooth.BluetoothManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter BTAdapter= BluetoothAdapter.getDefaultAdapter();
    private ArrayList<String> allDetectedBeaconsRSSI;
    private ArrayList<String> allDetectedBeaconsMac;

    private Activity act;
    private TextView mac;
    private  String LOG_TAG= MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        act=this;
        allDetectedBeaconsRSSI=new ArrayList<String>();
        allDetectedBeaconsMac=new ArrayList<String>();

        //BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        //BTAdapter = mBluetoothManager.getAdapter();
       // BTAdapter=(BluetoothAdapter) this.getSystemService(Context.BLUETOOTH_SERVICE);
        //BTAdapter.startDiscovery();

        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        mac=(TextView)findViewById(R.id.mac);
        Button boton = (Button) findViewById(R.id.button);
        boton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                BTAdapter.startDiscovery();
                //for(int i=0;i<allDetectedBeacons.size();i++){
                 //   Log.v(LOG_TAG,"Number "+i+" is "+allDetectedBeacons.get(i));
                //}

               // registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            }
        });
        ScheduledExecutorService scheduledExecutorService= Executors.newScheduledThreadPool(5);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                BTAdapter.startDiscovery();
                //for(int i=0;i<allDetectedBeacons.size();i++){
                  //  Log.v(LOG_TAG,"Number "+i+" is "+allDetectedBeacons.get(i));
                //}
            }
        },0,10, TimeUnit.SECONDS);
    }
//pathLoss=2.01 shadowFading=3.2
    double calculateDistance(double power,double initialPower,double pathLoss,double shadowFading, double initialDistance){
        double distance=initialPower-power+shadowFading;
        distance=distance/(10*pathLoss);
        distance=Math.pow(10,distance);
        return  distance*initialDistance;
    }


    private final BroadcastReceiver receiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.v(LOG_TAG,"ahahahah");
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                //String macAdd=intent.getStringExtra(BluetoothDevice.)
        //        String macAdd=intent.getStringExtra(BluetoothDevice.)
                //Toast.makeText(act, name + " => " + rssi + "dBm\n",
                  //      Toast.LENGTH_SHORT).show();
                //allDetectedBeacons.add(name + " => " + rssi + "dBm");
                //Log.v(LOG_TAG,name + " => " + rssi + "dBm and MAC address => "+device.getAddress()+"\n");
                if(device.getAddress().toString().equals("20:91:48:4C:5A:66")||device.getAddress().toString().equals("20:91:48:4C:5B:AF")
                        ||device.getAddress().toString().equals("20:91:48:42:03:73")){

                    mac.setText(mac.getText()+"\n"+name + " => " + rssi + "dBm and MAC address => "+device.getAddress()+"\n");
                    boolean check=false;
                    for(int i=0;allDetectedBeaconsMac.size()<0;i++){
                        if(allDetectedBeaconsMac.get(i).equals(device.getAddress())){
                        check=true;
                        }
                    }
                    if(check==false) {
                        allDetectedBeaconsRSSI.add(rssi + "");
                        allDetectedBeaconsMac.add(device.getAddress());

                        Log.v(LOG_TAG, allDetectedBeaconsRSSI.size() + " <=Size");
                    }


                    if(allDetectedBeaconsRSSI.size()==3){
                        double distance;
                        Circle c1=null;
                        Circle c2=null;
                        Circle c3=null;
                        Point p1=new Point(0,0);//back 66
                        Point p2=new Point(4,0);//right af
                        Point p3=new Point(0,5.5);//left 73

                        switch (allDetectedBeaconsMac.get(0)){
                            case "20:91:48:4C:5A:66":
                                distance=calculateDistance(Double.parseDouble(allDetectedBeaconsRSSI.get(0)),
                                        -77,2.01,3.2,1);

                                c1 = new Circle(p1, distance);
                                break;
                            case "20:91:48:4C:5B:AF":
                                distance=calculateDistance(Double.parseDouble(allDetectedBeaconsRSSI.get(0)),
                                        -79,2.01,3.2,1);
                                c2=new Circle(p2,distance);
                                break;
                            case "20:91:48:42:03:73":
                                distance=calculateDistance(Double.parseDouble(allDetectedBeaconsRSSI.get(0)),
                                        -75,2.01,3.2,1);
                                c3=new Circle(p3,distance);

                                break;
                            default: distance=0;

                        }

//                            mac.setText(allDetectedBeaconsMac.get(0)+" distance "+distance+"\n");

                        switch (allDetectedBeaconsMac.get(1)){
                            case "20:91:48:4C:5A:66":
                                distance=calculateDistance(Double.parseDouble(allDetectedBeaconsRSSI.get(1)),
                                        -77,2.01,3.2,1);
                                c1=new Circle(p1,distance);

                                break;
                            case "20:91:48:4C:5B:AF":
                                distance=calculateDistance(Double.parseDouble(allDetectedBeaconsRSSI.get(1)),
                                        -79,2.01,3.2,1);
                                c2=new Circle(p2,distance);

                                break;
                            case "20:91:48:42:03:73":
                                distance=calculateDistance(Double.parseDouble(allDetectedBeaconsRSSI.get(1)),
                                        -75,2.01,3.2,1);
                                c3=new Circle(p3,distance);

                                break;
                            default: distance=0;

                        }

                        //mac.setText(mac.getText()+allDetectedBeaconsMac.get(1)+" distance "+distance+"\n");

                        switch (allDetectedBeaconsMac.get(2)){
                            case "20:91:48:4C:5A:66":
                                distance=calculateDistance(Double.parseDouble(allDetectedBeaconsRSSI.get(2)),
                                        -77,2.01,3.2,1);
                                c1=new Circle(p1,distance);

                                break;
                            case "20:91:48:4C:5B:AF":
                                distance=calculateDistance(Double.parseDouble(allDetectedBeaconsRSSI.get(2)),
                                        -79,2.01,3.2,1);
                                c2=new Circle(p2,distance);

                                break;
                            case "20:91:48:42:03:73":
                                distance=calculateDistance(Double.parseDouble(allDetectedBeaconsRSSI.get(2)),
                                        -75,2.01,3.2,1);
                                c3=new Circle(p3,distance);

                                break;
                            default: distance=0;

                        }
                       // mac.setText(mac.getText()+allDetectedBeaconsMac.get(2)+" distance "+distance+"\n");
                   //     mac.setText(mac.getText()+"20:91:48:40:2D:6A"+" distance "+2.588517375928574+"\n");


                        if(c1!=null&&c2!=null&&c3!=null){
                        ArrayList<Circle> circles=new ArrayList<>();
                            circles.add(c1);
                            circles.add(c2);
                            circles.add(c3);

                            ArrayList<Point> points=new ArrayList<>();
                            ArrayList<Point> inter=getAllIntersectingPoints(circles);

                            for(int i=0;i<inter.size();i++){
                                if(isContainedInCircles(inter.get(i),circles)){
                                    points.add(inter.get(i));
                                    for(int j=0;j<points.size();j++){
                                    mac.setText("  X => "+points.get(j).x+" Y=> "+points.get(j).y+"\n");
                                    }
                                }
                            }
                            allDetectedBeaconsRSSI.clear();
                            allDetectedBeaconsMac.clear();


                        }



                    }

                }
            }
        }
    };
    class Point{
        public double x;
        public double y;

        public Point(double xx,double yy){
        x=Double.parseDouble(new DecimalFormat("##.#").format(xx));
            y=Double.parseDouble(new DecimalFormat("##.#").format(yy));
        }

    }
    class Circle{
        public double radius;
        public Point center;
        public Circle(Point pointt,double rad){
            center=pointt;
            radius=Double.parseDouble(new DecimalFormat("##.#").format(rad));
        }
    }
    double getTwoPointsDistance(Point p1,Point p2){
    return  Double.parseDouble(new DecimalFormat("##.#").format(Math.sqrt(Math.pow((p1.x - p2.x), 2)+Math.pow((p1.y - p2.y), 2))));
    }

    ArrayList<Point> getTwoCirclesIntersectingPoints(Circle c1,Circle c2){
    Point p1=c1.center;
    Point p2=c2.center;
    double r1=c1.radius;
        double r2=c2.radius;
        double distance=getTwoPointsDistance(p1,p2);
        if(distance>=(r1+r2)||distance<=Math.abs(r1-r2)){
            return null;
        }
        double a=(Math.pow(r1,2)-Math.pow(r2,2)+Math.pow(distance,2))/(2*distance);
        double h=Math.sqrt(Math.pow(r1,2)-Math.pow(a,2));
        double x0=p1.x+a*(p2.x-p1.x)/distance;
        double y0=p1.y+a*(p2.y-p1.y)/distance;
        double rx=-(p2.y - p1.y) * (h/distance);
        double ry = -(p2.x - p1.x) * (h / distance);
        Point p11=new Point(x0+rx,y0-ry);
        Point p12=new Point(x0-rx,y0+ry);
            ArrayList<Point> pret=new ArrayList<Point>();
            pret.add(p11);
            pret.add(p12);
            //{p11,p12};
        // Log.v(LOG_TAG,"dISTANCE AHO "+distance);
        //Log.v(LOG_TAG,"p11 x=>  "+p11.x+" y=> "+p11.y);
        //Log.v(LOG_TAG,"p12 x=>  "+p12.x+" y=> "+p12.y);

        return pret;
    }

    ArrayList<Point> getAllIntersectingPoints(ArrayList<Circle> circles){
        ArrayList<Point> points=new ArrayList<Point>();
        int length=circles.size();
        ArrayList<Point> res;
            for(int i=0;i<length;i++){
                int j=i+1;
                for(int k=j;k<length;k++){
                    res=getTwoCirclesIntersectingPoints(circles.get(i),circles.get(k));
                    if(res!=null){
  //                      Log.v(LOG_TAG,"ANa magtesh hena asln 5aleeees");
                        for(int l=0;l<res.size();l++){
//                         Log.v(LOG_TAG,"place "+i+" X=> "+res.get(l).x+" y=> "+res.get(l).y);
                        }
                        points.addAll(res);
                    }
                }
            }
        return points;
    }
     boolean isContainedInCircles(Point point,ArrayList<Circle> circles){
        for(int i=0;i<circles.size();i++){
            if(getTwoPointsDistance(point,circles.get(i).center)>(circles.get(i).radius)) {
                return false;
            }
        }
         return true;
     }

}

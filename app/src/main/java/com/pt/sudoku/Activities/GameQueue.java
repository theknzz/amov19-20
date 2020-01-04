package com.pt.sudoku.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.pt.sudoku.ComunicationBackgroundService;
import com.pt.sudoku.R;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class GameQueue extends AppCompatActivity {

    static boolean running = false;
    Intent intentComm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_queue);

        intentComm=  new Intent(this, ComunicationBackgroundService.class);

        intentComm.putExtra("mode", getIntent().getStringExtra("mode"));

        intentComm.putExtra("type", getIntent().getStringExtra("type"));

        intentComm.putExtra("level", getIntent().getIntExtra("level", 8));


        TextView textView = findViewById(R.id.waitingText);

        if (getIntent().getStringExtra("type").equals("server")) {

            textView.setText("connect to:" + getLocalIpAddress());
        }

        if (intentComm.getStringExtra("type").equals("client")) {
            clientDlg();
        }

        if (running == false) {
            //ContextCompat.startForegroundService(this, intentClient);
            running = true;

            if (intentComm.getStringExtra("type").equals("server")) {
                startService(intentComm);
            }
        }
    }

    void clientDlg() {
        final EditText edtIP = new EditText(this);
        final AlertDialog ad = new AlertDialog.Builder(this).setTitle("Connect to server")
                .setMessage("Type the server's IP").setView(edtIP)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intentComm.putExtra("ip", edtIP.getText().toString());
                        startService(intentComm);
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                }).create();

        edtIP.setMaxLines(1);

        edtIP.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (edtIP.getText().toString().isEmpty())
                    return false;
                intentComm.putExtra("ip", edtIP.getText().toString());
                startService(intentComm);
                ad.dismiss();
                return true;
            }
        });
        edtIP.setText("10.0.2.2");

        ad.show();
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

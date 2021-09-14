package com.marc.decoditeccamfast.clases.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.marc.decoditeccamfast.Dashboard;
import com.marc.decoditeccamfast.R;
import com.marc.decoditeccamfast.ScanRequestRemote;
import com.marc.decoditeccamfast.clases.http.SocketHandler;
import com.marc.decoditeccamfast.clases.models.Constants;

import java.io.Serializable;

import io.socket.client.Socket;

public class ListenScanerRequest extends Service {

    private static final String TAG = "SCANREQUESTSERVICE";
    private static final String CHANNEL_ID = "SCANNOTI";

    public ListenScanerRequest() {
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Servicio Creado...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Servicio Iniciado");
        createNotificationChannel();
        Intent dash = new Intent(getApplicationContext(), Dashboard.class);
        dash.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, dash, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_scan_code)
                .setContentTitle("Escuchando Peticion...")
                .setContentText("Esta escuchando peticion para escanear codigos")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pi)
                .setAutoCancel(true);

        startForeground(120, builder.build());

        SocketHandler.getSocket().connect();
        SocketHandler.getSocket().on("requestScan", args -> {
            if(args[0] != null){
                createNotificationChannel();
                Intent scanRemote = new Intent(getApplicationContext(), ScanRequestRemote.class);
                scanRemote.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                scanRemote.putExtra("code", args[0].toString());
                PendingIntent remote = PendingIntent.getActivity(getApplicationContext(), 0, scanRemote, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder b = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_menu_camera)
                        .setContentTitle("¡Se Ha Solicitado Escanear!")
                        .setContentText("Toca La Notificacion para Escanear"+ args[0].toString())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(remote)
                        .setAutoCancel(true);
                NotificationManagerCompat mC = NotificationManagerCompat.from(getApplicationContext());
                mC.notify(201, b.build());

                /*Intent send = new Intent(Constants.ACTION_RUN_SERVICE)
                        .putExtra(Constants.EXTRA_REQUEST, args[0].toString());
                LocalBroadcastManager.getInstance(ListenScanerRequest.this).sendBroadcast(send);*/
}
        });

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Servicio destruido...");
        Intent localIntent = new Intent(Constants.ACTION_EXIT_SERVICE);
        LocalBroadcastManager.
                getInstance(ListenScanerRequest.this).sendBroadcast(localIntent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.description_channel);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
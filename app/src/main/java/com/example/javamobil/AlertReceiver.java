package com.example.javamobil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import androidx.core.app.NotificationCompat;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class AlertReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context,intent.getStringExtra("text").toString());
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        Log.d("TAG", "onReceive: "+intent.getStringExtra("id"));
        notificationHelper.getManager().notify(Integer.parseInt(intent.getStringExtra("id")), nb.build());
        Uri url = null;
        if (Objects.equals(intent.getStringExtra("sound"), "mixkit_city_alert_siren_loop"))
            url = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.mixkit_city_alert_siren_loop);
        else if (Objects.equals(intent.getStringExtra("sound"), "mixkit_classic_alarm"))
            url = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.mixkit_classic_alarm);
        else if (Objects.equals(intent.getStringExtra("sound"), "mixkit_digital_clock_digital_alarm_buzzer"))
            url = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.mixkit_digital_clock_digital_alarm_buzzer);
        else if (Objects.equals(intent.getStringExtra("sound"), "mixkit_retro_game_emergency_alarm"))
            url = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.mixkit_retro_game_emergency_alarm);

        final Ringtone[] _ringtone = {RingtoneManager.getRingtone(context, url)};


        Timer _start_ringtone = new Timer();
        //*Note: The phone needs to be set to ringing mode for it to work.
        _ringtone[0].play();

        _start_ringtone.schedule(new TimerTask() {
            @Override
            public void run() {
                _ringtone[0].stop();
                _ringtone[0] = null;
            }
            //Music plays for 100 seconds. 1s = 1000ms
        }, 100000);

    }
}

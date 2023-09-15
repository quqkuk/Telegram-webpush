package org.telegram.messenger;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import org.unifiedpush.android.connector.MessagingReceiver;

public class UnifiedPushReceiver extends MessagingReceiver {
    @Override
    public void onNewEndpoint(Context context, String endpoint, String instance){
        final long getPushStringEndTime = SystemClock.elapsedRealtime();
        AndroidUtilities.runOnUIThread(() -> {
            ApplicationLoader.postInitApplication();
            Utilities.globalQueue.postRunnable(() -> {
                SharedConfig.pushStringGetTimeEnd = getPushStringEndTime;
                PushListenerController.sendRegistrationToServer(PushListenerController.UnifiedPushListenerServiceProvider.INSTANCE, endpoint);
            });
        });
    }

    @Override
    public void onMessage(Context context, byte[] message, String instance){
        PushListenerController.processRemoteMessage(PushListenerController.UnifiedPushListenerServiceProvider.INSTANCE, message, SystemClock.elapsedRealtime());
    }

    @Override
    public void onRegistrationFailed(Context context, String instance){
        //TODO: Handle Failure
    }

    public void onUnregistered(Context context, String instance){

    }
}

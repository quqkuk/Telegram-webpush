package org.telegram.messenger;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import org.unifiedpush.android.connector.MessagingReceiver;

public class UnifiedPushReceiver extends MessagingReceiver {
    private final PushListenerController.UnifiedPushListenerServiceProvider INSTANCE = PushListenerController.UnifiedPushListenerServiceProvider.INSTANCE;

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
        PushListenerController.processRemoteMessage(INSTANCE, message, SystemClock.elapsedRealtime());
    }

    @Override
    public void onRegistrationFailed(Context context, String instance){
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("Failed to get endpoint");
        }
        SharedConfig.pushStringStatus = "__UNIFIEDPUSH_FAILED__";
        PushListenerController.sendRegistrationToServer(INSTANCE, null);
    }

    public void onUnregistered(Context context, String instance){

    }
}

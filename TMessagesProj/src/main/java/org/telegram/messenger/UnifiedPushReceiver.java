package org.telegram.messenger;

import android.content.Context;
import android.os.SystemClock;

import org.unifiedpush.android.connector.MessagingReceiver;

public class UnifiedPushReceiver extends MessagingReceiver {
    @Override
    public void onNewEndpoint(Context context, String endpoint, String instance){
        AndroidUtilities.runOnUIThread(() -> {
            ApplicationLoader.postInitApplication();
            PushListenerController.sendRegistrationToServer(PushListenerController.PUSH_TYPE_WEBPUSH, endpoint);
        });
    }

    @Override
    public void onMessage(Context context, byte[] message, String instance){
        PushListenerController.processRemoteMessage(PushListenerController.PUSH_TYPE_WEBPUSH, message, SystemClock.elapsedRealtime());
    }

    @Override
    public void onRegistrationFailed(Context context, String instance){

    }

    public void onUnregistered(Context context, String instance){

    }
}

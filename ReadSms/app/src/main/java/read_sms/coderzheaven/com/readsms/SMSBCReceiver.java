package read_sms.coderzheaven.com.readsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSBCReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get Bundle object contained in the SMS intent passed in
        Bundle bundle = intent.getExtras();
        SmsMessage[] smsm;
        String smsStr = "";
        if (null != bundle) {
            // Get the SMS message
            Object[] pduses = (Object[]) bundle.get("pdus");
            smsm = new SmsMessage[pduses.length];
            for (int i = 0; i < smsm.length; i++) {
                smsm[i] = SmsMessage.createFromPdu((byte[]) pduses[i]);
                smsStr += "Sent From: " + smsm[i].getOriginatingAddress();
                smsStr += "\r\nMessage: ";
                smsStr += smsm[i].getMessageBody().toString();
                smsStr += "\r\n";
            }
        }
    }
}
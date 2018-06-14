package com.pictureselect.android.contentobserver;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

public class SystemPictureVideoContentObserver extends ContentObserver {
    private Handler handler;
    private int handlerMsgWhat;
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public SystemPictureVideoContentObserver(Handler handler, int handlerMsgWhat) {
        super(handler);
        this.handler = handler;
        this.handlerMsgWhat = handlerMsgWhat;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        Message message = Message.obtain();
        message.obj = uri;
        message.what = handlerMsgWhat;
        handler.sendMessage(message);
    }
}

package de.inselhome.tvrecorder.client.ui;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.widget.TextView;

import de.inselhome.tvrecorder.common.objects.Channel;



public class ChannelView extends TextView {

    protected Channel channel;


    public ChannelView(Context context, Channel channel) {
        super(context);

        this.channel = channel;

        setHeight(40);
        setWidth(100);
        setGravity(Gravity.CENTER_VERTICAL);

        Resources res = getResources();

        int resId = res.getIdentifier(
            channel.getKey().toLowerCase(),
            "drawable",
            "de.inselhome.tvrecorder.client");

        if (resId == 0) {
            setText(channel.getDescription());
            setBackgroundDrawable(null);
        }
        else {
            setText(" ");
            setBackgroundDrawable(res.getDrawable(resId));
        }
    }


    public Channel getChannel() {
        return channel;
    }
}

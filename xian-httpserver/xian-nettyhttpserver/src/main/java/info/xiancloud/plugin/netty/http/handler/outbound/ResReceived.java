package info.xiancloud.plugin.netty.http.handler.outbound;

import info.xiancloud.plugin.netty.http.bean.ResponseWrapper;
import info.xiancloud.plugin.util.thread.MsgIdHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * ResReceived
 *
 * @author happyyangyuan
 */
public class ResReceived extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ResponseWrapper response = (ResponseWrapper) msg;
        MsgIdHolder.set(response.getRequest().getMsgId());
        /*LOG.debug("<<<<<<<<<<<<<  收到业务层的响应:" + msg);*/
        super.write(ctx, msg, promise);
    }
}

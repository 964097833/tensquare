package com.tensquare.notice.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class NettyServer {

    public void start(int port) {
        System.out.println("准备启动netty。。。");
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        NioEventLoopGroup boos = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        serverBootstrap.group(boos,worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        //请求消息解码器
                        ch.pipeline().addLast(new HttpServerCodec());
                        // 将多个消息转换为单一的request或者response对象
                        ch.pipeline().addLast(new HttpObjectAggregator(65536));
                        //处理WebSocket的消息事件
                        ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));

                        MyWebSocketHandler myWebSocketHandler = new MyWebSocketHandler();
                        ch.pipeline().addLast(myWebSocketHandler);
                    }
                }).bind(port);
    }

}

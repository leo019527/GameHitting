package com.example.dong.gamehit;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by 李凌耀 on 2017/7/15.
 */

public class BluetoothUtil {
    private static final boolean D = true;

    // 服务名 SDP
    private static final String SERVICE_NAME = "Bluetooth";
    // uuid SDP
    private static final UUID SERVICE_UUID = UUID.fromString("245dc87c-683d-11e7-907b-a6006ad3dba0");
    // 蓝牙适配器
    private final BluetoothAdapter mAdapter;
    private Handler mHandler;
    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;
    private static BluetoothUtil mBluetoothUtil;
    private BluetoothDevice mConnectedBluetoothDevice;
    //常数，指示当前的连接状态
    public static final int STATE_NONE = 0;       // 当前没有可用的连接
    public static final int STATE_LISTEN = 1;     // 现在侦听传入的连接
    public static final int STATE_CONNECTING = 2; // 现在开始连接
    public static final int STATE_CONNECTED = 3;  // 现在连接到远程设备
    public static final int STATAE_CONNECT_FAILURE = 4; //连接失败

    public static final int MESSAGE_DISCONNECTED = 5;
    public static final int STATE_CHANGES = 6;
    public static final String DEVICE_NAME = "device_name";
    public static final int MESSAGE_READ = 7;
    public static final int MESSAGE_WRITE= 8;
    public static final String READ_MSG = "read_msg";

    private BluetoothUtil(Context context) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
    }

    public static BluetoothUtil getInstance(Context c){
        if(null == mBluetoothUtil){
            mBluetoothUtil = new BluetoothUtil(c);
        }
        return mBluetoothUtil;
    }
    public void registerHandler(Handler handler){
        mHandler = handler;
    }

    public void unregisterHandler(){
        mHandler = null;
    }
    /*
     * 设置当前状态的聊天连接
     */
    private synchronized void setState(int state) {
        mState = state;
        mHandler.obtainMessage(STATE_CHANGES, state, -1).sendToTarget();
    }

    /*
     * 返回当前的连接状态。
     */
    public synchronized int getState() {
        return mState;
    }

    public BluetoothDevice getConnectedDevice(){
        return mConnectedBluetoothDevice;
    }
    /*
     * 开始服务器模式。
     */
    public synchronized void startListen() {
        // 取消任何线程正在运行的连接
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
        // 启动线程来监听一个bluetoothserversocket
        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }
        setState(STATE_LISTEN);
    }

    /*
     * 开始connectthread启动连接到远程设备。
     */
    public synchronized void connect(BluetoothDevice device) {
        // 取消任何线程试图建立连接
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        }
        // 取消任何线程正在运行的连接
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
        //启动线程连接到远程设备
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    /**
     * 开始ConnectedThread开始管理一个蓝牙连接,传输、接收数据.
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        //取消任何线程正在运行的连接
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
        // 启动线程管理连接和传输
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();
        //把连接设备的名字传到 ui Activity
        mConnectedBluetoothDevice =  device;
        Message msg = mHandler.obtainMessage(STATE_CONNECTED);
        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_NAME, device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        setState(STATE_CONNECTED);
    }

    /**
     * 停止所有的线程
     */
    public synchronized void disconnect() {
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
        if (mAcceptThread != null) {mAcceptThread.cancel(); mAcceptThread = null;}
        setState(STATE_NONE);
    }

    /*
     * 写入线程
     */
    public void write(byte[] out) {
        //创建临时对象
        ConnectedThread r;
        // 同步副本的connectedthread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // 执行写同步
        r.write(out);
    }

    /**
     * 发送链接失败的消息
     */
    private void connectionFailed() {
        // 发送失败的信息带回活动
        Message msg = mHandler.obtainMessage(STATAE_CONNECT_FAILURE);
        mHandler.sendMessage(msg);
        mConnectedBluetoothDevice = null;
        setState(STATE_NONE);
    }

    /**
     * 发送链接断开的消息
     */
    private void connectionLost() {
        // 发送失败的信息带回Activity
        Message msg = mHandler.obtainMessage(MESSAGE_DISCONNECTED);
        mHandler.sendMessage(msg);
        mConnectedBluetoothDevice = null;
        setState(STATE_NONE);
    }

    /**
     *  服务器线程 侦听传入的连接。
     *  它运行直到连接被接受（或取消）。
     */
    private class AcceptThread extends Thread {
        // 本地服务器套接字
        private final BluetoothServerSocket mServerSocket;
        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            // 创建一个新的侦听服务器套接字
            try {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(
                        SERVICE_NAME, SERVICE_UUID);
                //tmp = mAdapter.listenUsingInsecureRfcommWithServiceRecord(SERVICE_NAME, SERVICE_UUID);
            } catch (IOException e) {
            }
            mServerSocket = tmp;
        }

        public void run() {
            setName("AcceptThread");
            BluetoothSocket socket = null;
            // 循环，直到连接成功
            while (mState != STATE_CONNECTED) {
                try {
                    // 这是一个阻塞调用 返回成功的连接
                    socket = mServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // 如果连接被接受
                if (socket != null) {
                    synchronized (BluetoothUtil.this) {
                        switch (mState) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // 正常情况。启动ConnectedThread。
                                connected(socket, socket.getRemoteDevice());
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // 没有准备或已连接。新连接终止。
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                }
                                break;
                        }
                    }
                }
            }
        }

        public void cancel() {
            try {
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 客户端线程用来连接设备
     */
    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            // 得到一个bluetoothsocket
            try {
                mmSocket = device.createRfcommSocketToServiceRecord
                        (SERVICE_UUID);
            } catch (IOException e) {
                mmSocket = null;
            }
        }

        public void run() {
            try {
                // socket 连接,该调用会阻塞，直到连接成功或失败
                mmSocket.connect();
            } catch (IOException e) {
                connectionFailed();
                try {//关闭这个socket
                    mmSocket.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                return;
            }
            // 启动连接线程
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 传输数据线程
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            // 获得bluetoothsocket输入输出流
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            // 监听输入流
            while (true) {
                try {
                    byte[] buffer = new byte[1024];
                    // 读取输入流
                    int bytes = mmInStream.read(buffer);
                    // 发送获得的字节的ui activity
                    Message msg = mHandler.obtainMessage(MESSAGE_READ);
                    Bundle bundle = new Bundle();
                    bundle.putByteArray(READ_MSG, buffer);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    connectionLost();
                    break;
                }
            }
        }
        /**
         * 向外发送。
         * @param buffer  发送的数据
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
                // 分享发送的信息到Activity
                mHandler.obtainMessage(MESSAGE_WRITE, -1, -1, buffer)
                        .sendToTarget();
            } catch (IOException e) {
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }
}


package com.mubaracktahir.jpos.demo.connection;

import com.mubaracktahir.jpos.demo.Utils;
import com.mubaracktahir.jpos.demo.iso8583.ISO8583Packager;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;


@Service
public class ConnectionHelper {
    public final static int SIZE = 2048;

    private Socket clientSocket;

    public void startServer(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);
            while (true) {
                clientSocket = serverSocket.accept();
                byte[] isoRequest = handleClientRequest(clientSocket);
                ISOMsg isoResponse = getIsoResponse(isoRequest);
                assert isoResponse != null;
                respond(isoResponse.pack());
            }
        } catch (ISOException | IOException e) {
            e.printStackTrace();
        }

    }

    void log(ISOMsg isoMsg) {

        System.out.println("----ISO MESSAGE-----");
        try {
            System.out.println("  MTI : " + isoMsg.getMTI());
            for (int i = 1; i <= isoMsg.getMaxField(); i++) {
                if (isoMsg.hasField(i)) {
                    System.out.println("    Field-" + i + " : " + isoMsg.getString(i));
                }
            }
        } catch (ISOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("--------------------");
        }
    }

    private ISOMsg getIsoResponse(byte[] isoRequest) {
        String hex = Utils.bytesToHexString(isoRequest);
        String finalHex = hex.substring(4, hex.length());
        ISOMsg isoMsg = new ISOMsg();
        try {
            isoMsg.setPackager(new ISO8583Packager());
            isoMsg.unpack(Utils.hexStringToBytes(finalHex));
            System.out.println("-----------------REQUEST----------------");
            log(isoMsg);
            isoMsg.set(0, "0810");
            setRandomValue(isoMsg, 58);
            isoMsg.set(39, "00");
            System.out.println();
            System.out.println();
            System.out.println("-----------------RESPONSE----------------");
            log(isoMsg);
            return isoMsg;
        } catch (ISOException isoException) {
            isoException.printStackTrace();
        }
        return null;
    }

    private void setRandomValue(ISOMsg isoMsg, int fieldNumber) {
        Random random = new Random();
        String randomValue = String.format("%04d", random.nextInt(10000));
        isoMsg.set(fieldNumber, randomValue);
    }

    private byte[] handleClientRequest(Socket clientSocket) {
        try {
            InputStream inputStream = clientSocket.getInputStream();
            byte[] isoMessageBytes = new byte[SIZE];
            int bytesRead = inputStream.read(isoMessageBytes);
            byte[] isoRequest = new byte[bytesRead];
            System.arraycopy(isoMessageBytes, 0, isoRequest, 0, bytesRead);
            return isoRequest;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void respond(byte[] isoResponse) {
        try (OutputStream outputStream = clientSocket.getOutputStream()) {
            outputStream.write(isoResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

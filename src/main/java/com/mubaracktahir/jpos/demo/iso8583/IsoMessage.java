package com.mubaracktahir.jpos.demo.iso8583;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;

public class IsoMessage extends ISOMsg {
    @Override
    public byte[] pack() throws ISOException {
        String hexMessage = ISOUtil.byte2hex(super.pack());
        String messageAndLength = (ISOUtil.padleft(
                Integer.toString(hexMessage.length() / 2, 16),
                4,
                '0'
        ) + hexMessage).toUpperCase();
        System.out.println("RESPONSE====> "+messageAndLength);
        return ISOUtil.hex2byte(messageAndLength);
    }

    @Override
    public int unpack(byte[] b) throws ISOException {
        String hex = ISOUtil.byte2hex(b);
        String finalHex = hex.substring(4, hex.length());
        System.out.println("REQUEST====> "+hex);
        return super.unpack(ISOUtil.hex2byte(finalHex));
    }
}

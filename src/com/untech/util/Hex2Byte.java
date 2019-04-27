package com.untech.util;

public class Hex2Byte
{

    private Hex2Byte()
    {
    }

    public static String byte2Hex(byte abyte0[])
    {
        StringBuffer stringbuffer = new StringBuffer();
        
        for(int i = 0; i < abyte0.length; i++)
        {
            stringbuffer.append(a[abyte0[i] >>> 4 & 15]);
            stringbuffer.append(a[abyte0[i] & 15]);
        }
        return new String(stringbuffer);
    }

    public static byte[] hex2Byte(String s) throws Exception 
    {
        int i = s.length() / 2;
        byte abyte0[] = new byte[i];
        for(int j = 0; j < i; j++)
        {
            int k = a(s.charAt(j * 2));
            int l = a(s.charAt(j * 2 + 1));
            abyte0[j] = (byte)(k << 4 | l);
        }

        return abyte0;
    }

    private static int a(char c) throws Exception
    {
        if(c >= '0' && c <= '9')
            return c - 48;
        if(c >= 'a' && c <= 'f')
            return (c - 97) + 10;
        if(c >= 'A' && c <= 'F')
            return (c - 65) + 10;
        else {
        	throw new Exception("data is wrong!");
        }
            
    }

    private static final String a[] = {
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", 
        "b", "c", "d", "e", "f"
    };

}

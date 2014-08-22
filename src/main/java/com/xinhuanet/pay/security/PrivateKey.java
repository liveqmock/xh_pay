package com.xinhuanet.pay.security;

import java.io.*;

/**
 * User: 李野
 * @version 1.0
 */
public class PrivateKey {

    private String key = "";

    public String getKey() {
        return key;
    }

    public boolean buildKey(String merId,String MerKeyFile) {
        FileInputStream localFileInputStream;
        BufferedReader localBufferedReader;

        try {
            localFileInputStream = new FileInputStream(MerKeyFile);
            localBufferedReader = new BufferedReader(new InputStreamReader(localFileInputStream));
        } catch (FileNotFoundException localFileNotFoundException) {
            return false;
        }

        String str1;

        try {
            str1 = localBufferedReader.readLine();

            if ((str1.compareTo("[SecureLink]") != 0) && (str1.compareTo("[NetPayClient]") != 0)){
                return false;
            }
            str1 = localBufferedReader.readLine();
            int i = str1.indexOf("=");
            str1 = str1.substring(i + 1, str1.length());

            if (str1.compareTo(merId) != 0) {
                return false;
            }
            str1 = localBufferedReader.readLine();
            int j = str1.indexOf("=");
            str1 = str1.substring(j + 1, str1.length());
        } catch (IOException e) {
            return false;
        }
        key = str1;
        return true;
    }
}

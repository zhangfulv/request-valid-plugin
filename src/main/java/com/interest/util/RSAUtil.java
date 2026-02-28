package com.interest.util;




import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;

/**
 * @ClassName RSAEncrypt 非对称RSA加密
 * @Author Mr.zf, link:282734967@qq.com
 * @Date 2022/8/3 17:51
 */
public class RSAUtil {
    private final String kpStr = "rO0ABXNyABVqYXZhLnNlY3VyaXR5LktleVBhaXKXAww60s0SkwIAAkwACnByaXZhdGVLZXl0ABpMamF2YS9zZWN1cml0eS9Qcml2YXRlS2V5O0wACXB1YmxpY0tleXQAGUxqYXZhL3NlY3VyaXR5L1B1YmxpY0tleTt4cHNyABRqYXZhLnNlY3VyaXR5LktleVJlcL35T7OImqVDAgAETAAJYWxnb3JpdGhtdAASTGphdmEvbGFuZy9TdHJpbmc7WwAHZW5jb2RlZHQAAltCTAAGZm9ybWF0cQB+AAVMAAR0eXBldAAbTGphdmEvc2VjdXJpdHkvS2V5UmVwJFR5cGU7eHB0AANSU0F1cgACW0Ks8xf4BghU4AIAAHhwAAACfDCCAngCAQAwDQYJKoZIhvcNAQEBBQAEggJiMIICXgIBAAKBgQDNITp//CLrnCVMQwwVJa69/bQ569g6El9HTtNseBE6nnS6J9mmpiLYIvgEOvRpGd1+hdGV+RQnvGSm6/EkziYDTdzeN9mwj8XK/mPeKoWA/uETyvRDoxxbUqi+Et/JH9Y10TFL3YYAaoDyIgWucbFLgMCJAvvY2f5DEYiB/1YPlwIDAQABAoGAOFIpR76or9wdeYTnKl9ATTX9Z/HTWo50zpVcA2osANZE4l/SqKjw5DslsbOmK71ITVbcprrOx+I4GISrnxli4gNfMfuQr7O+3jQo87FJwj/N3m852S4ibWxWbGjZLmyPfRr8fsAkyqVMfiuBw2g4rbI7WZhVuYyCO6n4zcJl6uECQQDwvFWw1WDMs5ChCYCL/Wes9tAWK5AvIvsv1+WN20m3aEfxCyGvY5ayOWXExeJiwGujoYERv/DZLhIzUruzGdapAkEA2iLuwGYePvreN1m952jyzLEuuFYyVE97wP5N9ZZOVB1S7+sR18/My44X2G1eBvj8KZcqfWhho/FWVsNAWRncPwJBAK0AG3CmFxdkX6ZqxrdTl0LkZ+vWyJonQG5Eb9I+6b3lMfYWctGw9uYDe5AqSSOAdpo0ASY5UqMwiaHabcsq4WkCQQCO4Vi/X2QqXooVeUvBeuWmm9v9VEBtJw7pb+lClqxBvl9n1PKACJWdMzp9Qc3YBViuKVzkfe2Ow/KIrCXG9wBjAkEA1mzT0EdzAzQGzWe7jNzWgVsOthpzepinTWlf1m37xVputCgFI+FEVGj5/dMBYKcyA6Ts0GZoqIAM4fGkVtzez3QABlBLQ1MjOH5yABlqYXZhLnNlY3VyaXR5LktleVJlcCRUeXBlAAAAAAAAAAASAAB4cgAOamF2YS5sYW5nLkVudW0AAAAAAAAAABIAAHhwdAAHUFJJVkFURXNxAH4ABHEAfgAJdXEAfgAKAAAAojCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAzSE6f/wi65wlTEMMFSWuvf20OevYOhJfR07TbHgROp50uifZpqYi2CL4BDr0aRndfoXRlfkUJ7xkpuvxJM4mA03c3jfZsI/Fyv5j3iqFgP7hE8r0Q6McW1KovhLfyR/WNdExS92GAGqA8iIFrnGxS4DAiQL72Nn+QxGIgf9WD5cCAwEAAXQABVguNTA5fnEAfgANdAAGUFVCTElD";
    public final String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDNITp//CLrnCVMQwwVJa69/bQ569g6El9HTtNseBE6nnS6J9mmpiLYIvgEOvRpGd1+hdGV+RQnvGSm6/EkziYDTdzeN9mwj8XK/mPeKoWA/uETyvRDoxxbUqi+Et/JH9Y10TFL3YYAaoDyIgWucbFLgMCJAvvY2f5DEYiB/1YPlwIDAQAB";
    // 临时使用
    private String RSAKeyStore = "d:/RSAKey.txt";
    private KeyPair pair;
    /**
    * desc: 不做懒加载
    */
    private static RSAUtil _INSTANCE = new RSAUtil();
    public static RSAUtil getInstance() {
        return _INSTANCE;
    }

    private RSAUtil() {
        this.pair = (KeyPair) byteToObject(Base64.decodeBase64(kpStr));

    }

    private Object byteToObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);
            obj = oi.readObject();
            bi.close();
            oi.close();
        } catch (Exception e) {
            throw new RuntimeException("byteToObject error!", e);
        }
        return obj;
    }

    public void saveKeyPair(KeyPair kp) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(RSAKeyStore);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(kp);
            oos.close();
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException("saveKeyPair error!", e);
        }
    }

    public String encode(String s) {
        return this.encrypt(s);
    }

    public String decode(String s) {
        return this.decrypt(s);
    }


    private String encrypt(String source) {
        byte[] data = source.getBytes();
        Cipher ci = null;
        try {
            ci = Cipher.getInstance("RSA");
            ci.init(Cipher.ENCRYPT_MODE, pair.getPublic());
            return Base64Util.encode(ci.doFinal(data));
        } catch (Exception e) {
            throw new RuntimeException("RSAUtil encrypt error！", e);
        }

    }


    private String decrypt(String source) {
        byte[] data = Base64.decodeBase64(source);
        Cipher ci = null;
        try {
            ci = Cipher.getInstance("RSA");
            ci.init(Cipher.DECRYPT_MODE, pair.getPrivate());
            return new String(ci.doFinal(data));
        } catch (Exception e) {
            throw new RuntimeException("RSAUtil decrypt error！", e);
        }
    }



    public static void main(String[] args) {
        System.out.println(RSAUtil.getInstance().encode("ZDA5YWVkNGItMWJiOS00MTYxLWI4NjYtN2JkYjZlYTI4NTk2"));
        System.out.println(RSAUtil.getInstance().encode("123456"));
        System.out.println(RSAUtil.getInstance().encode("123456"));
        System.out.println(RSAUtil.getInstance().pair.getPublic().toString());

        System.out.println(RSAUtil.getInstance().decode("KacbUgtYNYFgJecHXwrc4mWUDwydk1B0k4MTPpOl6jUb0cEknvNENhs+C86EnZuSLBFk87Npo0XQIXTjzDBeVTR5wyy1IhgsiuhSmKtil0BiJe6lkbAVgWc0khEM2hVb3h5d+zoIEhcyfVPrsRGLZ+cRdHnMM3e/PxG9mb6jsUE="));
        System.out.println(RSAUtil.getInstance().decode("gaPCzsQuN9LsNtzX3gF4EU6ZT4UNjKj5fw8aiZxZCcK5GfCb69WpwOyeQq/LDUhzkWkIRvI3T3eaZbBTSpjpX7B6LLgPFiWuhYY4sFBehJVJzhoeMUNTq/k8YALubvj+NGnwQIQHNbuj7FM+MIwOd+nLb7UjQEde682tzBzkVVk="));
        System.out.println(RSAUtil.getInstance().decode("jjX6bxg+719jzTZRod4CiiPFev1JDPTO2NP2xEdoHaS90yANbCnQdS65P1Nnw8znww81txZzTBVoMq8pTETGpKZz7ROK573g8yI0ApjtHqDazeqke7XU0IkocK9eFFF2cZjmD+PelRUPWSBIIijEFZMAwbUvNv+fC5ZrkFyDwz4="));
        System.out.println(RSAUtil.getInstance().decode("u/7zgEtO219G4JJTb+9FlX0fE30rjNAUvq84o0IK0wwXLENX/vwMSZ7oxVaTzRKgbB3tmuPi6YqS2pS5SGDl1EcZEGGlciHPAfK37u1lF+4YOt1AuspZdQLOCpvaoaYn07hLskpaNAokC3pJ+QUqynrryav1m7OmrnhTUhsnZxk="));
        System.out.println(RSAUtil.getInstance().decode("BO/Ekcv5OrPHHmM302zh7IVdKyf1sp+UgPXfahyk9bGV38+iebdo8Ng2rAXCawGOjhbrPjDYNwSN1hsrNLbi9NUFUeziQaOk1QjtzSrh5vGP6JPBtzT18bxRnb8Fyyj4WJEw8Vut51qSEkiwbdfp4NjzHpZsGCcQjmT55T9gVs0="));
        System.out.println(RSAUtil.getInstance().decode("qqQh+hnCN7aMgSqp3oW75ktRXz8hoA6dZIWuiDv/iVKUOp9G947VNfeiAoV7oSS+ZMxS8dbuD1E7YUbkuZdYcTNlhxfxVbcHtztjXRqU4rr2+kafZWpNlgjIY5HlGA5j5a7yFaqPZPV70irlp7yggr02LVQRepI98RAD3OZze6o="));
    }
}

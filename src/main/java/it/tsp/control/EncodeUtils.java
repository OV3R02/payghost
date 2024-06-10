package it.tsp.control;

import com.password4j.BcryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.Bcrypt;

public class EncodeUtils {

    public static String encode(String pwd) {
        BcryptFunction bc = BcryptFunction.getInstance(Bcrypt.B, 10);
        Hash hash = Password.hash(pwd).with(bc);
        return hash.getResult();
    }

    public static boolean verify(String plainPwd, String encodedPwd) {
        BcryptFunction bc = BcryptFunction.getInstance(Bcrypt.B, 10);
        return Password.check(plainPwd, encodedPwd).with(bc);
    }
}

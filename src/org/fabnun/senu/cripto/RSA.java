package org.fabnun.senu.cripto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;

public class RSA implements Serializable {

    int tamPrimo;
    BigInteger n, q, p;
    BigInteger totient;
    BigInteger e, d;

    public RSA(int tamPrimo) {
        this.tamPrimo = tamPrimo;
        generaPrimos();             //Genera p y q
        generaClaves();             //Genera e y d
    }

    public RSA() {
    }

    private BigInteger[] load(File f) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);
        BigInteger[] value = (BigInteger[]) ois.readObject();
        ois.close();
        fis.close();
        return value;
    }

    private void save(File f, BigInteger[] value) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(value);
        oos.close();
        fos.close();
    }

    public void loadPublicKey(File f) throws IOException, ClassNotFoundException {
        BigInteger[] values = load(f);
        n = values[0];
        d = values[1];
    }

    public void loadPrivateKey(File f) throws IOException, ClassNotFoundException {
        BigInteger[] values = load(f);
        n = values[0];
        e = values[1];
    }

    public void savePublicKey(File f) throws FileNotFoundException, IOException {
        save(f, new BigInteger[]{n, d});
    }

    public void savePrivateKey(File f) throws FileNotFoundException, IOException {
        save(f, new BigInteger[]{n, e});
    }

    private void generaPrimos() {
        p = new BigInteger(tamPrimo, 10, new Random());
        do {
            q = new BigInteger(tamPrimo, 10, new Random());
        } while (q.compareTo(p) == 0);
    }

    private void generaClaves() {
        // n = p * q
        n = p.multiply(q);
        // toltient = (p-1)*(q-1)
        totient = p.subtract(BigInteger.valueOf(1));
        totient = totient.multiply(q.subtract(BigInteger.valueOf(1)));
        // Elegimos un e coprimo de y menor que n
        do {
            e = new BigInteger(2 * tamPrimo, new Random());
        } while ((e.compareTo(totient) != -1)
                || (e.gcd(totient).compareTo(BigInteger.valueOf(1)) != 0));
        // d = e^1 mod totient
        d = e.modInverse(totient);
    }

    public BigInteger[] encripta(String mensaje) {
        int i;
        byte[] temp = new byte[1];
        byte[] digitos = mensaje.getBytes();
        BigInteger[] bigdigitos = new BigInteger[digitos.length];
        for (i = 0; i < bigdigitos.length; i++) {
            temp[0] = digitos[i];
            bigdigitos[i] = new BigInteger(temp);
        }
        BigInteger[] encriptado = new BigInteger[bigdigitos.length];
        for (i = 0; i < bigdigitos.length; i++) {
            encriptado[i] = bigdigitos[i].modPow(e, n);
        }
        return (encriptado);
    }

    public String desencripta(BigInteger[] encriptado) {
        BigInteger[] desencriptado = new BigInteger[encriptado.length];
        for (int i = 0; i < desencriptado.length; i++) {
            desencriptado[i] = encriptado[i].modPow(d, n);
        }
        char[] charArray = new char[desencriptado.length];

        for (int i = 0; i < charArray.length; i++) {
            charArray[i] = (char) (desencriptado[i].intValue());
        }
        return (new String(charArray));
    }

    public String desencripta(File f) throws IOException, ClassNotFoundException {
        return desencripta(load(f));
    }

    public void encripta(String mensaje, File f) throws FileNotFoundException, IOException {
        save(f, encripta(mensaje));
    }
    private static File fpub = new File("public.key");
    private static File fpri = new File("private.key");
    private static File encript = new File("encripted");

    private static void main1() throws FileNotFoundException, IOException {
        RSA rsa = new RSA(512);
        rsa.savePublicKey(fpub);
        rsa.savePrivateKey(fpri);
    }

    private static void main2() throws FileNotFoundException, IOException, ClassNotFoundException {
        RSA rsa = new RSA();
        rsa.loadPrivateKey(fpri);
        rsa.encripta("chapalapachalachapalapachalachapalapachalachapalapachala", encript);
    }

    private static void main3() throws IOException, ClassNotFoundException {
        RSA rsa = new RSA();
        rsa.loadPublicKey(fpub);
        System.out.println(rsa.desencripta(encript));
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
        main2();
    }
}

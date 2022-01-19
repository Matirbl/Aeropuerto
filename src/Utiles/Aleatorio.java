package Utiles;

import java.util.Random;

public class Aleatorio {
    public static double doubleAleatorio(int min, int max) {
        return Math.random() * (max - min) + min;
    }

    public static int intAleatorio(int min, int max) {
        return (int) doubleAleatorio(min, max + 1);
    }

    public static boolean booleanAleatorio() {
        Random r = new Random();
        return r.nextBoolean();
    }

    public static char charAleatorio() {
        char a;
        if (Math.random() >= 0.5) {
            // letras mayúsculas
            a = (char) intAleatorio(97, 123);
        } else {
            // letras minúsculas
            a = (char) intAleatorio(65, 91);
        }
        return a;
    }

    public static String stringAleatorio(int tam) {
        String res = "";
        for (int i = 0; i < tam; i++) {
            res = res + charAleatorio();
        }
        return res;
    }
}

package org.example.ordenacao;

import java.util.Arrays;

public class Ordenacao {

    public String ordena(int a, int b, int c, int d, int e) {
        int buffer = 0;

        do{
            if (a > b) {
                buffer = a;
                a = b;
                b = buffer;
            }

            if (b > c) {
                buffer = b;
                b = c;
                c = buffer;
            }
            if (c > d) {
                buffer = c;
                c = d;
                d = buffer;
            }
            if (d > e) {
                buffer = d;
                d = e;
                e = buffer;
            }
        }while(a>b || b>c || c>d || d>e);

        return Arrays.toString(new int[]{a, b, c, d, e});
    }
}

package org.example.Service;

public class LucroService {
    private static double lucroTotalDoDia = 0.0;
    private static final double PORCENTAGEM_LUCRO = 0.3; // 30%

    public static void registrarLucro(double valorVenda) {
        double lucro = valorVenda * PORCENTAGEM_LUCRO;
        lucroTotalDoDia += lucro;
    }

    public static double getLucroTotalDoDia() {
        return lucroTotalDoDia;
    }

    public static void resetarLucro() {
        lucroTotalDoDia = 0.0;
    }
}

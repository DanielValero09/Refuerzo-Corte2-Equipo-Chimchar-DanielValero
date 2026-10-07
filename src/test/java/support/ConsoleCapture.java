package support;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public final class ConsoleCapture {
    private ConsoleCapture() {
    }

    public static String ejecutar(Runnable accion) {
        PrintStream original = System.out;
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        try (PrintStream captura = new PrintStream(salida, true, StandardCharsets.UTF_8)) {
            System.setOut(captura);
            accion.run();
        } finally {
            System.setOut(original);
        }
        return salida.toString(StandardCharsets.UTF_8);
    }
}

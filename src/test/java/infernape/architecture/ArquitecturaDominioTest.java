package infernape.architecture;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ArquitecturaDominioTest {
    @Test
    void dominioSoloImportaJDKYDominioSinFrameworkRedPersistenciaNiMockito() throws IOException {
        // Arrange
        Path dominio = Path.of("src/main/java/infernape/domain");
        // Act
        List<String> imports = imports(dominio);
        // Assert
        assertFalse(imports.isEmpty());
        assertTrue(imports.stream().allMatch(line -> line.startsWith("import java.") || line.startsWith("import infernape.domain.")));
        assertTrue(imports.stream().noneMatch(line -> line.matches(".*(springframework|persistence|java[.]net|java[.]sql|Mockito|mockito|okhttp|httpclient).*")));
    }

    @Test
    void aplicacionNoImportaAdaptadoresConcretosNiPatronesDeInfraestructura() throws IOException {
        // Arrange
        Path aplicacion = Path.of("src/main/java/infernape/application");
        // Act
        List<String> imports = imports(aplicacion);
        // Assert
        assertTrue(imports.stream().allMatch(line -> line.startsWith("import java.") || line.startsWith("import infernape.domain.")));
    }

    private List<String> imports(Path directory) throws IOException {
        try (Stream<Path> archivos = Files.walk(directory)) {
            return archivos.filter(path -> path.toString().endsWith(".java"))
                .flatMap(path -> leer(path).stream()).filter(line -> line.startsWith("import ")).toList();
        }
    }

    private List<String> leer(Path archivo) {
        try { return Files.readAllLines(archivo); }
        catch (IOException error) { throw new java.io.UncheckedIOException(error); }
    }
}

import repository.EstacionamentoRepository;
import service.EstacionamentoService;
import ui.ConsoleUI;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        EstacionamentoRepository repository = new EstacionamentoRepository();
        EstacionamentoService service = new EstacionamentoService(20, repository);
        ConsoleUI ui = new ConsoleUI(service);
        ui.iniciar();
    }
}

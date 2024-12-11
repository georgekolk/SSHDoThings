import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.Signal;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static java.lang.System.*;

public class CustomCommand implements Command, Runnable{
    private InputStream in;
    private OutputStream out;
    private OutputStream err;
    private Thread thread;

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8), true)) {

            writer.println("Вітаю! Введіть команди (/move, /chat, /who, /exit для завершення):");

            StringBuilder currentLine = new StringBuilder();

            int charCode;
            while ((charCode = reader.read()) != -1) { // Зчитування посимвольно
                char character = (char) charCode;
                // Якщо натиснуто Enter
                //В Java при роботі з BufferedReader в середовищах, де використовується стандартний ввід, натискання Enter може бути представлене як \r\n (для Windows) або \n (для Unix/Linux/Mac).

                if (character == '\n' || character == '\r') {
                    writer.print(character); // Виводимо введений символ
                    writer.flush(); // Забезпечуємо негайний вивід
                    currentLine.append(character);

                    String line = currentLine.toString().trim();

                    if (line.equalsIgnoreCase("/exit")) {
                        writer.println("Завершення сесії...");
                        break;
                    }

                    switch (line) {
                        case "/move":
                            writer.println("Виконую команду /move...");
                            break;
                        case "/chat":
                            writer.println("Виконую команду /chat...");
                            break;
                        case "/who":
                            writer.println("Виконую команду /who...");
                            break;
                        default:
                            writer.println("Невідома команда: " + line);
                            break;
                    }

                    currentLine.setLength(0); // Очищення буфера для наступної строки
                } else {
                    writer.print(character); // Виводимо введений символ
                    writer.flush(); // Забезпечуємо негайний вивід
                    currentLine.append(character); // Додаємо символ до поточної строки
                }
            }
        } catch (IOException e) {
            try {
                err.write(("Помилка: " + e.getMessage() + "\n").getBytes(StandardCharsets.UTF_8));
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public void setExitCallback(ExitCallback callback) {
        // Порожня реалізація, не потрібна для нашого прикладу
    }

    @Override
    public void setErrorStream(OutputStream err) {
        this.err = err;
    }

    @Override
    public void setInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public void setOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void start(ChannelSession channelSession, Environment environment) throws IOException {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void destroy(ChannelSession channelSession) throws Exception {
        thread.interrupt();
    }
}

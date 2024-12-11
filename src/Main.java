import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
//import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.shell.ProcessShellFactory;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Створення сервера на порті 2222
        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setPort(2222);

        // Генерація ключів для сервера

        //sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Paths.get("hostkey.pem")));

        // Аутентифікація за паролем
        sshd.setPasswordAuthenticator((username, password, session) ->
                "admin".equals(username) && "password".equals(password));

        // Дозволяємо виконання команд через оболонку
        //sshd.setShellFactory(new ProcessShellFactory("/bin/sh", "-i"));
        //sshd.setCommandFactory(new SimpleCommandFactory());
        //sshd.setShellFactory(() -> new CustomShell());
        sshd.setShellFactory(new SimpleShellFactory());
        // Запуск сервера
        sshd.start();
        System.out.println("SSH сервер запущено на порті 2222...");

        synchronized (sshd) {
            sshd.wait();
        }
    }
}

import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.shell.ShellFactory;

import java.io.IOException;

public class SimpleShellFactory implements ShellFactory {
    @Override
    public Command createShell(ChannelSession channelSession) throws IOException {
        return new CustomCommand();
    }
}

package zaglaz;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import zaglaz.command.commands.Greet;

public class Horus {
  private static final String BOT_TOKEN = "foobar"; //replace value with bot token
  public static DiscordClient client = DiscordClient.create(BOT_TOKEN);

    public static void main(String[] args) {

      // Logging in
      GatewayDiscordClient botGateway = client.login().block();

      // Creates, registers, and listens for greet command
      Greet greetCommand = new Greet(botGateway);
      greetCommand.buildCommand();
      greetCommand.registerGreetCommand(12345L); //replace argument with your guildID
      greetCommand.listenForGreet();

      //Ensures bot doesn't disconnect
      botGateway.onDisconnect().block();
  }
}

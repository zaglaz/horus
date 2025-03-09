package zaglaz;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import zaglaz.command.commands.Greet;

public class Horus {
  private static final String BOT_TOKEN = ""; //replace with bot token
  private static final long GUILD_ID = 1L; //replace with your guildID

  public static DiscordClient client = DiscordClient.create(BOT_TOKEN);

    public static void main(String[] args) {

      // Logging in
      GatewayDiscordClient botGateway = client.login().block();

      // Creates, registers, and listens for greet command
      Greet greetCommand = new Greet(botGateway);
      greetCommand.buildCommand();
      greetCommand.registerGuildGreetCommand(GUILD_ID);
      greetCommand.listenForGreet();

      //Ensures bot doesn't disconnect
      botGateway.onDisconnect().block();
  }
}

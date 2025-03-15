package zaglaz;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import zaglaz.command.commands.Greet;
import zaglaz.command.commands.RandomNumber;

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
      greetCommand.makeGuildCommand(GUILD_ID);
      greetCommand.listenForGreet();
      
      //Does the same for randomnum command
      RandomNumber rand = new RandomNumber(botGateway);
      rand.buildCommand();
      rand.makeGuildCommand(GUILD_ID);
      rand.listenForRandomNumber();

      //Ensures bot doesn't disconnect
      botGateway.onDisconnect().block();
  }
}

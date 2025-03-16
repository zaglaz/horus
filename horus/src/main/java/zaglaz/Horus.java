package zaglaz;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import zaglaz.command.commands.Greet;
import zaglaz.command.commands.RandomNumber;
import zaglaz.util.Autorole;

public class Horus {
  private static final String BOT_TOKEN = ""; //replace with bot token
  private static final long GUILD_ID = 1L; //replace with your guildID
  private static final PermissionSet autorolePermissions = PermissionSet.of( //replace with any explicit perms you'd like the autorole to have
    Permission.SEND_MESSAGES, 
    Permission.ATTACH_FILES, 
    Permission.CHANGE_NICKNAME
  );

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

      //Enables Autorole
      Autorole auto = new Autorole(botGateway, "Newcomer", new int[] {0, 255, 0}, false, true, autorolePermissions);
      auto.giveAutoRole();

      //Ensures bot doesn't disconnect
      botGateway.onDisconnect().block();
  }
}

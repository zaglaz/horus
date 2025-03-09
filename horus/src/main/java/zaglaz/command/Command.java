package zaglaz.command;

import discord4j.core.GatewayDiscordClient;
import discord4j.discordjson.json.ApplicationCommandRequest;

public abstract class Command {
    public final GatewayDiscordClient client;
    private final long applicationID; 

    public Command(GatewayDiscordClient client) {
        this.client = client;
        this.applicationID = client.getRestClient().getApplicationId().block();
    }

    /*
     * Our primary method for setting up commands in concrete subclasses
     */
    public abstract ApplicationCommandRequest buildCommand();

    /*
     * Command subclasses can invoke either global or guild command methods 
     * in their own registration methods
     */
    public void makeGuildCommand(long guildID) {
        client.getRestClient().getApplicationService()
            .createGuildApplicationCommand(this.applicationID, guildID, buildCommand()).subscribe();
    }

    public void makeGlobalCommand() {
        client.getRestClient().getApplicationService()
            .createGlobalApplicationCommand(this.applicationID, buildCommand()).subscribe();
    }
}

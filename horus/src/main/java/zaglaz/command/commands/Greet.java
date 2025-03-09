package zaglaz.command.commands;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;
import zaglaz.command.Command;

public class Greet extends Command {

    public Greet(GatewayDiscordClient client) {
        super(client);
    }

    @Override
    public ApplicationCommandRequest buildCommand() {
        // Command setup
        ApplicationCommandRequest greetRequest = ApplicationCommandRequest.builder()
            .name("greet")
            .description("Greets you")
            .build();
        return greetRequest;
    }

    public void registerGuildGreetCommand(long guildID) {
        makeGuildCommand(guildID);
    }

    public void registerGlobalGreetCommand() {
        makeGlobalCommand();
    }

    public void listenForGreet() {
        /*
         * Post-registration, we look to see if the user invokes the command, 
         * after which the application responds
         */
        client.on(ChatInputInteractionEvent.class)
            .filter(event -> event.getCommandName().equals("greet"))
            .flatMap(event -> event.reply("Hi, " + event.getInteraction().getUser().getUsername() + "! 😃")).subscribe();
    }
}

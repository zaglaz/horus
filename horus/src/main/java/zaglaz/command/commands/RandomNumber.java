package zaglaz.command.commands;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import reactor.core.publisher.Mono;

import zaglaz.command.Command;

public class RandomNumber extends Command {

    public RandomNumber(GatewayDiscordClient client) {
        super(client);
    }

    @Override
    public ApplicationCommandRequest buildCommand() {
        // Command setup
        ApplicationCommandRequest randomNumRequest = ApplicationCommandRequest.builder()
            .name("randomnum")
            .description("Generated a random number from a given range.")
            .addOption(ApplicationCommandOptionData.builder()
                .name("lower_bound")
                .description("lower bound of the range (inclusive)")
                .type(ApplicationCommandOption.Type.INTEGER.getValue())
                .required(true)
                .build()
            )
            .addOption(ApplicationCommandOptionData.builder()
                .name("upper_bound")
                .description("upper bound of the range (inclusive)")
                .type(ApplicationCommandOption.Type.INTEGER.getValue())
                .required(true)
                .build()
            )
            .build();
        return randomNumRequest;
    } 

    public void listenForRandomNumber() {
        client.on(ChatInputInteractionEvent.class)
            .filter(event -> event.getCommandName().equals("randomnum"))
            .flatMap(event -> {
                int randomNumber = 0;
                /*
                 * Recieving and storing the values of both 
                 * our slash command options
                 */
                Long min = event.getOption("lower_bound")
                    .flatMap(ApplicationCommandInteractionOption::getValue)
                    .map(ApplicationCommandInteractionOptionValue::asLong)
                    .orElse(null);

                Long max = event.getOption("upper_bound")
                    .flatMap(ApplicationCommandInteractionOption::getValue)
                    .map(ApplicationCommandInteractionOptionValue::asLong)
                    .orElse(null);
                // Checking if min is truly a min and for null values
                if (max != null && min != null) {
                    if (max > min ) {
                        // Inclusive range from lower->upper
                        randomNumber = (int)(Math.random() * (max - min + 1) + min);
                        return event.reply(String.valueOf(randomNumber));
                    }
                }
                return event.reply("Error: invalid input");
            })
            .onErrorResume(e -> Mono.empty())
            .subscribe();
    }
}

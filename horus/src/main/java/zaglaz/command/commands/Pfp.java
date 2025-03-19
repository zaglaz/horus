package zaglaz.command.commands;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import reactor.core.publisher.Mono;
import zaglaz.command.Command;

public class Pfp extends Command {

    public Pfp(GatewayDiscordClient client) {
        super(client);
    }

    @Override
    public ApplicationCommandRequest buildCommand() {
            // Command setup
            ApplicationCommandRequest PfpRequest = ApplicationCommandRequest.builder()
                .name("pfp")
                .description("get a user's pfp")
                .addOption(ApplicationCommandOptionData.builder()
                    .name("user")
                    .description("the user whose pfp you want to get")
                    .type(ApplicationCommandOption.Type.USER.getValue())
                    .build()
                )
                .build();
        return PfpRequest;
    } 

    public void listenForPfp() {
        client.on(ChatInputInteractionEvent.class)
            .filter(event -> event.getCommandName().equals("pfp"))
            .flatMap(event -> {
                /*
                 * Receiving and storing the id of the User argument
                 */
                Snowflake userId = event.getOption("user")
                    .flatMap(ApplicationCommandInteractionOption::getValue)
                    .map(ApplicationCommandInteractionOptionValue::asSnowflake)
                    .orElse(null);
                    return client.getUserById(userId)
                        /*
                         * We look for that user's pfp. If they don't have one, we get the placeholder pfp URL.
                         */
                        .flatMap(user -> {
                            String pfpUrl = user.getAvatarUrl() != null 
                            ? user.getAvatarUrl() 
                            : user.getDefaultAvatarUrl();
                            // Using the URL, we build an embed for the bot reply
                            EmbedCreateSpec pfpEmbed = EmbedCreateSpec.builder()
                                .title(user.getUsername() + "'s Profile Picture")
                                .image(pfpUrl)
                                .build();
                            // Bot embed reply visible only to the user who called the command 
                            return event.reply(InteractionApplicationCommandCallbackSpec.builder()
                                .addEmbed(pfpEmbed)
                                .ephemeral(true)
                                .build());
                        });
            })
            .onErrorResume(e -> Mono.empty())
            .subscribe();
    }
}

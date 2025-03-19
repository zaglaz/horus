package zaglaz.util;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.guild.MemberJoinEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Role;
import discord4j.core.spec.RoleCreateSpec;
import discord4j.rest.util.Color;
import discord4j.rest.util.PermissionSet;
import reactor.core.publisher.Mono;

public class Autorole {
    public final GatewayDiscordClient client;
    public String roleName;
    public Color roleColor;
    public boolean isHoisted;
    public boolean isMentionable;
    public PermissionSet rolePermissions;

    public Autorole(GatewayDiscordClient client, String roleName, int[] rgb, boolean isHoisted, 
    boolean isMentionable, PermissionSet rolePermissions) {
        this.client = client;
        this.roleName = roleName;
        this.roleColor = Color.of(rgb[0], rgb[1], rgb[2]);
        this.isHoisted = isHoisted;
        this.isMentionable = isMentionable;
        this.rolePermissions = rolePermissions;
    }

    public void updatePermissions(PermissionSet newRolePermissions) {
        this.rolePermissions = (this.rolePermissions == null)
        ? newRolePermissions
        : this.rolePermissions.and(newRolePermissions);
    }        

    public Mono<Role> makeAutoRole(Guild guild) {
        return guild.createRole(RoleCreateSpec.builder()
            .name(roleName)
            .color(roleColor)
            .hoist(isHoisted)
            .mentionable(isMentionable)
            .permissions(rolePermissions)
            .build());
    }

    /* TODO - with .flatMap() and .filter() retrieve guild roles, see if we already have a role of the same name; 
    * if we don't call makeAutoRole() and add it, then assign it to the newly-joined user regardless/
    */
    public void giveAutoRole() {
        client.on(MemberJoinEvent.class)
            .flatMap(event -> event.getGuild())
            .onErrorResume(e -> Mono.empty())
            .subscribe();
    }
}

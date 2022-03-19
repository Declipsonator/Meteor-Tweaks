package me.declipsonator.meteortweaks.mixins.meteor;

import me.declipsonator.meteortweaks.utils.MixinReferences;
import me.declipsonator.meteortweaks.utils.TweaksUtil;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.misc.Notifier;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Notifier.class)
public class NotifierMixin extends Module {
    public NotifierMixin(Category category, String name, String description) {
        super(category, name, description);
    }

    @EventHandler
    private void packet(PacketEvent.Receive event) {
        if (!MixinReferences.joinLeaveMessages.get()) return;
        if(!(event.packet instanceof PlayerListS2CPacket)) return;
        for(PlayerListS2CPacket.Entry entry : ((PlayerListS2CPacket) event.packet).getEntries()) {

            if(MixinReferences.friendsJoinLeave.get() && Friends.get().get(TweaksUtil.uuidToAccount(entry.getProfile().getId()).getName()) != null) continue;

            //2 different methods for join and leave. Using the same results in null for one of the 2
            if(((PlayerListS2CPacket) event.packet).getAction() == PlayerListS2CPacket.Action.ADD_PLAYER && MixinReferences.joinOrLeave.get() == MixinReferences.joinLeave.Both || MixinReferences.joinOrLeave.get() == MixinReferences.joinLeave.Join) {
                ChatUtils.sendMsg("Notifier", Text.of(entry.getProfile().getName() + " joined the server."));
            } else if(((PlayerListS2CPacket) event.packet).getAction() == PlayerListS2CPacket.Action.REMOVE_PLAYER && MixinReferences.joinOrLeave.get() == MixinReferences.joinLeave.Both || MixinReferences.joinOrLeave.get() == MixinReferences.joinLeave.Leave) {
                ChatUtils.sendMsg("Notifier", Text.of(TweaksUtil.uuidToAccount(entry.getProfile().getId()).getName() + " left the server."));
            }
        }

    }

    @Override
    public String getInfoString() {
        MixinReferences.sgJoinLeave = settings.createGroup("Join/Leave Messages");

        MixinReferences.joinLeaveMessages = MixinReferences.sgJoinLeave.add(new BoolSetting.Builder()
                .name("join-leave-messages")
                .description("Whether or not to notify you of players joining or leaving the server.")
                .defaultValue(false)
                .build()
        );

        MixinReferences.joinOrLeave = MixinReferences.sgJoinLeave.add(new EnumSetting.Builder<MixinReferences.joinLeave>()
                .name("messages")
                .description("What messages to send.")
                .defaultValue(MixinReferences.joinLeave.Both)
                .build()
        );

        MixinReferences.friendsJoinLeave = MixinReferences.sgJoinLeave.add(new BoolSetting.Builder()
                .name("ignore-friends")
                .description("Ignore friends.")
                .defaultValue(false)
                .build()
        );

        return null;
    }
}

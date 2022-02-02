package me.declipsonator.meteortweaks.mixins;

import me.declipsonator.meteortweaks.modules.GUIMove;
import meteordevelopment.meteorclient.events.meteor.KeyEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.render.Freecam;
import meteordevelopment.meteorclient.utils.misc.input.Input;
import meteordevelopment.meteorclient.utils.misc.input.KeyAction;
import meteordevelopment.orbit.EventHandler;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(value = Freecam.class, remap = false)
public class FreecamMixin {


    @Shadow
    private boolean forward, backward, right, left, up, down;

    @EventHandler
    public void onKey(KeyEvent event) {
        if (Input.isKeyPressed(GLFW.GLFW_KEY_F3)) return;

        // TODO: This is very bad but you all can cope :cope:
        GUIMove guiMove = Modules.get().get(GUIMove.class);
        if (mc.currentScreen != null && !guiMove.isActive()) return;
        if (mc.currentScreen != null && guiMove.isActive() && guiMove.skip()) return;

        boolean cancel = true;

        if (mc.options.keyForward.matchesKey(event.key, 0) || mc.options.keyForward.matchesMouse(event.key)) {
            forward = event.action != KeyAction.Release;
            mc.options.keyForward.setPressed(false);
        }
        else if (mc.options.keyBack.matchesKey(event.key, 0) || mc.options.keyBack.matchesMouse(event.key)) {
            backward = event.action != KeyAction.Release;
            mc.options.keyBack.setPressed(false);
        }
        else if (mc.options.keyRight.matchesKey(event.key, 0) || mc.options.keyRight.matchesMouse(event.key)) {
            right = event.action != KeyAction.Release;
            mc.options.keyRight.setPressed(false);
        }
        else if (mc.options.keyLeft.matchesKey(event.key, 0) || mc.options.keyLeft.matchesMouse(event.key)) {
            left = event.action != KeyAction.Release;
            mc.options.keyLeft.setPressed(false);
        }
        else if (mc.options.keyJump.matchesKey(event.key, 0) || mc.options.keyJump.matchesMouse(event.key)) {
            up = event.action != KeyAction.Release;
            mc.options.keyJump.setPressed(false);
        }
        else if (mc.options.keySneak.matchesKey(event.key, 0) || mc.options.keySneak.matchesMouse(event.key)) {
            down = event.action != KeyAction.Release;
            mc.options.keySneak.setPressed(false);
        }
        else {
            cancel = false;
        }

        if (cancel) event.cancel();
    }

}

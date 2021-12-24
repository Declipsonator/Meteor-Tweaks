package me.declipsonator.meteortweaks;

import me.declipsonator.meteortweaks.modules.Scaffold;
import me.declipsonator.meteortweaks.modules.hud.HudExample;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.render.hud.HUD;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.lang.invoke.MethodHandles;

public class MeteorTweaks extends MeteorAddon {
	public static final Logger LOG = LogManager.getLogger();

	@Override
	public void onInitialize() {
		LOG.info("Initializing Meteor Tweaks Addon");

		// Required when using @EventHandler
		MeteorClient.EVENT_BUS.registerLambdaFactory("me.declipsonator.meteortweaks", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));

		// Modules
		Modules.get().add(new Scaffold());


	}

}

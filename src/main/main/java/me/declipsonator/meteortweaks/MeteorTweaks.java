package me.declipsonator.meteortweaks;

import me.declipsonator.meteortweaks.modules.*;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

public class MeteorTweaks extends MeteorAddon {
	public static final Logger LOG = LogManager.getLogger();

	@Override
	public void onInitialize() {
		LOG.info("Initializing Meteor Tweaks Addon");

		// Required when using @EventHandler
		MeteorClient.EVENT_BUS.registerLambdaFactory("me.declipsonator.meteortweaks", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));

		// Modules
		Modules.get().add(new Scaffold());
		Modules.get().add(new AntiGhostBlocks());
		Modules.get().add(new VelocityBoost());
		Modules.get().add(new GUIMove());
		Modules.get().add(new AutoTool());

	}



}

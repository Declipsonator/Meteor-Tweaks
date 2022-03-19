/*
    I believe a lot of the code in this originated in ion
    Usually if I take inspiration from code I rewrite it
    In this case, it's so short that there's not really much I can do
 */

package me.declipsonator.meteortweaks.modules;

import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;

public class TpsSync extends Module {

    public TpsSync() {
        super(Categories.Misc, "tps-sync", "Prevent desyncs between the client and server.");
    }

}
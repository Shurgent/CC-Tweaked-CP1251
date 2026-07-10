// Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
//
// SPDX-License-Identifier: LicenseRef-CCPL

package dan200.computercraft.mixin.cccbridge;

import dan200.computercraft.shared.integration.cccbridge.CccBridgeText;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "cc.tweaked_programs.cccbridge.common.assistance.CharsetManipulator", remap = false)
@SuppressWarnings("unused")
class CccBridgeCharsetManipulatorMixin {
    @Inject(method = "toCCTxt", at = @At("HEAD"), cancellable = true, remap = false)
    private static void toCCTxt(String line, CallbackInfoReturnable<String> callback) {
        callback.setReturnValue(CccBridgeText.toComputerText(line));
    }

    @Inject(method = "toMCTxt", at = @At("HEAD"), cancellable = true, remap = false)
    private static void toMCTxt(String line, CallbackInfoReturnable<MutableComponent> callback) {
        callback.setReturnValue(Component.literal(CccBridgeText.fromTerminalText(line)));
    }
}

// Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
//
// SPDX-License-Identifier: LicenseRef-CCPL

package dan200.computercraft.mixin.cccbridge;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.shared.integration.cccbridge.CccBridgeText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "cc.tweaked_programs.cccbridge.common.computercraft.peripherals.TargetBlockPeripheral", remap = false)
@SuppressWarnings("unused")
class CccBridgeTargetBlockPeripheralMixin {
    @Final
    @Shadow
    public Terminal term;

    @Inject(method = "getLine", at = @At("HEAD"), cancellable = true, remap = false)
    private void getLine(int y, CallbackInfoReturnable<String> callback) throws LuaException {
        if (y < 1 || y > term.getHeight()) throw new LuaException("Expected number in range 1-" + term.getHeight());

        callback.setReturnValue(CccBridgeText.fromTerminalBuffer(term.getLine(y - 1)));
    }

    @Inject(method = "dump", at = @At("HEAD"), cancellable = true, remap = false)
    private void dump(CallbackInfoReturnable<String[]> callback) {
        var dump = new String[term.getHeight()];
        for (var i = 0; i < dump.length; i++) {
            dump[i] = CccBridgeText.fromTerminalBuffer(term.getLine(i));
        }

        callback.setReturnValue(dump);
    }
}

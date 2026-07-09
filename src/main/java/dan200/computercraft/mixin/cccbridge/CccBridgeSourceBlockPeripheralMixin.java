/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
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
@Mixin( targets = "cc.tweaked_programs.cccbridge.peripherals.SourceBlockPeripheral", remap = false )
public final class CccBridgeSourceBlockPeripheralMixin
{
    @Shadow
    @Final
    private Terminal term;

    private CccBridgeSourceBlockPeripheralMixin()
    {
    }

    @Inject( method = "getLine", at = @At( "HEAD" ), cancellable = true, remap = false )
    private void getLine( int y, CallbackInfoReturnable<String> callback ) throws LuaException
    {
        if( y < 1 || y > term.getHeight() ) throw new LuaException( "Expected number in range 1-" + term.getHeight() );

        callback.setReturnValue( CccBridgeText.fromTerminalBuffer( term.getLine( y - 1 ) ) );
    }
}

/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.mixin.cccbridge;

import dan200.computercraft.shared.integration.cccbridge.CccBridgeText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin( targets = "cc.tweaked_programs.cccbridge.Misc", remap = false )
public final class CccBridgeMiscMixin
{
    private CccBridgeMiscMixin()
    {
    }

    @Inject( method = "toCCTxt", at = @At( "HEAD" ), cancellable = true, remap = false )
    private static void toCCTxt( String line, CallbackInfoReturnable<String> callback )
    {
        callback.setReturnValue( CccBridgeText.toComputerText( line ) );
    }

    @Inject( method = "toMCTxt", at = @At( "HEAD" ), cancellable = true, remap = false )
    private static void toMCTxt( String line, CallbackInfoReturnable<MutableComponent> callback )
    {
        callback.setReturnValue( new TextComponent( CccBridgeText.fromTerminalText( line ) ) );
    }
}

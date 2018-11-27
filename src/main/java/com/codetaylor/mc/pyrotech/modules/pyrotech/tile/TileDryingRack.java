package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockDryingRack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.ITileInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.InteractionItemStack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.DryingRackRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class TileDryingRack
    extends TileDryingRackBase
    implements ITileInteractable {

  private IInteraction[] interactions;

  public TileDryingRack() {

    super();

    this.interactions = new IInteraction[4];

    for (int slot = 0; slot < 4; slot++) {

      final int qX = (slot & 1);
      final int qZ = ((slot >> 1) & 1);

      this.interactions[slot] = new TileDryingRack.Interaction(
          new ItemStackHandler[]{
              this.stackHandler,
              this.outputStackHandler
          },
          slot,
          qX,
          qZ
      );

    }

  }

  @Override
  protected int getSlotCount() {

    return 4;
  }

  @Override
  protected float getSpeedModified(float speed) {

    return (float) (speed * ModulePyrotechConfig.DRYING_RACK.SPEED_MODIFIER);
  }

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  @Override
  public EnumFacing getTileFacing(World world, BlockPos pos, IBlockState blockState) {

    return blockState.getValue(BlockDryingRack.FACING);
  }

  private class Interaction
      extends InteractionItemStack<TileDryingRack> {

    /* package */ Interaction(ItemStackHandler[] stackHandlers, int slot, double x, double z) {

      super(
          stackHandlers,
          slot,
          new EnumFacing[]{EnumFacing.UP},
          new AxisAlignedBB(x * 0.5, 0, z * 0.5, x * 0.5 + 0.5, 12f / 16f, z * 0.5 + 0.5),
          new Transform(
              Transform.translate(x * 0.375 + 0.25 + 0.0625, 0.75 + 0.03125, z * 0.375 + 0.25 + 0.0625),
              Transform.rotate(1, 0, 0, 90),
              Transform.scale(0.25, 0.25, 0.25)
          )
      );
    }

    @Override
    protected int getInsertionIndex(TileDryingRack tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      return (DryingRackRecipe.getRecipe(player.getHeldItemMainhand()) != null) ? 0 : 1;
    }
  }
}

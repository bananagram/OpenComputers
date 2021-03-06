package li.cil.oc.server.driver.item

import li.cil.oc.api.driver.Slot
import li.cil.oc.server.component
import li.cil.oc.Items
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity

object UpgradeNavigation extends Item {
  override def worksWith(stack: ItemStack) = isOneOf(stack, Items.upgradeNavigation)

  override def createEnvironment(stack: ItemStack, container: TileEntity) =
    new component.UpgradeNavigation(container)

  override def slot(stack: ItemStack) = Slot.Upgrade
}

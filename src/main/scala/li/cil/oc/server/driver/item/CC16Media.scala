package li.cil.oc.server.driver.item

import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import li.cil.oc.util.mods.{ComputerCraft16, Mods}
import li.cil.oc
import li.cil.oc.api.fs.Label
import dan200.computercraft.api.media.IMedia
import net.minecraft.nbt.NBTTagCompound
import li.cil.oc.api.driver.Slot

object CC16Media extends Item {
  override def slot(stack: ItemStack) = Slot.Disk

  override def createEnvironment(stack: ItemStack, container: TileEntity) =
    if (Mods.ComputerCraft16.isAvailable && ComputerCraft16.isDisk(stack) && container != null) {
      val address = addressFromTag(dataTag(stack))
      val mount = ComputerCraft16.createDiskMount(stack, container.getWorldObj)
      Option(oc.api.FileSystem.asManagedEnvironment(mount, new ComputerCraftLabel(stack), container)) match {
        case Some(environment) =>
          environment.node.asInstanceOf[oc.server.network.Node].address = address
          environment
        case _ => null
      }
    }
    else null

  override def worksWith(stack: ItemStack) = Mods.ComputerCraft16.isAvailable && ComputerCraft16.isDisk(stack)

  private def addressFromTag(tag: NBTTagCompound) =
    if (tag.hasKey("node") && tag.getCompoundTag("node").hasKey("address")) {
      tag.getCompoundTag("node").getString("address")
    }
    else java.util.UUID.randomUUID().toString

  private class ComputerCraftLabel(val stack: ItemStack) extends Label {
    val media = stack.getItem.asInstanceOf[IMedia]

    override def getLabel = media.getLabel(stack)

    override def setLabel(value: String) {
      media.setLabel(stack, value)
    }

    override def load(nbt: NBTTagCompound) {}

    override def save(nbt: NBTTagCompound) {}
  }

}

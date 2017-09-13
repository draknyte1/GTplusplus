package gtPlusPlus.core.util.nbt;

import static gtPlusPlus.core.item.ModItems.ZZZ_Empty;

import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTUtils {

	public static NBTTagCompound getNBT(ItemStack aStack) {
		NBTTagCompound rNBT = aStack.getTagCompound();
		return ((rNBT == null) ? new NBTTagCompound() : rNBT);
	}

	public static void setBookTitle(ItemStack aStack, String aTitle) {
		NBTTagCompound tNBT = getNBT(aStack);
		tNBT.setString("title", aTitle);
		GT_Utility.ItemNBT.setNBT(aStack, tNBT);
	}

	public static String getBookTitle(ItemStack aStack) {
		NBTTagCompound tNBT = getNBT(aStack);
		return tNBT.getString("title");
	}

	public static ItemStack[] readItemsFromNBT(ItemStack itemstack){
		NBTTagCompound tNBT = getNBT(itemstack);
		final NBTTagList list = tNBT.getTagList("Items", 10);
		ItemStack inventory[] = new ItemStack[list.tagCount()];
		for(int i = 0;i<list.tagCount();i++){
			final NBTTagCompound data = list.getCompoundTagAt(i);
			final int slot = data.getInteger("Slot");
			if((slot >= 0) && (slot < list.tagCount())){
				if (ItemStack.loadItemStackFromNBT(data) == ItemUtils.getSimpleStack(ZZZ_Empty)){
					inventory[slot] = null;
				}
				else {
					inventory[slot] = ItemStack.loadItemStackFromNBT(data);
				}

			}
		}
		return inventory;
	}
	
	public static ItemStack[] readItemsFromNBT(ItemStack itemstack, String customkey){
		NBTTagCompound tNBT = getNBT(itemstack);
		final NBTTagList list = tNBT.getTagList(customkey, 10);
		ItemStack inventory[] = new ItemStack[list.tagCount()];
		for(int i = 0;i<list.tagCount();i++){
			final NBTTagCompound data = list.getCompoundTagAt(i);
			final int slot = data.getInteger("Slot");
			if((slot >= 0) && (slot < list.tagCount())){
				if (ItemStack.loadItemStackFromNBT(data) == ItemUtils.getSimpleStack(ZZZ_Empty)){
					inventory[slot] = null;
				}
				else {
					inventory[slot] = ItemStack.loadItemStackFromNBT(data);
				}

			}
		}
		return inventory;
	}

	public static ItemStack writeItemsToNBT(ItemStack itemstack, ItemStack[] stored){
		NBTTagCompound tNBT = getNBT(itemstack);
		final NBTTagList list = new NBTTagList();
		for(int i = 0;i<stored.length;i++){
			final ItemStack stack = stored[i];
			if(stack != null){
				final NBTTagCompound data = new NBTTagCompound();
				stack.writeToNBT(data);
				data.setInteger("Slot", i);
				list.appendTag(data);
			}
			else {
				final NBTTagCompound data = new NBTTagCompound();
				ItemStack nullstack = ItemUtils.getSimpleStack(ZZZ_Empty);
				nullstack.writeToNBT(data);
				data.setInteger("Slot", i);
				list.appendTag(data);
			}
		}
		tNBT.setTag("Items", list);
		itemstack.setTagCompound(tNBT);
		return itemstack;
	}
	
	public static ItemStack writeItemsToNBT(ItemStack itemstack, ItemStack[] stored, String customkey){
		NBTTagCompound tNBT = getNBT(itemstack);
		final NBTTagList list = new NBTTagList();
		for(int i = 0;i<stored.length;i++){
			final ItemStack stack = stored[i];
			if(stack != null){
				final NBTTagCompound data = new NBTTagCompound();
				stack.writeToNBT(data);
				data.setInteger("Slot", i);
				list.appendTag(data);
			}
		}
		tNBT.setTag(customkey, list);
		itemstack.setTagCompound(tNBT);
		return itemstack;
	}
	
	public static ItemStack writeItemsToGtCraftingComponents(ItemStack rStack, ItemStack[] stored, boolean copyTags){
		
		if (copyTags){
			for (int i = 0; i < stored.length; i++) {
	            if (stored[i] != null && stored[i].hasTagCompound()) {
	                rStack.setTagCompound((NBTTagCompound) stored[i].getTagCompound().copy());
	                break;
	            }
	        }
		}		
		
		 NBTTagCompound rNBT = rStack.getTagCompound(), tNBT = new NBTTagCompound();
         if (rNBT == null) rNBT = new NBTTagCompound();
         for (int i = 0; i < 9; i++) {
             ItemStack tStack = stored[i];
             if (tStack != null && GT_Utility.getContainerItem(tStack, true) == null && !(tStack.getItem() instanceof GT_MetaGenerated_Tool)) {
                 tStack = GT_Utility.copyAmount(1, tStack);
                 if(GT_Utility.isStackValid(tStack)){
                 GT_ModHandler.dischargeElectricItem(tStack, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false, true);
                 tNBT.setTag("Ingredient." + i, tStack.writeToNBT(new NBTTagCompound()));}
             }
         }
         rNBT.setTag("GT.CraftingComponents", tNBT);
         rStack.setTagCompound(rNBT);
		return rStack;
	}


}

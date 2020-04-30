package travellersgear.common.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class ComparableItemStack {

	private final ItemStack stack;

	public ComparableItemStack(final ItemStack stack) {
		this.stack = stack;
	}

    @Override
    public boolean equals(final Object object)
    {
        if (!(object instanceof ComparableItemStack))
            return false;
        return OreDictionary.itemMatches(this.stack, ((ComparableItemStack) object).getStack(), true);
    }

	public ItemStack getStack() {
		return stack;
	}
}

package travellersgear.client;

import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

public final class ToolDisplayInfo {

	public int slot;
	public float[] translation;
	public float[] rotation;
	public float[] scale;

	public boolean rotateWithHead;
	public boolean hideWhenEquipped;

	public ToolDisplayInfo(final int slot, final float[] translation, final float[] rotation, final float[] scale) {
		this.slot        = slot;
		this.translation = translation;
		this.rotation    = rotation;
		this.scale       = scale;
	}

    @NotNull
    public static ToolDisplayInfo readFromNBT(@NotNull final NBTTagCompound tag) {
        final int slot = tag.getInteger("slot");

        final float[] translation = {
            tag.getFloat("x"),
            tag.getFloat("y"),
            tag.getFloat("z")
        };
        final float[] rotation = {
            tag.getFloat("rX"),
            tag.getFloat("rY"),
            tag.getFloat("rZ")
        };
        final float[] scale = {
            tag.getFloat("sX"),
            tag.getFloat("sY"),
            tag.getFloat("sZ")
        };
        boolean rotateWithHead   = tag.getBoolean("rotateWithHead");
        boolean hideWhenEquipped = tag.getBoolean("hideWhenEquipped");

        final ToolDisplayInfo displayInfo = new ToolDisplayInfo(slot, translation, rotation, scale);
        displayInfo.rotateWithHead   = rotateWithHead;
        displayInfo.hideWhenEquipped = hideWhenEquipped;
        return displayInfo;
    }

	@NotNull
    public NBTTagCompound writeToNBT() {
		final NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("slot", slot);

		if (translation != null && translation.length > 2) {
			tag.setFloat("x", translation[0]);
			tag.setFloat("y", translation[1]);
			tag.setFloat("z", translation[2]);
		}
		if (rotation != null && rotation.length > 2) {
			tag.setFloat("rX", rotation[0]);
			tag.setFloat("rY", rotation[1]);
			tag.setFloat("rZ", rotation[2]);
		}
		if (scale != null && scale.length > 2) {
			tag.setFloat("sX", scale[0]);
			tag.setFloat("sY", scale[1]);
			tag.setFloat("sZ", scale[2]);
		}
		tag.setBoolean("rotateWithHead",   rotateWithHead);
		tag.setBoolean("hideWhenEquipped", hideWhenEquipped);
		return tag;
	}
}

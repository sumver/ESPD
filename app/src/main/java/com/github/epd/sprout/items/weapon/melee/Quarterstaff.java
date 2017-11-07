
package com.github.epd.sprout.items.weapon.melee;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class Quarterstaff extends MeleeWeapon {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.QUARTERSTAFF;
	}

	public Quarterstaff() {
		super(2, 1f, 1f);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}

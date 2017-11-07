
package com.github.epd.sprout.items.weapon.missiles;

import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Dart extends MissileWeapon {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.DART;

		MIN = 1;
		MAX = 4;

		bones = false; // Finding them in bones would be semi-frequent and
		// disappointing.
	}

	public Dart() {
		this(1);
	}

	public Dart(int number) {
		super();
		quantity = number;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	@Override
	public Item random() {
		quantity = Random.Int(5, 15);
		return this;
	}

	@Override
	public int price() {
		return quantity * 2;
	}
}

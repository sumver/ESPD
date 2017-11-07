
package com.github.epd.sprout.items.food;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.buffs.Blindness;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class DeathCap extends Food {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.MUSHROOM_DEATHCAP;
		energy = (Hunger.STARVING - Hunger.HUNGRY) / 10;
		message = Messages.get(BlueMilk.class, "eat");
		bones = false;
	}

	private static final String TXT_PREVENTING = Messages.get(BlueMilk.class, "prevent");
	private static final String TXT_EFFECT = Messages.get(DeathCap.class, "effect");

	@Override
	public void execute(Hero hero, String action) {

		if (action.equals(AC_EAT)) {

			if (Dungeon.bossLevel()) {
				GLog.w(TXT_PREVENTING);
				return;
			}

		}

		if (action.equals(AC_EAT)) {


			GLog.w(TXT_EFFECT);

			switch (Random.Int(10)) {
				case 1:
					for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
						if (mob.hostile)
							mob.damage(Math.max(5, Math.round(mob.HP / 2)), this);
					}
					hero.damage(Math.max(1, Math.round(hero.HP / 4)), this);
					Buff.prolong(hero, Blindness.class, Random.Int(5, 7));
					break;
				case 0:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
					for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
						if (mob.hostile) {
							mob.damage(Math.max(3, Math.round(mob.HP / 2)), this);
							mob.aggro(hero);
						}
					}
					hero.damage(Math.max(1, Math.round(hero.HP / 4)), this);
					Buff.prolong(hero, Blindness.class, Random.Int(6, 9));
					break;
			}
		}

		super.execute(hero, action);
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}

	@Override
	public int price() {
		return 20 * quantity;
	}

	public DeathCap() {
		this(1);
	}

	public DeathCap(int value) {
		this.quantity = value;
	}
}

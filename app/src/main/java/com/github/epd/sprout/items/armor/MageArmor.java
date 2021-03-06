
package com.github.epd.sprout.items.armor;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Burning;
import com.github.epd.sprout.actors.buffs.Roots;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.hero.HeroClass;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.effects.particles.ElmoParticle;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class MageArmor extends ClassArmor {

	private static final String AC_SPECIAL = Messages.get(MageArmor.class, "ac_special");

	private static final String TXT_NOT_MAGE = Messages.get(MageArmor.class, "not_mage");

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.ARMOR_MAGE;
	}

	@Override
	public String special() {
		return AC_SPECIAL;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	@Override
	public void doSpecial() {

		for (Mob mob : Dungeon.level.mobs) {
			if (Level.fieldOfView[mob.pos]) {
				Buff.affect(mob, Burning.class).reignite(mob);
				Buff.prolong(mob, Roots.class, 3);
			}
		}

		curUser.HP -= (curUser.HP / 3);

		curUser.spend(Actor.TICK);
		curUser.sprite.operate(curUser.pos);
		curUser.busy();

		curUser.sprite.centerEmitter().start(ElmoParticle.FACTORY, 0.15f, 4);
		Sample.INSTANCE.play(Assets.SND_READ);
	}

	@Override
	public boolean doEquip(Hero hero) {
		if (hero.heroClass == HeroClass.MAGE) {
			return super.doEquip(hero);
		} else {
			GLog.w(TXT_NOT_MAGE);
			return false;
		}
	}
}
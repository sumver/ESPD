
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.items.teleporter.Bone;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.RedDewdrop;
import com.github.epd.sprout.items.YellowDewdrop;
import com.github.epd.sprout.items.teleporter.PrisonKey;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.MossySkeletonSprite;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class MossySkeleton extends Mob {

	private static final String TXT_HERO_KILLED = Messages.get(MossySkeleton.class, "kill");
	private static final String TXT_KILLCOUNT = Messages.get(MossySkeleton.class, "count");

	{
		name = Messages.get(this, "name");
		spriteClass = MossySkeletonSprite.class;

		HP = HT = 40 + (10 * Random.NormalIntRange(7, 10));
		defenseSkill = 17;

		EXP = 1;
		maxLvl = 10;

		baseSpeed = 0.5f + Math.min(2f, Statistics.skeletonsKilled / 50);

		loot = new YellowDewdrop();
		lootChance = 0.5f; // by default, see die()

		lootOther = new RedDewdrop();
		lootChanceOther = 0.1f; // by default, see die()

		properties.add(Property.UNDEAD);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(20 + Math.round(Statistics.skeletonsKilled / 10), 45 + Math.round(Statistics.skeletonsKilled / 5));

	}

	@Override
	protected float attackDelay() {
		return 2f - (Math.min(1.5f, Statistics.skeletonsKilled / 50));
	}

	@Override
	public void die(Object cause) {

		super.die(cause);

		Statistics.skeletonsKilled++;
		GLog.h(TXT_KILLCOUNT, Statistics.skeletonsKilled);

		if (Dungeon.depth < 27) {
			Item.autocollect(new PrisonKey(), pos);
			explodeDew(pos);
		} else {
			explodeDew(pos);
		}

		if (!Dungeon.limitedDrops.bone.dropped() && Statistics.skeletonsKilled > 30 && Random.Int(10) == 0) {
			Dungeon.limitedDrops.bone.drop();
			Item.autocollect(new Bone(), pos);
		}

		if (!Dungeon.limitedDrops.bone.dropped() && Statistics.skeletonsKilled > 50) {
			Dungeon.limitedDrops.bone.drop();
			Item.autocollect(new Bone(), pos);
		}

		boolean heroKilled = false;
		for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
			Char ch = findChar(pos + PathFinder.NEIGHBOURS8[i]);
			if (ch != null && ch.isAlive()) {
				int damage = Math.max(0,
						Random.NormalIntRange(3, 8) - Random.IntRange(0, ch.dr() / 2));
				ch.damage(damage, this);
				if (ch == Dungeon.hero && !ch.isAlive()) {
					heroKilled = true;
				}
			}
		}

		if (Dungeon.visible[pos]) {
			Sample.INSTANCE.play(Assets.SND_BONES);
		}

		if (heroKilled) {
			Dungeon.fail(Utils.format(ResultDescriptions.MOB,
					Utils.indefinite(name)));
			GLog.n(TXT_HERO_KILLED);
		}
	}


	@Override
	public int attackSkill(Char target) {
		return 28;
	}

	@Override
	public int dr() {
		return 27;
	}

	@Override
	public String defenseVerb() {
		return Messages.get(this, "def");
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
	}
}

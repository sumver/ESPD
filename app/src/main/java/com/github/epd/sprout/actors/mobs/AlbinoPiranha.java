
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.buffs.Burning;
import com.github.epd.sprout.actors.buffs.Frost;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.actors.buffs.Roots;
import com.github.epd.sprout.items.teleporter.ConchShell;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.food.Meat;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.AlbinoPiranhaSprite;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

import java.util.HashSet;

public class AlbinoPiranha extends Mob {

	private static final String TXT_KILLCOUNT = Messages.get(AlbinoPiranha.class, "count");

	{
		name = Messages.get(AlbinoPiranha.class, "name");
		spriteClass = AlbinoPiranhaSprite.class;

		baseSpeed = 2f;

		EXP = 0;

		loot = new Meat();
		lootChance = 0.1f;

	}

	public AlbinoPiranha() {
		super();

		HP = HT = 10 + Dungeon.depth * 5;
		defenseSkill = 10 + Dungeon.depth * 2;
	}

	protected boolean checkwater(int cell) {
		return Level.water[cell];
	}


	@Override
	protected boolean act() {

		if (!Level.water[pos]) {
			damage(HT, this);
			//die(null);
			return true;


		} else {
			// this causes pirahna to move away when a door is closed on them.
			Dungeon.level.updateFieldOfView(this, Level.fieldOfView);
			enemy = chooseEnemy();
			if (state == this.HUNTING && !(enemy.isAlive() && Level.fieldOfView[enemy.pos] && Level.water[enemy.pos])) {
				state = this.WANDERING;
				int oldPos = pos;
				int i = 0;
				do {
					i++;
					target = Dungeon.level.randomDestination();
					if (i == 100)
						return true;
				} while (!getCloser(target));
				moveSprite(oldPos, pos);
				return true;
			}
			if (!Level.water[enemy.pos] || enemy.flying) {
				enemy.invisible = 1;
			} else {
				enemy.invisible = 0;
			}

			return super.act();
		}
	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange(Dungeon.depth, 4 + Dungeon.depth * 2);
	}

	@Override
	public int attackSkill(Char target) {
		return 20 + Dungeon.depth * 2;
	}

	@Override
	public int dr() {
		return Dungeon.depth;
	}

	@Override
	public void die(Object cause) {
		explodeDew(pos);
		if (Random.Int(55 - Math.min(Statistics.albinoPiranhasKilled, 50)) == 0) {
			Item mushroom = Generator.random(Generator.Category.MUSHROOM);
			Item.autocollect(mushroom, pos);
		}

		if (!Dungeon.limitedDrops.conchshell.dropped() && Statistics.albinoPiranhasKilled > 30 && Random.Int(10) == 0) {
			Dungeon.limitedDrops.conchshell.drop();
			Item.autocollect(new ConchShell(), pos);
		}

		if (!Dungeon.limitedDrops.conchshell.dropped() && Statistics.albinoPiranhasKilled > 50) {
			Dungeon.limitedDrops.conchshell.drop();
			Item.autocollect(new ConchShell(), pos);
		}

		super.die(cause);

		Statistics.albinoPiranhasKilled++;
		GLog.h(TXT_KILLCOUNT, Statistics.albinoPiranhasKilled);
	}

	@Override
	public boolean reset() {
		return true;
	}

	@Override
	protected boolean getCloser(int target) {

		if (rooted) {
			return false;
		}

		int step = Dungeon.findStep(this, pos, target,
				Level.water,
				Level.fieldOfView);
		if (step != -1) {
			move(step);
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean getFurther(int target) {
		int step = Dungeon.flee(this, pos, target, Level.water,
				Level.fieldOfView);
		if (step != -1) {
			move(step);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String description() {
		return Messages.get(AlbinoPiranha.class, "desc");
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

	static {
		IMMUNITIES.add(Burning.class);
		IMMUNITIES.add(Paralysis.class);
		IMMUNITIES.add(ToxicGas.class);
		IMMUNITIES.add(Roots.class);
		IMMUNITIES.add(Frost.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}

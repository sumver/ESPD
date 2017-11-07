
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Light;
import com.github.epd.sprout.actors.buffs.Terror;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.PurpleParticle;
import com.github.epd.sprout.items.food.Meat;
import com.github.epd.sprout.items.potions.PotionOfHealing;
import com.github.epd.sprout.items.wands.WandOfDisintegration;
import com.github.epd.sprout.items.weapon.enchantments.Death;
import com.github.epd.sprout.items.weapon.enchantments.Leech;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.EyeSprite;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Eye extends Mob {

	private static final String TXT_DEATHGAZE_KILLED = Messages.get(Eye.class, "kill");
	protected static final float SPAWN_DELAY = 2f;

	{
		name = Messages.get(this, "name");
		spriteClass = EyeSprite.class;

		HP = HT = 130 + (adj(0) * Random.NormalIntRange(4, 7));
		defenseSkill = 20 + adj(1);
		viewDistance = Light.DISTANCE;

		EXP = 13;
		maxLvl = 25;

		flying = true;

		loot = new PotionOfHealing();
		lootChance = 0.1f;

		lootOther = new Meat();
		lootChanceOther = 0.5f; // by default, see die()

		properties.add(Property.UNDEAD);
	}

	@Override
	public int dr() {
		return 25 + adj(1);
	}

	private Ballistica beam;

	@Override
	protected boolean canAttack(Char enemy) {
		beam = new Ballistica(pos, enemy.pos, Ballistica.STOP_TERRAIN);
		return beam.subPath(1, beam.dist).contains(enemy.pos);
	}

	@Override
	public int attackSkill(Char target) {
		return 30 + adj(0);
	}

	@Override
	protected float attackDelay() {
		return 1.6f;
	}

	@Override
	protected boolean doAttack(Char enemy) {

		spend(attackDelay());

		boolean rayVisible = false;

		for (int i : beam.subPath(0, beam.dist)) {
			if (Dungeon.visible[i]) {
				rayVisible = true;
			}
		}

		if (rayVisible) {
			sprite.attack(beam.collisionPos);
			return false;
		} else {
			attack(enemy);
			return true;
		}
	}

	@Override
	public boolean attack(Char enemy) {

		for (int pos : beam.subPath(1, beam.dist)) {

			Char ch = Actor.findChar(pos);
			if (ch == null) {
				continue;
			}

			if (hit(this, ch, true)) {
				ch.damage(Random.NormalIntRange(14, 20 + adj(0)), this);

				if (Dungeon.visible[pos]) {
					ch.sprite.flash();
					CellEmitter.center(pos).burst(PurpleParticle.BURST,
							Random.IntRange(1, 2));
				}

				if (!ch.isAlive() && ch == Dungeon.hero) {
					Dungeon.fail(Utils.format(ResultDescriptions.MOB,
							Utils.indefinite(name)));
					GLog.n(TXT_DEATHGAZE_KILLED, name);
				}
			} else {
				ch.sprite.showStatus(CharSprite.NEUTRAL, ch.defenseVerb());
			}
		}

		return true;
	}

	public static void spawnAroundChance(int pos) {
		for (int n : PathFinder.NEIGHBOURS4) {
			int cell = pos + n;
			if (Level.passable[cell] && Actor.findChar(cell) == null && Random.Float() < 0.50f) {
				spawnAt(cell);
			}
		}
	}

	public static Eye spawnAt(int pos) {
		if (Level.passable[pos] && Actor.findChar(pos) == null) {

			Eye e = new Eye();
			e.pos = pos;
			e.state = e.HUNTING;
			GameScene.add(e, SPAWN_DELAY);

			e.sprite.alpha(0);
			e.sprite.parent.add(new AlphaTweener(e.sprite, 1, 0.5f));

			return e;

		} else {
			return null;
		}
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
	}

	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

	static {
		RESISTANCES.add(WandOfDisintegration.class);
		RESISTANCES.add(Death.class);
		RESISTANCES.add(Leech.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

	static {
		IMMUNITIES.add(Terror.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}

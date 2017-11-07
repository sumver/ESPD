
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.watabou.noosa.TextureFilm;

public class SokobanSentinelSprite extends MobSprite {

	public SokobanSentinelSprite() {
		super();

		texture(Assets.SENTINEL);

		TextureFilm frames = new TextureFilm(texture, 12, 15);

		idle = new Animation(2, true);
		idle.frames(frames, 0, 0, 0, 0, 0, 1, 1);

		run = new Animation(15, true);
		run.frames(frames, 2, 3, 4, 5, 6, 7);

		attack = new Animation(4, false);
		attack.frames(frames, 8, 9, 10);

		die = new Animation(5, false);
		die.frames(frames, 11, 12, 13, 14, 15, 15);

		play(idle);
	}

	@Override
	public int blood() {
		return 0xFFcdcdb7;
	}
}

/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.github.epd.sprout.effects;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.actors.blobs.Blob;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;

public class BlobEmitter extends Emitter {

	private static final int WIDTH = Blob.WIDTH;
	private static final int LENGTH = Blob.LENGTH;

	private Blob blob;

	public BlobEmitter(Blob blob) {

		super();

		this.blob = blob;
		blob.use(this);
	}

	@Override
	protected void emit(int index) {

		if (blob.volume <= 0) {
			return;
		}

		if (blob.area.isEmpty())
			blob.setupArea();

		int[] map = blob.cur;
		float size = DungeonTilemap.SIZE;

		int cell;
		for (int i = blob.area.left; i < blob.area.right; i++) {
			for (int j = blob.area.top; j < blob.area.bottom; j++) {
				cell = i + j*WIDTH;
				if (map[cell] > 0 && Dungeon.visible[cell]) {
					float x = (i + Random.Float()) * size;
					float y = (j + Random.Float()) * size;
					factory.emit(this, index, x, y);
				}
			}
		}
	}
}
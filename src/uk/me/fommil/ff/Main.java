/*
 * Copyright Samuel Halliday 2009
 *
 * This file is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This file is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this file.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package uk.me.fommil.ff;

import uk.me.fommil.ff.physics.GamePhysics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import uk.me.fommil.ff.swos.TacticsParser;
import uk.me.fommil.ff.swos.PitchParser;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import uk.me.fommil.ff.swos.SpriteParser;

/**
 * @author Samuel Halliday
 * @see <a href="http://hotud.org/component/content/article/42-sport/22386">SWOS 96/97 Download</a>
 */
@SuppressWarnings("serial")
public class Main {

	public static final File SWOS = new File("data/Sensible World of Soccer 96-97");

	/**
	 * @param args
	 * @throws IOException
	 */
	public static final void main(String[] args) throws IOException {


		Map<String, Tactics> swosTactics = TacticsParser.getSwosTactics(SWOS);
		BufferedImage pitchImage = PitchParser.getPitch(SWOS, 6);

		Map<Integer, Sprite> sprites = SpriteParser.getSprites(SWOS);

		Team a = new Team();
		a.setCurrentTactics(swosTactics.get("442"));
		Team b = new Team();
		b.setCurrentTactics(swosTactics.get("433"));

		Pitch pitch = new Pitch();
		final GamePhysics game = new GamePhysics(a, pitch);
		final ClassicView gv = new ClassicView(game, pitchImage, sprites);

		JFrame frame = new JFrame();
		frame.add(gv);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Foolish Football");
		frame.setUndecorated(true);
		frame.setVisible(true);

		final long period = 50L;
		TimerTask ticker = new TimerTask() {

			@Override
			public synchronized void run() {
				game.tick(period / 1000.0);
				gv.repaint();
			}
		};
		new Timer().schedule(ticker, 0L, period);

		assert gv.getKeyListeners().length > 0;

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		gs[0].setFullScreenWindow(frame);
	}
}

package utils;

import core.Board;
import core.HGame;
import core.HPlayer;
import main.Main;
import me.filoghost.holographicdisplays.api.beta.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.beta.hologram.Hologram;
import me.filoghost.holographicdisplays.api.beta.hologram.HologramLines;
import me.filoghost.holographicdisplays.api.beta.hologram.line.HologramLine;
import me.filoghost.holographicdisplays.api.beta.hologram.line.TextHologramLine;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramLines;
import reflect.CreditImporter;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.stream.Collectors;

public class ScoreboardManager {
	
	public static void updateStage(Board board, String name) {
		if (board.get(11).contains(name)) return;
		board.set(11, "Stage: §a" + name);
	}
	
	public static void updateScoreboard(HPlayer p) {
		if (p.isInDuel()) {updateDuelScoreboard(p);return;}
		
		int minutes = p.getTime() / 60;
		int seconds = (p.getTime()) % 60;
		String str = String.format("%d:%02d", minutes, seconds);
		if (p.isInParty()) {
			for (HPlayer hp : p.getParty()) {
				hp.getBoard().set(10, "Play Time: §a" + str);
				hp.getBoard().set(8, "Perfect Walls: §a" + hp.getPerfectWall());
				hp.getBoard().set(7, "Wall: §a" + hp.getWall());
				hp.getBoard().set(6, "Score: §a" + hp.getScore());
				hp.getBoard().set(4, "Missing block: §4" + hp.getMissed());
				hp.getBoard().set(3, "Misplaced block: §4" + hp.getMisplaced());
				hp.getBoard().set(2, "Choke: §4" + hp.getChoke());
			}
		} else {
			p.getBoard().set(10, "Play Time: §a" + str);
			p.getBoard().set(8, "Perfect Walls: §a" + p.getPerfectWall());
			p.getBoard().set(7, "Wall: §a" + p.getWall());
			p.getBoard().set(6, "Score: §a" + p.getScore());
			p.getBoard().set(4, "Missing block: §4" + p.getMissed());
			p.getBoard().set(3, "Misplaced block: §4" + p.getMisplaced());
			p.getBoard().set(2, "Choke: §4" + p.getChoke());
		}
		
		HGame game = Main.hGames.stream().filter(g -> g.getOwner() == p).collect(Collectors.toList()).get(0);
		if (game == null) return;
		Hologram hologram = game.getHologram();
		if (hologram == null) return;
		
		HologramLines lines = hologram.getLines();
		
		if (lines.size() != 11) {
			lines.clear();
			for (int i = 0; i < 11; i++) {
				lines.appendText(" ");
			}
		}
		((TextHologramLine) lines.get(0)).setText(p.getDisplayName());
		((TextHologramLine) lines.get(1)).setText(" ");
		((TextHologramLine) lines.get(2)).setText("§6Play Time: §a" + str);
		((TextHologramLine) lines.get(3)).setText("§6Perfect Walls: §a" + p.getPerfectWall());
		((TextHologramLine) lines.get(4)).setText("§6Wall: §a" + p.getWall());
		((TextHologramLine) lines.get(5)).setText(" ");
		((TextHologramLine) lines.get(6)).setText("§6Score: §a" + p.getScore());
		((TextHologramLine) lines.get(7)).setText(" ");
		((TextHologramLine) lines.get(8)).setText("§6Missing block: §4" + p.getMissed());
		((TextHologramLine) lines.get(9)).setText("§6Misplaced block: §4" + p.getMisplaced());
		((TextHologramLine) lines.get(10)).setText("§6Choke: §4" + p.getChoke());
	}
	
	
	public static void updateDuelScoreboard(HPlayer p) {
		int minutes = p.getTime() / 60;
		int seconds = (p.getTime()) % 60;
		String str = String.format("%d:%02d", minutes, seconds);

		if (p.isInParty()) {
			HPlayer leader = p.getParty().get(0);
			
			for (HPlayer hp : p.getParty()) {
				String player1 = leader.getDisplayName().substring(0, 2) + leader.getPlayer().getName() + "§r: " + leader.getScore();
				String player2 = leader.getOpponent().getDisplayName().substring(0, 2) + leader.getOpponent().getPlayer().getName() + "§r: " + leader.getOpponent().getScore();
				
				hp.getBoard().set(8, "Play Time: §a" + str);
				hp.getBoard().set(7, " ");
				hp.getBoard().set(6, leader.getScore() >= leader.getOpponent().getScore() ? player1 : player2);
				hp.getBoard().set(5, leader.getScore() < leader.getOpponent().getScore() ? player1 : player2);
				hp.getBoard().set(4, "  ");
				hp.getBoard().set(3, "Perfect Walls: §a" + leader.getPerfectWall());
				hp.getBoard().set(2, "Wall: §a" + leader.getWall());
			}
		} else {
			String player1 = p.getDisplayName().substring(0, 2) + p.getPlayer().getName() + "§r: " + p.getScore();
			String player2 = p.getOpponent().getDisplayName().substring(0, 2) + p.getOpponent().getPlayer().getName() + "§r: " + p.getOpponent().getScore();
			
			p.getBoard().set(8, "Play Time: §a" + str);
			p.getBoard().set(7, " ");
			p.getBoard().set(6, p.getScore() >= p.getOpponent().getScore() ? player1 : player2);
			p.getBoard().set(5, p.getScore() < p.getOpponent().getScore() ? player1 : player2);
			p.getBoard().set(4, "  ");
			p.getBoard().set(3, "Perfect Walls: §a" + p.getPerfectWall());
			p.getBoard().set(2, "Wall: §a" + p.getWall());
		}
		
		HGame game = GameUtils.getGameArea(p.getPlayer());
		
		if (game == null) return;
		Hologram hologram = game.getHologram();
		if (hologram == null) return;
		
		HologramLines lines = hologram.getLines();
		
		if (lines.size() != 11) {
			lines.clear();
			for (int i = 0; i < 11; i++) {
				lines.appendText(" ");
			}
		}
		((TextHologramLine) lines.get(0)).setText(p.getDisplayName());
		((TextHologramLine) lines.get(1)).setText(" ");
		((TextHologramLine) lines.get(2)).setText("§6Play Time: §a" + str);
		((TextHologramLine) lines.get(3)).setText("§6Perfect Walls: §a" + p.getPerfectWall());
		((TextHologramLine) lines.get(4)).setText("§6Wall: §a" + p.getWall());
		((TextHologramLine) lines.get(5)).setText(" ");
		((TextHologramLine) lines.get(6)).setText("§6Score: §a" + p.getScore());
		((TextHologramLine) lines.get(7)).setText(" ");
		((TextHologramLine) lines.get(8)).setText("§6Missing block: §4" + p.getMissed());
		((TextHologramLine) lines.get(9)).setText("§6Misplaced block: §4" + p.getMisplaced());
		((TextHologramLine) lines.get(10)).setText("§6Choke: §4" + p.getChoke());
	}
	
	public static void setDuelScoreboard(String stage, HPlayer p) {
		int minutes = 0 / 60;
		int seconds = (0) % 60;
		String str = String.format("%d:%02d", minutes, seconds);

		p.getBoard().remove(11);
		p.getBoard().set(10, "§a§m-----------------");
		p.getBoard().set(9, "Stage: §a" + stage);
		p.getBoard().set(8, "Play Time: §a" + str);
		p.getBoard().set(7, " ");
		p.getBoard().set(6, p.getDisplayName().substring(0, 2) + p.getPlayer().getName() + "§r: " + 0);
		p.getBoard().set(5, p.getOpponent().getDisplayName().substring(0, 2) + p.getOpponent().getPlayer().getName() + "§r: " + 0);
		p.getBoard().set(4, "  ");
		p.getBoard().set(3, "Perfect Walls: §a" + 0);
		p.getBoard().set(2, "Wall: §a" + 0);
		p.getBoard().set(1, "§a§m-----------------§r");
	}

	public static void updateCredit(Board board, HPlayer player) {
		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
		String formattedCredit = numberFormat.format(CreditImporter.getCredits(player));


		board.set(13, "Credits: §6§l" + formattedCredit);
	}

	public static void setDefaultScoreboard(Board board, HPlayer player) {
		int minutes = 0 / 60;
		int seconds = (0) % 60;
		String str = String.format("%d:%02d", minutes, seconds);


		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
		String formattedCredit = numberFormat.format(CreditImporter.getCredits(player));

		board.set(14, "§a§m-----------------");
		board.set(13, "Credits: §6§l" + formattedCredit);
		board.set(12, "     ");
		board.set(11, "Stage: §anone");
		board.set(10, "Play Time: §a" + str);
		board.set(9, "  ");
		board.set(8, "Perfect Walls: §a" + 0);
		board.set(7, "Wall: §a" + 0);
		board.set(6, "Score: §a" + 0);
		board.set(5, "   ");
		board.set(4, "Missing block: §4" + 0);
		board.set(3, "Misplaced block: §4" + 0);
		board.set(2, "Choke: §4" + 0);
		board.set(1, "§a§m-----------------§r");
	}
}

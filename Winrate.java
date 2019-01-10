package com.fmi.uni.Commands;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.fmi.uni.Connections.Connector;
import com.fmi.uni.Parsers.DataParser;

public class Winrate extends Command {

	protected Winrate() {
	}

	public String formatOutput(Double input) {
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);

		return df.format(input);
	}

	public void printWinrate(Long wins, Long losses) {
		System.out.println("wins: " + wins);
		System.out.println("losses: " + losses);
		double winrate = 0;
		if (wins + losses != 0) {
			winrate = (double) wins / (wins + losses);
		}
		System.out.println("Total winrate: " + formatOutput(winrate));
	}

	@Override
	public void execute() {

		String jsonData = Connector.getWinrate(profile_id);
		JSONObject object = null;
		try {
			object = DataParser.getJSONObject(jsonData);
		} catch (ParseException e1) {
			System.err.println("Problem with getting json object in getwinrate");
		}
		long wins = 0;
		long losses = 0;

		try {
			wins = DataParser.parseSingleObjectLong(object, "win");
			losses = DataParser.parseSingleObjectLong(object, "lose");
		} catch (NullPointerException ex) {
			System.err.println("Null pointer in winrate");
		} catch (ParseException e) {
			System.err.println("problem with parsing in winrate");
		}

		printWinrate(wins, losses);
	}

	@Override
	public void setProfileId(long profile_id) {
		super.profile_id = profile_id;
	}

}

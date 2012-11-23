package com.nterop.tests.selenium.actions;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// create Options object
		Options options = new Options();

		// add t option
		final String user_cmd = "u";
		final String pass_cmd = "p";
		final String no_cmd = "n";
		final String reports_cmd = "r";
		options.addOption(user_cmd, true, "Username");
		options.addOption(pass_cmd, true, "Parssword");
		options.addOption(no_cmd, true, "Number of items or report to generate.");
		options.addOption(reports_cmd, false, "Create Reports. By default will create Items");

		CommandLineParser parser = new GnuParser();
		try {
			String username;
			String pass;
			String no;

			// parse the command line arguments
			CommandLine cmd = parser.parse(options, args);
			username = cmd.getOptionValue(user_cmd);
			pass = cmd.getOptionValue(pass_cmd);
			no = cmd.getOptionValue(no_cmd);

			if (username != null && pass != null && no != null) {
				Generator gen = new Generator();
				int n = Integer.valueOf(no).intValue();
				if (!cmd.hasOption(reports_cmd)) {
					gen.addItems(username, pass, n);
				} else {
					gen.addReports(username, pass, n);
				}
				gen.cleanup();
			} else {
				println("Must provide username password and number of items");
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("ant", options);
			}
		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void println(String msg) {
		System.out.println(msg);
	}
}

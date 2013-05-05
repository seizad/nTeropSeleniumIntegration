package com.nterop.tests.selenium.actions;

import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;

public class Main {
	
	static int count = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// create Options object
		final Options options = new Options();

		// add t option
		final String timeout_cmd = "timeout";
		final String delay_cmd = "delay";
		final String url_cmd = "url";
		final String user_file_cmd = "file";
		final String no_cmd = "n";
		final String reports_cmd = "report";
		final String parades_cmd = "parade";
		options.addOption(url_cmd, true, "URL to test agains.");
		options.addOption(timeout_cmd, true, "Selenium Timeout.");
		options.addOption(delay_cmd, true, "Custom delay for load times.");
		options.addOption(user_file_cmd, true, "Path to the CSV file containing username passwords.");
		options.addOption(no_cmd, true, "Number of items or report to generate.");
		options.addOption(reports_cmd, false, "If set, will create Reports. By default will create Items");
		options.addOption(parades_cmd, false, "If set, will create Parades. By default will create Items");

		final HelpFormatter formatter = new HelpFormatter();
		
		CommandLineParser parser = new GnuParser();
		try {
			// parse the command line arguments
			final CommandLine cmd = parser.parse(options, args);
			final String user_file = cmd.getOptionValue(user_file_cmd);
			final String no = cmd.getOptionValue(no_cmd);
			final String timeout_str = cmd.getOptionValue(timeout_cmd);
			final String delay_str = cmd.getOptionValue(delay_cmd);
			final String url = cmd.getOptionValue(url_cmd);

			final int timeout = Integer.valueOf(timeout_str).intValue();
			final int delay = Integer.valueOf(delay_str).intValue();

			// read username password from file
			if (user_file != null) {
				Reader reader = new FileReader(user_file);
				CSVReader<String[]> csvParser = CSVReaderBuilder.newDefaultReader(reader);
				List<String[]> data = csvParser.readAll();


				for (final String [] users : data) {

					new Thread(new Runnable() {

						@Override
						public void run() {
							Generator gen = new Generator(url, timeout, delay);
							try {
								// null) {
								if (no != null && url != null) {
									int n = Integer.valueOf(no).intValue();
									String username = users[0];
									String pass = users[1];
									String usr_district = users[2];
									if (cmd.hasOption(reports_cmd)) {
										gen.addReportsWithDistinctItems(username, pass, n, usr_district);
									} else if (cmd.hasOption(parades_cmd)) {
										gen.addParadesWithItems(username, pass, n, usr_district);
									}else {
										gen.addItems(username, pass, n);
									}
									gen.cleanup();
								} else {
									log("Must provide username password and number of items");
									formatter.printHelp("<script-name>", options);
								}
							} catch (Exception e) {
								gen.cleanup();
								log(e.getMessage());
								e.printStackTrace();
							}
						}
					}).start();
				}
			}
			
		} catch (Exception e) {
			System.err.println("Parsing failed.  Reason: " + e.getMessage());
			e.printStackTrace();
			formatter.printHelp( "<script-name>" , options );
		}
	}

	public static void log(String msg) {
		System.out.println(msg);
	}
}

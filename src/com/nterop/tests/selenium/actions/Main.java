package com.nterop.tests.selenium.actions;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
		final String url_cmd = "url";
		final String user_file_cmd = "file";
		final String no_cmd = "n";
		final String reports_cmd = "report";
		options.addOption(url_cmd, true, "URL to test agains.");
		options.addOption(timeout_cmd, true, "Selenium Timeout.");
		options.addOption(user_file_cmd, true, "Path to the CSV file containing username passwords.");
		options.addOption(no_cmd, true, "Number of items or report to generate.");
		options.addOption(reports_cmd, false, "Create Reports. By default will create Items");

		CommandLineParser parser = new GnuParser();
		try {
			// parse the command line arguments
			final CommandLine cmd = parser.parse(options, args);
			final String user_file = cmd.getOptionValue(user_file_cmd);
			final String no = cmd.getOptionValue(no_cmd);
			final String timeout_str = cmd.getOptionValue(timeout_cmd);
			final String url = cmd.getOptionValue(url_cmd);

			final int timeout = Integer.valueOf(timeout_str).intValue();

			// read username password from file
			if (user_file != null) {
				Reader reader = new FileReader(user_file);
				CSVReader<String[]> csvParser = CSVReaderBuilder.newDefaultReader(reader);
				List<String[]> data = csvParser.readAll();


				for (final String [] users : data) {

					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								// null) {
								if (no != null && url != null) {
									Generator gen = new Generator(url, timeout);
									int n = Integer.valueOf(no).intValue();
									String username = users[0];
									String pass = users[1];
									if (!cmd.hasOption(reports_cmd)) {
										gen.addItems(username, pass, n);
									} else {
//										gen.addReports(username, pass, n);
										gen.addReportsWithDistinctItems(username, pass, n);
									}
									gen.cleanup();
								} else {
									println("Must provide username password and number of items");
									HelpFormatter formatter = new HelpFormatter();
									formatter.printHelp("ant", options);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
				}
			}
			
		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
		} catch (FileNotFoundException e) {
			System.err.println("Parsing failed.  Reason: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Parsing failed.  Reason: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void println(String msg) {
		System.out.println(msg);
	}
}

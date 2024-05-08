package com.aim.instance.reader;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.aim.instance.Location;
import com.aim.interfaces.UZFInstanceInterface;
import com.aim.interfaces.UAVInstanceReaderInterface;
import com.aim.instance.UZFInstance;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class UAVInstanceReader implements UAVInstanceReaderInterface {

	@Override
	public UZFInstanceInterface readUZFInstance(Path path, Random random) {// Reset ID counter for consistent IDs across different reads
		List<Location> enclosureLocations = new ArrayList<>();
		Location preparationArea = null;
		int enclosureCount = 0;

		try {
			List<String> lines = Files.readAllLines(path);
			boolean readingPrepArea = false;
			boolean readingEnclosures = false;

			for (String line : lines) {
				if (line.contains("PREPARATION_AREA")) {
					readingPrepArea = true;
					continue;
				}
				if (line.contains("ENCLOSURE_LOCATIONS")) {
					readingPrepArea = false;
					readingEnclosures = true;
					continue;
				}
				if (line.contains("EOF")) {
					break;
				}

				if (readingPrepArea) {
					String[] coords = line.trim().split(" ");
					preparationArea = new Location(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
					readingPrepArea = false;
				} else if (readingEnclosures) {
					String[] coords = line.trim().split(" ");
					enclosureLocations.add(new Location(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])));
					enclosureCount++;
				}
			}
			Location[] enclosureArray = enclosureLocations.toArray(new Location[0]);
			// Here you would create your instance object and return it, for example:
			return new UZFInstance(enclosureCount, enclosureArray, preparationArea, random);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/*public static void main(String[] args) {
		Path path = Paths.get("instances/uzf/square.uzf");
		Random random = new Random();
		UAVInstanceReader reader = new UAVInstanceReader();
		UZFInstanceInterface uzfInstance = reader.readUZFInstance(path, random);
	}*/
}
